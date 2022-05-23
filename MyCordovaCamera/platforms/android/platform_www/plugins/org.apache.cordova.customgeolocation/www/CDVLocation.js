cordova.define("org.apache.cordova.customgeolocation.CDVLocation", function(require, exports, module) {
var exec = require('cordova/exec');

exports.getLocation = function (arg0, success, error) {
    exec(success, error, 'CDVLocation', 'getLocation', [arg0]);
};
exports.reverseGeocodeLocation = function (arg0, success, error) {
    exec(success, error, 'CDVLocation', 'reverseGeocodeLocation', [arg0]);
};
});
