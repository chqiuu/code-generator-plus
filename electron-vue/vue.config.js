const path = require('path')
const webpack = require('webpack')
const { IgnorePlugin } = require('webpack')
const { createMockMiddleware } = require('umi-mock-middleware')
const { BundleAnalyzerPlugin } = require('webpack-bundle-analyzer')
const createThemeColorReplacerPlugin = require('./config/theme.plugin')
const GitRevisionPlugin = require('git-revision-webpack-plugin')
const GitRevision = new GitRevisionPlugin()
const buildDate = JSON.stringify(new Date().toLocaleString())

// const isProd = process.env.NODE_ENV === 'production'
const isUseCDN = process.env.IS_USE_CDN === 'true'
const isAnalyz = process.env.IS_ANALYZ === 'true'

function resolve (dir) {
  return path.join(__dirname, dir)
}

// check Git
function getGitHash () {
  try {
    return GitRevision.version()
  } catch (e) {}
  return 'unknown'
}

const assetsCDN = {
  externals: {
    vue: 'Vue',
    vuex: 'Vuex',
    'vue-router': 'VueRouter',
  },
  assets: {
    css: [],
    // https://unpkg.com/:package@:version/:file
    // https://cdn.jsdelivr.net/package:version/:file
    js: [
      '//cdn.jsdelivr.net/npm/vue@latest/dist/vue.min.js',
      '//cdn.jsdelivr.net/npm/vue-router@latest/dist/vue-router.min.js',
      '//cdn.jsdelivr.net/npm/vuex@latest/dist/vuex.min.js',
    ],
  },
}

// vue.config
const vueConfig = {
  publicPath: '',
  runtimeCompiler: true,
  pluginOptions: {
    electronBuilder: {
      // 打包参数配置
      builderOptions: {
        productName: 'code-generator', // 项目名 这也是生成的exe文件的前缀名
        // appId: "xxxxx",//包名
        copyright: 'chqiuu@qq.com', // 版权信息
        compression: 'maximum', // "store" | "normal"| "maximum" 打包压缩情况(store 相对较快)，store 39749kb, maximum 39186kb
        directories: {
          output: 'build', // 输出文件夹
        },
        asar: false, // asar打包
        extraResources: [
          {
            from: 'D:\\MyApps\\jre\\jdk8u282-b08-jre\\',
            to: 'app/server/jre',
          }, {
            from: '../server/target/code-generator-plus-1.0.6.jar',
            to: 'app/server/code-generator-plus.jar',
          }, {
            from: './extraResources/server/start.bat',
            to: 'app/server/start.bat',
          }],
        win: {
          icon: 'icon/icon_512.ico', // 图标路径
          artifactName: '${productName}-setup-${version}.${ext}',
        },
        nsis: {
          oneClick: false, // 一键安装
          perMachine: true, // 是否开启安装时权限限制（此电脑或当前用户）
          allowElevation: true, // 允许请求提升。 如果为false，则用户必须使用提升的权限重新启动安装程序。
          allowToChangeInstallationDirectory: true, // 允许修改安装目录
          installerIcon: './icon/installer_icon_512.ico', // 安装图标
          uninstallerIcon: './icon/uninstaller_icon_512.ico', // 卸载图标
          installerHeaderIcon: './icon/installer_header_icon_512.ico', // 安装时头部图标
          createDesktopShortcut: true, // 创建桌面图标
          createStartMenuShortcut: true, // 创建开始菜单图标
          shortcutName: '代码生成器', // 图标名称
        },
      },
    },
  },
  configureWebpack: {
    module: {
      rules: [
        {
          // text-loader 解析.md文件的loader vue.config.js
          test: /\.md$/,
          use: 'text-loader',
        },
      ],
    },
    plugins: [
      // Ignore all locale files of moment.js
      new IgnorePlugin(/^\.\/locale$/, /moment$/),
      new webpack.DefinePlugin({
        APP_VERSION: `"${require('./package.json').version}"`,
        GIT_HASH: JSON.stringify(getGitHash()),
        BUILD_DATE: buildDate,
      }),
    ],
    resolve: {
      alias: {
        '@ant-design/icons/lib/dist$': resolve('./src/icons.js'),
      },
    },
    externals: isUseCDN ? assetsCDN.externals : {},
  },
  chainWebpack: config => {
    // replace svg-loader
    const svgRule = config.module.rule('svg')
    svgRule.uses.clear()

    svgRule
      .oneOf('inline')
      .resourceQuery(/inline/)
      .use('vue-svg-icon-loader')
      .loader('vue-svg-icon-loader')
      .end()
      .end()
      .oneOf('external')
      .use('file-loader')
      .loader('file-loader')
      .options({
        name: 'assets/[name].[hash:8].[ext]',
      })

    // if `IS_USE_CDN` env is TRUE require on cdn assets
    isUseCDN &&
      config.plugin('html').tap(args => {
        args[0].cdn = assetsCDN.assets
        return args
      })
    // if `IS_ANALYZ` env is TRUE on report bundle info
    isAnalyz &&
      config.plugin('webpack-report').use(BundleAnalyzerPlugin, [
        {
          analyzerMode: 'static',
        },
      ])
  },
  // style config
  css: {
    loaderOptions: {
      less: {
        modifyVars: {
          // less vars，customize ant design theme
          'border-radius-base': '2px',
        },
        // DO NOT REMOVE THIS LINE
        javascriptEnabled: true,
      },
    },
  },
  devServer: {
    // development server port 8000
    port: 8000,
    // mock serve
    // before: app => {
    //   if (process.env.MOCK !== 'none' && process.env.HTTP_MOCK !== 'none') {
    //     app.use(createMockMiddleware())
    //   }
    // },
    // If you want to turn on the proxy, please remove the mockjs /src/main.jsL11
    proxy: {
      '/api': {
        target: 'http://localhost:803/',
        // target: 'http://192.168.1.204:803',
        secure: false, // 是否校验（或者说理会）对方https证书
        logLevel: 'debug', // 日志等级，默认可以不配置用于调试时打印一些代理信息
        changeOrigin: true,
        onProxyRes: function (proxyRes, req, res) {
          // 代理response事件
          console.log('res---->\n')
          console.log(proxyRes.headers)
        },
        onProxyReq: function (proxyReq, req, res) {
          // 代理requset事件
          console.log('req---->\n')
          delete req.headers.host
        },
        pathRewrite: {
          '^/api': '',
        },
      },
    },
  },
  /* ADVANCED SETTINGS */

  // disable source map in production
  productionSourceMap: false,
  // ESLint Check: DISABLE for false
  // Type: boolean | 'warning' | 'default' | 'error'
  lintOnSave: 'warning',
  // babel-loader no-ignore node_modules/*
  transpileDependencies: [],
}

// preview.pro.antdv.com only do not use in your production;
if (process.env.VUE_APP_PREVIEW === 'true') {
  console.log('Running Preview Mode')
  // add `ThemeColorReplacer` plugin to webpack plugins
  vueConfig.configureWebpack.plugins.push(createThemeColorReplacerPlugin())
}

module.exports = vueConfig
