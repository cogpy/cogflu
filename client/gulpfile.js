'use strict';

var _ = require('lodash');
var gulp = require('gulp');
var $ = require('gulp-load-plugins')();


// --- Config
var config = {
  // Port for development server
  port: 9000,

  // Paths to use for file output
  paths: {
    temp: '.tmp',
    dist: 'dist'
  },
  
  // Proxy configuration
  proxy: {
    root: 'http://localhost:8080',
    paths: [
      '/aperture',
      '/rest'
    ]
  }
};


// --- Stylus
gulp.task('stylus', function() {  
  return gulp.src(['app/styles/main.styl', 'app/styles/development.styl'])
    .pipe($.stylus({
      use: [require('nib')],
      compress: true
    }))
    .pipe(gulp.dest(config.paths.temp+'/styles'))
    .pipe($.size());
});

// --- JSHint
gulp.task('jshint', function() {
  return gulp.src('app/scripts/**/*.js')
    .pipe($.jshint())
    .pipe($.jshint.reporter(require('jshint-stylish')))
    .pipe($.size());
});

// --- Build: useref finds js + ccs -> concat + ugly + optimize
gulp.task('html', ['stylus', 'jshint'], function() {
  var jsFilter = $.filter('**/*.js');
  var cssFilter = $.filter('**/*.css');

  return gulp.src('app/*.html')
    .pipe($.preprocess())
    .pipe($.useref.assets({searchPath: '{'+config.paths.temp+',app}'}))
    .pipe(jsFilter)
    .pipe($.uglify())
    .pipe(jsFilter.restore())
    .pipe(cssFilter)
    .pipe($.csso())
    .pipe(cssFilter.restore())
    .pipe($.useref.restore())
    .pipe($.useref())
    .pipe(gulp.dest(config.paths.dist))
    .pipe($.size());
});

// --- Compress images
// TODO Fix on Windows
// gulp.task('images', function() {
//     return gulp.src('app/images/**/*')
//         .pipe($.cache($.imagemin({
//             optimizationLevel: 3,
//             progressive: true,
//             interlaced: true
//         })))
//         .pipe(gulp.dest('dist/images'))
//         .pipe($.size());
// });

// --- Place all fonts in dist
gulp.task('fonts', function() {
  return $.bowerFiles()
    .pipe($.filter('**/*.{eot,svg,ttf,woff}'))
    .pipe($.flatten())
    .pipe(gulp.dest(config.paths.dist+'/fonts'))
    .pipe($.size());
});

// --- Copy non-source, non-style files
gulp.task('extras', function() {
  return gulp.src(['app/*.*', '!app/*.html'], { dot: true })
    .pipe(gulp.dest(config.paths.dist));
});

// --- Clean
gulp.task('clean', function() {
  return gulp.src([config.paths.temp, config.paths.dist], { read: false })
    .pipe($.clean());
});

// --- Server
gulp.task('connect', function() {
  var url = require('url');
  var proxy = require('proxy-middleware');
  var connect = require('connect');

  var app = connect()
    .use(require('connect-livereload')({ port: 35729 }))
    .use(connect.static('app'))
    .use(connect.static(config.paths.temp))
    .use(connect.directory('app'));

  _.each(config.proxy.paths, function(path) {
    var options = url.parse(config.proxy.root+path);
    options.route = path;
    console.log('Proxy: ' + path + ' to ' + config.proxy.root+path);
    app.use(proxy(options));
  });

  require('http').createServer(app)
    .listen(config.port)
    .on('listening', function() {
      console.log('Started connect web server on http://localhost:'+config.port);
    });
});

// --- Live reload
gulp.task('watch', ['stylus', 'connect'], function() {
  var server = $.livereload();

  // watch for changes
  gulp.watch([
    'app/*.html',
    config.paths.temp+'/styles/**/*.css',
    'app/scripts/**/*.js',
    'app/images/**/*'
  ]).on('change', function (file) {
    // trigger browser reload of changed file
    server.changed(file.path);
  });

  gulp.watch('app/styles/**/*.styl', ['stylus']);
  gulp.watch('app/scripts/**/*.js', ['jshint']);
  // gulp.watch('app/images/**/*', ['images']);
});



// ====== Usage
// --- Development: watch for changes, run dev server
gulp.task('serve', ['watch'], function() {});

// --- Deploy: end-to-end build to dist/
gulp.task('build', ['html', /*'images',*/ 'fonts', 'extras']);

// --- Default: clean, build
gulp.task('default', ['clean'], function() {
  gulp.start('build');
});

