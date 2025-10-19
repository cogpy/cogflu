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
package oculus.aperture.geo;

import oculus.aperture.spi.geo.GeopoliticalData;
import oculus.aperture.spi.geo.GeospatialData;

public class BasicGeopoliticalData implements GeopoliticalData {

  private final String region;
  private final GeospatialData geoData;
  private final Continent continent;

  /**
   * Constructs a basic immutable geopolitical object
   *
   * @param region An optional regional description.
   * @param geoData Core geospatial data.
   * @param continent One of the seven continents.
   */
  public BasicGeopoliticalData(GeospatialData geoData, String region, Continent continent) {
    this.region = region;
    this.geoData = geoData;
    this.continent = continent;
  }

  @Override
  public String getRegion() {
    return this.region;
  }

  @Override
  public GeospatialData getGeoData() {
    return this.geoData;
  }

  @Override
  public Continent getContinent() {
    return this.continent;
  }
}
