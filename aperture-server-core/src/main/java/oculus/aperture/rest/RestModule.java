/**
 * Copyright (c) 2013-2014 Oculus Info Inc. http://www.oculusinfo.com/
 *
 * <p>Released under the MIT License.
 *
 * <p>Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * <p>The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * <p>THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package oculus.aperture.rest;

import com.google.inject.Provides;
import com.google.inject.servlet.ServletModule;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import oculus.aperture.common.rest.ResourceDefinition;
import oculus.aperture.config.ClientConfigResource;
import org.restlet.Application;
import org.restlet.Context;
import org.restlet.routing.Router;
import org.restlet.routing.TemplateRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This module sets up all REST related infrastructure, including:
 *
 * <ul>
 *   <li>Binding the /rest/* endpoint to the RestServlet
 *   <li>Binding a Restlet Application object that is configured with all ServerResources mapped to
 *       paths in other modules
 * </ul>
 *
 * @author rharper
 */
public class RestModule extends ServletModule implements ServletContextListener {

  private static final String REST_BASE = "/rest";

  /**
   * Simple passthrough filter that allows all requests through without caching. This replaces the
   * ehcache-web dependency which is not compatible with Jakarta Servlet API.
   *
   * <p>For production use, consider implementing a Jakarta-compatible caching solution such as
   * JCache (JSR-107) or Spring Cache.
   */
  private static class SimplePassthroughFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
      // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
      // Simply pass through to the next filter/servlet in the chain
      chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
      // No cleanup needed
    }
  }

  final Logger logger = LoggerFactory.getLogger(getClass());

  private final ServletContext context;

  private SimplePassthroughFilter filter;

  public RestModule(ServletContext context) {
    this.context = context;
  }

  @Override
  protected void configureServlets() {
    /*
     * Filters
     */
    // Deny access to rpc unless authenticated
    // filter("/rpc").through(SubjectAuthenticatedFilter.class);

    /*
     * Servlets
     */

    // Handle all RPC requests with a servlet bound to the
    // RPCServlet Name
    // TODO Make configurable
    logger.debug("Setting REST base path to '" + REST_BASE + "/*'");
    serve(REST_BASE + "/*").with(RestletServlet.class);

    // Note: Caching has been temporarily disabled for Jakarta Servlet API compatibility
    // The previous ehcache-web implementation is not compatible with Jakarta EE 10+
    // For production use, consider implementing a Jakarta-compatible caching solution
    logger.info("REST caching is disabled (Jakarta Servlet API compatibility)");

    filter = new SimplePassthroughFilter();

    // Filter all REST calls
    filter(REST_BASE + "/*").through(filter);
  }

  /**
   * Creates the restlet application that will be used to handle all rest calls Takes a map of paths
   * to resource classes
   *
   * <p>Use the following three lines to access the routing multibinder: TypeLiteral<String>
   * pathType = new TypeLiteral<String>() {}; TypeLiteral<Class<? extends ServerResource>> clazzType
   * = new TypeLiteral<Class<? extends ServerResource>>() {}; MapBinder<String, Class<? extends
   * ServerResource>> resourceBinder = MapBinder.newMapBinder(binder(), pathType, clazzType);
   * resourceBinder.bind("/my/path").toInstance(MyResource.class);
   */
  @Provides
  Application createApplication(FinderFactory factory, Map<String, ResourceDefinition> routes) {

    Context context = new Context();
    Application application = new Application();
    application.setContext(context);

    Router router = new Router(context);

    // route client config (always).
    router.attach("/config.js", factory.finder(ClientConfigResource.class));

    /*
     * Set binding rules here
     */
    for (Entry<String, ResourceDefinition> entry : routes.entrySet()) {
      final ResourceDefinition defn = entry.getValue();

      logger.info("Binding '" + entry.getKey() + "' to " + defn);
      TemplateRoute route = router.attach(entry.getKey(), factory.finder(defn.getResource()));

      // add any variable customizations.
      route.getTemplate().getVariables().putAll(defn.getVariables());
    }

    application.setInboundRoot(router);

    return application;
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {}

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    // Note: ehcache shutdown code removed for Jakarta Servlet API compatibility
    // If caching is re-implemented, add appropriate shutdown logic here
  }
}
