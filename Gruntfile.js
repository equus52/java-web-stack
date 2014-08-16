'use strict';

module.exports = function (grunt) {

  // Load grunt tasks automatically
  require('load-grunt-tasks')(grunt);

  // Time how long tasks take. Can help when optimizing build times
  require('time-grunt')(grunt);

  // Configurable paths for the application
  var app = require('./bower.json').appPath || 'WebContent';
  var appConfig = {
    dist: 'dist',
    dir: {
      appRoot: app,
      bower: app + '/vender/bower_components',
      sass: app + '/styles/sass',
      css: app + '/styles/css',
      cssGen: app + '/styles/css/gen',
      coffee: app + '/scripts/coffee',
      js: app + '/scripts/js',
      jsGen: app + '/scripts/js/gen',
      tmp: {
        css: '.tmp/styles/css'
      }
    }
  };

  var proxySnippet = require('grunt-connect-proxy/lib/utils').proxyRequest;

  // Define the configuration for all the tasks
  grunt.initConfig({

    // Project settings
    conf: appConfig,

    // Watches files for changes and runs tasks based on the changed files
    watch: {
      bower: {
        files: ['bower.json'],
        tasks: ['bower:install']
      },
      jsCompile: {
        files: ['<%= conf.dir.coffee %>/**/*.{coffee,litcoffee,coffee.md}'],
        tasks: ['js_compile']
      },
      cssCompile: {
        files: ['<%= conf.dir.sass %>/**/*.{scss,sass}'],
        tasks: ['css_compile']
      },
      gruntfile: {
        files: ['Gruntfile.js']
      },
      livereload: {
        options: {
          livereload: '<%= connect.options.livereload %>'
        },
        files: [
          '<%= conf.dir.appRoot %>/**/*.html',
          '<%= conf.dir.css %>/**/*.css',
          '<%= conf.dir.js %>/**/*.js',
          '<%= conf.dir.appRoot %>/images/**/*.{png,jpg,jpeg,gif,webp,svg}'
        ]
      }
    },

    // bower
    bower: {
      install: {
        options: {
          targetDir: '<%= conf.dir.bower %>',
          layout: 'byComponent',
          install: true,
          verbose: false,
          cleanTargetDir: true,
          cleanBowerDir: false
        }
      }
    },

    // The actual grunt server settings
    connect: {
      options: {
        port: 9000,
        // Change this to '0.0.0.0' to access the server from outside.
        hostname: 'localhost',
        livereload: 35729
      },
      livereload: {
        options: {
          open: true,
          middleware: function (connect) {
            return [
              proxySnippet,
              connect.static(appConfig.dir.appRoot)
            ];
          }
        }
      },
      proxies: [
        {
          context: '/api',
          host: 'localhost',
          port: '9010',
          rewrite: {
            '^/api': '/java-web-stack/api'
          },
          https: false,
          changeOrigin: false
        }
      ],
      dist: {
        options: {
          open: true,
          base: '<%= conf.dist %>'
        }
      }
    },

    // Make sure code styles are up to par and there are no obvious mistakes
    jshint: {
      options: {
        jshintrc: '.jshintrc',
        reporter: require('jshint-stylish')
      },
      all: {
        src: [
          'Gruntfile.js'
        ]
      }
    },

    // Empties folders to start fresh
    clean: {
      dist: ['.tmp', '<%= conf.dist %>'],
      js: '<%= conf.dir.jsGen %>',
      css: '<%= conf.dir.cssGen %>',
    },

    // Compiles CoffeeScript to JavaScript
    coffee: {
      options: {
        sourceMap: true,
        sourceRoot: ''
      },
      dist: {
        files: [{
          expand: true,
          cwd: '<%= conf.dir.coffee %>',
          src: '**/*.coffee',
          dest: '<%= conf.dir.jsGen %>',
          ext: '.js'
        }]
      },
    },

    // Compiles Sass to CSS and generates necessary files if requested
    compass: {
      options: {
        sassDir: '<%= conf.dir.sass %>',
        cssDir: '<%= conf.dir.cssGen %>',
        generatedImagesDir: '<%= conf.dir.appRoot %>/images/generated',
        imagesDir: '<%= conf.dir.appRoot %>/images',
        javascriptsDir: '<%= conf.dir.js %>',
        fontsDir: '<%= conf.dir.appRoot %>/styles/fonts',
        importPath: '<%= conf.dir.appRoot %>',
        httpImagesPath: '/images',
        httpGeneratedImagesPath: '/images/generated',
        httpFontsPath: '/styles/fonts',
        relativeAssets: false,
        assetCacheBuster: false,
        nolineComments: false,
        raw: 'Sass::Script::Number.precision = 10\n'
      },
      dist: {
      }
    },

    // Add vendor prefixed styles
    autoprefixer: {
      options: {
        browsers: ['last 1 version']
      },
      dist: {
        files: [{
          expand: true,
          cwd: '<%= conf.dir.cssGen %>/',
          src: '**/*.css',
          dest: '<%= conf.dir.cssGen %>/'
        }]
      }
    },

    // Renames files for browser caching purposes
    filerev: {
      dist: {
        src: [
          '<%= conf.dist %>/scripts/*.js',
          '<%= conf.dist %>/styles/css/*.css',
          '<%= conf.dist %>/images/**/*.{png,jpg,jpeg,gif,webp,svg}',
          '<%= conf.dist %>/styles/fonts/*'
        ]
      }
    },

    // Reads HTML for usemin blocks to enable smart builds that automatically
    // concat, minify and revision files. Creates configurations in memory so
    // additional tasks can operate on them
    useminPrepare: {
      html: '<%= conf.dist %>/index.html',
      options: {
        dest: '<%= conf.dist %>',
        flow: {
          html: {
            steps: {
              js: ['concat', 'uglifyjs'],
              css: ['cssmin']
            },
            post: {}
          }
        }
      }
    },

    // Performs rewrites based on filerev and the useminPrepare configuration
    usemin: {
      html: ['<%= conf.dist %>/**/*.html'],
      css: ['<%= conf.dist %>/**/*.css'],
      options: {
        assetsDirs: ['<%= conf.dist %>','<%= conf.dist %>/images']
      }
    },

    // ngmin tries to make the code safe for minification automatically by
    // using the Angular long form for dependency injection. It doesn't work on
    // things like resolve or inject so those have to be done manually.
    ngmin: {
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/concat/scripts',
          src: '*.js',
          dest: '.tmp/concat/scripts'
        }]
      }
    },

    // Replace Google CDN references
    cdnify: {
      dist: {
        html: ['<%= conf.dist %>/*.html']
      }
    },

    // Copies remaining files to places other tasks can use
    copy: {
      dist: {
        files: [
        {
          expand: true,
          dot: true,
          cwd: '<%= conf.dir.appRoot %>',
          dest: '<%= conf.dist %>',
          src: ['**']
        },
        ]
      },
      coffee: {
        files: [
        {
          expand: true,
          dot: true,
          cwd: '<%= conf.dir.coffee %>',
          dest: '<%= conf.dir.jsGen %>',
          src: ['**/*.coffee']
        },
        ]
      }
    },

    // Run some tasks in parallel to speed up the build process
    concurrent: {
      compile: [
        'js_clean_compile',
        'css_clean_compile',
      ]
    },

  });

  grunt.registerTask('js_compile', [
    'coffee',
    'copy:coffee',
  ]);

  grunt.registerTask('js_clean_compile', [
    'clean:js',
    'js_compile',
  ]);

  grunt.registerTask('css_compile', [
    'compass',
    'autoprefixer',
  ]);

  grunt.registerTask('css_clean_compile', [
    'clean:css',
    'css_compile',
  ]);

  grunt.registerTask('serve', 'Compile then start a connect web server', function (target) {
    if (target === 'dist') {
      return grunt.task.run(['build', 'connect:dist:keepalive']);
    }

    grunt.task.run([
      'compile',
      'configureProxies',
      'connect:livereload',
      'watch'
    ]);
  });

  grunt.registerTask('server', 'DEPRECATED TASK. Use the "serve" task instead', function (target) {
    grunt.log.warn('The `server` task has been deprecated. Use `grunt serve` to start a server.');
    grunt.task.run(['serve:' + target]);
  });

  grunt.registerTask('compile', [
    'bower:install',
    'concurrent:compile',
  ]);

  grunt.registerTask('dist', [
    'clean:dist',
    'copy:dist',
    'useminPrepare',
    'concat',
    'ngmin',
    'cdnify',
    'uglify',
    'cssmin',
    'filerev',
    'usemin',
  ]);

  grunt.registerTask('build', [
    'compile',
    'dist',
  ]);

  grunt.registerTask('default', [
    'newer:jshint',
    'build'
  ]);
};
