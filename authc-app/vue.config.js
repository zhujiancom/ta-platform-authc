const path = require('path')

function resolve (dir) {
    return path.join(__dirname, dir)
}

module.exports = {
    outputDir: 'dist', // build 输出目录
    assetsDir: 'assets',  // 静态内容目录
    lintOnSave: true, // 是否开启eslint
    devServer: {
        proxy: {
            '^/api/sys': {
                target: 'http://127.0.0.1:9090/authc/',
                changeOrigin: true,
                ws: true,
                pathRewrite: {
                    '^/api/sys': '/sys'
                }
            },
            '^/api/core':{
                target: 'http://127.0.0.1:9092/core/',
                changeOrigin: true,
                ws: true,
                pathRewrite: {
                    '^/api/core': '/sys'
                }
            }
        }
    },
    css: {
        loaderOptions: {
            less: {
                // modifyVars: {
                //     'primary-color': 'rgba(187,152,37,0.88)',
                //     'link-color': '#1DA57A',
                //     'border-radius-base': '2px',
                // },
                javascriptEnabled: true,
                // prependData: `@import "~@/assets/less/common.less";`
            }
        }
    },
    chainWebpack: (config) => {
        config.resolve.alias
            .set('@$', resolve('src'))
            .set('@api', resolve('src/api'))
            .set('@assets', resolve('src/assets'))
            .set('@comp', resolve('src/components'))
            .set('@views', resolve('src/views'))
            .set('@layout', resolve('src/components/layouts'))
    },
    pluginOptions:{
        'style-resources-loader':{
            preProcessor: 'less',
            patterns: [
                path.resolve(__dirname, './src/assets/less/index.less')
            ]
        }
    }
}
