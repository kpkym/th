const HtmlWebpackPlugin = require('html-webpack-plugin')
const HtmlWebpackInlineSourcePlugin = require('html-webpack-inline-source-plugin');
const path = require('path');

module.exports = {
    publicPath: "",
    productionSourceMap: false,
    css: {
        extract: false,
    },
    configureWebpack: {
      resolve: {
        alias: {
          "#": path.resolve(__dirname, "data"),
          "##": path.resolve(__dirname, "data", process.env.NODE_ENV)
        },
        extensions: ['.js', '.vue', '.json']
      },
      optimization: {
        splitChunks: false // makes there only be 1 js file - leftover from earlier attempts but doesn't hurt
      },
      plugins: [
        new HtmlWebpackPlugin({
          filename: 'index.html', // the output file name that will be created
          template: 'src/index.html', // this is important - a template file to use for insertion
          inlineSource: '.(js|css)$' // embed all javascript and css inline
        }),
        new HtmlWebpackInlineSourcePlugin()
      ]
    },
    outputDir: `dist/${process.env.NODE_ENV}`,
    assetsDir: 'static',
    devServer: {
        proxy: 'http://127.0.0.1:8688',
        disableHostCheck: true
    }
}