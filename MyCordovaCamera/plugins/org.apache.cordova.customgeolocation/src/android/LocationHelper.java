package org.apache.cordova.customgeolocation;

import android.Manifest;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.tbruyelle.rxpermissions2.RxPermissions;

import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class LocationHelper {


    CallbackContext cordovaCallbackContext;
    Activity activity;
    JSONArray args;
    CustomGeolocationPlugin.CallbackContext callbackContext;
    private  static  String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};

    public LocationHelper(JSONArray args, CallbackContext context,Activity activity) {
        this.args = args;
        this.cordovaCallbackContext = context;
        this.activity = activity;

        callbackContext = new CustomGeolocationPlugin.CallbackContext() {
            final JSONObject rJson = new JSONObject();
            @Override
            public void error(String msg) {

                cordovaCallbackContext.error(msg);
            }
            @Override
            public void success(Location currentLocation) {
                org.json.JSONObject jsonObject = new org.json.JSONObject();

                try {

                    jsonObject.put("latitude",currentLocation.getLatitude());
                    jsonObject.put("longitude",currentLocation.getLongitude());


                } catch (JSONException e) {
                    cordovaCallbackContext.error("LocationHelper:"+e.getMessage());
                }



            }
        };
    }

    public void doLocation() {
        applyPermissons(0, args, activity);

    }

    public void doLocationInterval() {
        applyPermissons(1, args, activity);
    }

    public void doLocationIntervalStop() {
        applyPermissons(2, args, activity);
    }

    public void getAdress(){
        String latitude="";
        String longitude="";
        if(args!=null&&args.length()>0){
            JSONObject  object = JSON.parseObject(args.optJSONObject(0).toString());
            latitude = object.getString("latitude");
            longitude = object.getString("longitude");
        }else {
            cordovaCallbackContext.error("Enter reference is null");
        }
        if(longitude.isEmpty()||latitude.isEmpty()){
            cordovaCallbackContext.error("longitude or latitude is null");
        }
        org.json.JSONObject jsonObject = new org.json.JSONObject();
        List<Address> result = null;
        Geocoder gc = new Geocoder(activity, Locale.getDefault());
        try {
            result = gc.getFromLocation(Double.parseDouble(latitude),
                    Double.parseDouble(longitude), 1);
            Address address = result.get(0);
            jsonObject.put("country",address.getCountryName());
            jsonObject.put("ISOcountryCode",address.getCountryCode());
            jsonObject.put("administrativeArea",address.getAdminArea());
            jsonObject.put("locality",address.getLocality());
            jsonObject.put("subLocality",address.getSubLocality());
            jsonObject.put("thoroughfare",address.getThoroughfare());
            jsonObject.put("name",address.getFeatureName());
            jsonObject.put("addressLine",address.getAddressLine(0));
            jsonObject.put("subAdminArea",address.getSubAdminArea());
            jsonObject.put("subAdminArea",address.getSubThoroughfare());
            cordovaCallbackContext.success(jsonObject.toString());
        } catch (IOException | JSONException e) {
            cordovaCallbackContext.error(e.getMessage());
        }
    }
    private void applyPermissons(final int type, final JSONArray args , final Activity context ){
        JSONObject object = new JSONObject();
        if(args!=null&&args.length()>0){
            object = JSON.parseObject(args.optJSONObject(0).toString());
        }
        JSONObject finalObject = object;
        context.runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                new RxPermissions(context)
                        .request(permissions)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean aBoolean) {
                                if(aBoolean){
                                    if(type == 0){
                                        CustomGeolocationPlugin.getInstance().doLocation(context, finalObject, callbackContext);
                                    }else if(type == 1){
                                        CustomGeolocationPlugin.getInstance().doInterval(context, callbackContext);
                                    }else if(type == 2){
                                        CustomGeolocationPlugin.getInstance().doStopInterval(context, null);
                                    }

                                }else{

                                    cordovaCallbackContext.error("You cant access to Document scan,as Camera permission is not enable");
                                }
                            }
                        });
            }
        });
    }
}
