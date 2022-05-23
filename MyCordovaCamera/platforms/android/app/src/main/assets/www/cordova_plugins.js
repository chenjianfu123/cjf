cordova.define('cordova/plugin_list', function(require, exports, module) {
  module.exports = [
    {
      "id": "com.cs.camera.CSCamera",
      "file": "plugins/com.cs.camera/www/CSCamera.js",
      "pluginId": "com.cs.camera",
      "clobbers": [
        "cordova.plugins.CSCamera"
      ]
    },
    {
      "id": "org.apache.cordova.zoloz.ZolozEkycPlugin",
      "file": "plugins/org.apache.cordova.zoloz/www/ZolozEkycPlugin.js",
      "pluginId": "org.apache.cordova.zoloz",
      "clobbers": [
        "cordova.plugins.ZolozEkycPlugin"
      ]
    },
    {
      "id": "org.apache.cordova.customgeolocation.CDVLocation",
      "file": "plugins/org.apache.cordova.customgeolocation/www/CDVLocation.js",
      "pluginId": "org.apache.cordova.customgeolocation",
      "clobbers": [
        "cordova.plugins.CDVLocation"
      ]
    }
  ];
  module.exports.metadata = {
    "com.cs.camera": "1.0.0",
    "org.apache.cordova.zoloz": "1.0.0",
    "org.apache.cordova.customgeolocation": "1.0.0"
  };
});