const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    proxy: {
      '/boo': {
        target: 'https://www.qyer.com',
        ws: true,
        changeOrigin: true,
        pathRewrite:{//重写路径
          '^/boo':''
        }
      }
    },
    allowedHosts: [
      'localhost',
      'www.qyer.com',
      'eth.jht213.com',
    ]
  }
})
