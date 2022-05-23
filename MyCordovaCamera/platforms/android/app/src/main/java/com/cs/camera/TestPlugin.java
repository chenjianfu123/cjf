package com.cs.camera;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class TestPlugin extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            if(message!=null){
                callbackContext.success("nice");
            }else {
                callbackContext.error("oooo");
            }

            return true;
        }
        return false;
    }
}
