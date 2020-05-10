const path = require('path');
const scriptsPath = './src/main/resources/static/scripts/';

module.exports = {
    devtool: 'source-map',

    entry: {
        'bibliography': scriptsPath + 'bibliography.js',
        'choices': scriptsPath +'choices.js',
        'main': scriptsPath +'main.js',
        'search-graphics': scriptsPath +'search-graphics',
        'whichpet': scriptsPath +'whichpet.js',
    },

    module: {
        rules: [
            {
                test: /\.(js)$/,
                exclude: /node_modules/,
                use: ['babel-loader']
            }
        ]
    },

    output: {
        filename: '[name].min.js',
        path: path.resolve(__dirname, 'target/classes/static/scripts'),
    },
};
