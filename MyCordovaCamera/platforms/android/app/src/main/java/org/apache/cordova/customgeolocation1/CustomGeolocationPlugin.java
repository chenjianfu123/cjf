package org.apache.cordova.customgeolocation1;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomGeolocationPlugin  {

    public interface CallbackContext{
        void error(String msg);
        void success(Location currentLocation);
    }

    private final String ACTION_LOCATION_URL = "location";
    private final String ACTION_INTERVAL_URL = "interval";
    private final String ACTION_STOP_INTERVAL_URL = "stop_interval";

    private static final String GPS001 = "GPS001"; // is mock locaiton error
    private static final String GPS002 = "GPS002"; // mGPS.canGetLocation is false
    private static final String GPS003 = "GPS003"; // mGPS getCurrentLocation is null


    CallbackContext callbackContext;

    private GPSTracker_Interval gpsTracker;

    boolean isMockLocationAllow = false;

    private CustomGeolocationPlugin(){}

    static CustomGeolocationPlugin instance = new CustomGeolocationPlugin();

    public static CustomGeolocationPlugin getInstance(){
        return instance;
    }

    public void doLocation(Context context, com.alibaba.fastjson.JSONObject p,CallbackContext callbackContext){
        JSONArray data = new JSONArray();
        try {
            data.put(new JSONObject(p.toJSONString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        execute(context,ACTION_LOCATION_URL,data,callbackContext);

    }

    public void doInterval(Context context,CallbackContext callbackContext){
        execute(context,ACTION_INTERVAL_URL,null,callbackContext);
    }

    public void doStopInterval(Context context, CallbackContext callbackContext){
        execute(context,ACTION_STOP_INTERVAL_URL,null,callbackContext);
    }

    public boolean execute(Context context,final String action, final JSONArray data, final CallbackContext callbackContext) {
        if (ACTION_LOCATION_URL.equals(action)) {
            try{
                JSONObject jsonObject = (JSONObject) data.get(0);
                this.isMockLocationAllow = jsonObject.getBoolean("isMockLocationAllow");
            }
            catch (JSONException e){
                Log.e("CustomGeolocationPlugin", "JSONException");
            }

//            Context context = cordova.getActivity().getApplicationContext();
            final GPSTracker mGPS = new GPSTracker(context);
            if(mGPS.canGetLocation){

                // delay to get location
                if (mGPS.getCurrentLocation() == null ||
                        mGPS.getCurrentLocation().getLongitude() == 0.0 ||
                        mGPS.getCurrentLocation().getLongitude() == 0.0){
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handleLocation(mGPS.getCurrentLocation(), callbackContext);
                        }
                    }, 2000);
                }
                else{
                    handleLocation(mGPS.getCurrentLocation(), callbackContext);
                }


            } else {
                // can't get the location
                Log.e("CustomGeolocationPlugin", "mGPS.canGetLocation is false");
                callbackContext.error(GPS002);
            }
            return true;
        } else if (ACTION_INTERVAL_URL.equals(action)) {
            startDetect(context,data, callbackContext);
            return true;
        } else if (ACTION_STOP_INTERVAL_URL.equals(action)) {
            stopDetect(context,data, callbackContext);
            return true;
        } else {
            return false;
        }
    }

    private void handleLocation(Location location, final CallbackContext callbackContext){
        Location currentLocation = location;
        if (currentLocation != null &&
                currentLocation.getLongitude() != 0.0 &&
                currentLocation.getLongitude() != 0.0){

            if (currentLocation.isFromMockProvider() && !this.isMockLocationAllow){
                Log.e("CustomGeolocationPlugin", "location isFromMockProvider is true");
                callbackContext.error(GPS001);
            }
            else{
                double mLat = currentLocation.getLatitude();
                double mLong = currentLocation.getLongitude();
//                callbackContext.success(mLat + "," + mLong);
                callbackContext.success(currentLocation);
                Log.d("CustomGeolocationPlugin", "Latitude: " + mLat + ", Longitude: " + mLong);
            }
        }
        else{
            Log.e("CustomGeolocationPlugin", "mGPS getCurrentLocation is null");
            callbackContext.error(GPS003);
        }
    }

    private void startDetect(Context context,final JSONArray data, final CallbackContext callbackContext){
        if (gpsTracker == null){
            gpsTracker = new GPSTracker_Interval(context);
        }
        gpsTracker.startDetection(new GPSTracker_Interval.OnDetectLocationListener() {
            @Override
            public void onLocationDetect(Location location) {
                Log.d("CustomGeolocationPlugin", "CustomGeolocationPlugin -> onLocationDetect -> latlng : " + location.getLatitude() + "," + location.getLongitude());
//                callbackContext.success(location.getLatitude() + "," + location.getLongitude());
                callbackContext.success(location);
            }

            @Override
            public void onError(String errorCode) {
                Log.e("CustomGeolocationPlugin", "CustomGeolocationPlugin -> onError -> " + errorCode);
                callbackContext.error(errorCode);
            }
        });
    }

    private void stopDetect(Context context,final JSONArray data, final CallbackContext callbackContext){
        if (gpsTracker == null){
            gpsTracker = new GPSTracker_Interval(context);
        }
        Log.e("CustomGeolocationPlugin", "CustomGeolocationPlugin -> interval -> stopDetect");

        gpsTracker.stopDetection();
    }
}
