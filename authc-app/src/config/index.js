var path = require('path')

module.exports = {
    build: {
        env: require('./env.prod'),
        index: path.resolve(__dirname, '../dist/index.html'),
        assetsRoot: path.resolve(__dirname, '../dist'),

    }
}
