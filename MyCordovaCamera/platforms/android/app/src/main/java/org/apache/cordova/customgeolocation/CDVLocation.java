package org.apache.cordova.customgeolocation;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class CDVLocation extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if("getLocation".equals(action)){
            new LocationHelper(args,callbackContext,cordova.getActivity()).doLocation();
            return true;
        }else if ("reverseGeocodeLocation".equals(action)){
            new LocationHelper(args,callbackContext,cordova.getActivity()).getAdress();
            return true;
        }
        return false;
    }
}
