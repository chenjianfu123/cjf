cordova.define("com.cs.camera.CSCamera", function(require, exports, module) {
var exec = require('cordova/exec');

exports.toast = function (arg0, success, error) {
   alert(arg0);
    exec(success, error, 'CSCamera', 'coolMethod', [arg0]);
};

});
