var exec = require('cordova/exec');

exports.getMetaInfo = function (arg0, success, error) {
    exec(success, error, 'ZolozEkycPlugin', 'getMetaInfo', [arg0]);
};
exports.zolozEkyc = function (arg0, success, error) {
    exec(success, error, 'ZolozEkycPlugin', 'zolozEkyc', [arg0]);
};