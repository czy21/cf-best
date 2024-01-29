const cracoLessPlugin = require('craco-less');
const webpackConfigPlugin = require("./webpack.config")
const {CracoAliasPlugin, configPaths} = require('react-app-rewire-alias')

module.exports = {
    eslint: {
        enable: false,// process.env.NODE_ENV === "development"
    },

    plugins: [
        {
            plugin: cracoLessPlugin,
            options: {
                lessLoaderOptions: {
                    lessOptions: {
                        javascriptEnabled: true,
                    },
                },
            },
        },
        {
            plugin: CracoAliasPlugin,
            options: {alias: configPaths('./tsconfig.extend.json')}
        },
        {plugin: webpackConfigPlugin, options: {preText: "Will log the webpack config:"}}
    ],
};