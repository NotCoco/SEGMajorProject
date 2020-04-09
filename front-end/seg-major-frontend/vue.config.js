module.exports = {
    chainWebpack: config => {
      config
      .plugin('html')
      .tap(args => {
        args[0].title = 'KCH Paediatric Liver Service'
        return args
      })
    }
  }