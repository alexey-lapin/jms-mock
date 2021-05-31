module.exports = {
  publicPath: process.env.VUE_APP_PUBLIC_PATH,
  devServer: {
    proxy: {
      [`^${process.env.VUE_APP_API_BASE_PATH}`]: {
        target: process.env.VUE_APP_API_TARGET,
        changeOrigin: true,
      },
    },
  },
  configureWebpack: (config) => {
    if (process.env.NODE_ENV === "production") {
      config.devtool = "";
    }
  },
};
