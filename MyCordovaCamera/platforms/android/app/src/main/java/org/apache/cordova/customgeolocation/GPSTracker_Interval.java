package org.apache.cordova.customgeolocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public final class GPSTracker_Interval implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // 1 minute

    private static final int LOCATION_UPDATE_COUNT = 3;
    private static final int TIMEOUT = 50000;

    private static final String GPS011 = "GPS011"; // GPS is disable
    private static final String GPS012 = "GPS012"; // location is null
    private static final String GPS013 = "GPS013"; // Timeout for getting location information
    private static final String GPS014 = "GPS014"; // mock location

    private int currentLocationUpdateCount = 0;

    public interface OnDetectLocationListener {
        public void onLocationDetect(Location location);

        public void onError(String errorCode);
    }

    private static final String TAG = "GPSTracker_Interval";

    private final Context mContext;

    boolean isGPSEnabled = false;

    LocationManager locationManager;
    private OnDetectLocationListener onDetectLocationListener;

    Location lastKnownLocation;

    Handler handler;
    Runnable runnable;

    public GPSTracker_Interval(Context context) {
        this.mContext = context;
    }

    public void addDetectLocationListener(OnDetectLocationListener onDetectLocationListener) {
        this.onDetectLocationListener = onDetectLocationListener;
    }

    @SuppressLint("MissingPermission")
    public void startDetection(OnDetectLocationListener onDetectLocationListener) {
        this.logD("startDetection");
        currentLocationUpdateCount = 0;
        startTimer();
        if (onDetectLocationListener != null) {
            this.onDetectLocationListener = onDetectLocationListener;
        }

        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!this.isGPSEnabled) {
            if (this.onDetectLocationListener != null) {
                this.logE("startDetection -> error: " + GPS011);
                this.onDetectLocationListener.onError(GPS011);
                this.stopDetection();
            }
        } else {
            this.lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null){
                this.logD("startDetection -> lastKnownLocation -> latlng: " + ((lastKnownLocation != null) ? (lastKnownLocation.getLatitude() + "," + lastKnownLocation.getLongitude()) : "location is empty") + ", isFromMockProvider: " + lastKnownLocation.isFromMockProvider() + ", provider: " + lastKnownLocation.getProvider());
            }
            else{
                this.logD("startDetection -> lastKnownLocation is null");
            }
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        }
    }

    public void stopDetection() {
        this.logD("stopDetection");
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker_Interval.this);
        }
        if (this.onDetectLocationListener != null) {
            this.onDetectLocationListener = null;
        }
        this.stopTimer();
    }


    private void logD(String message){
        Log.d(TAG, "GPSTracker_Interval -> " + message);
    }

    private void logE(String message){
        Log.e(TAG, "GPSTracker_Interval -> " + message);
    }


    @Override
    public void onLocationChanged(Location location) {
        currentLocationUpdateCount++;
        this.logD("onLocationChanged -> latlng: " + ((location != null) ? (location.getLatitude() + "," + location.getLongitude()) : "location is empty") + ", currentLocationUpdateCount: " + currentLocationUpdateCount + ", isFromMockProvider: " + location.isFromMockProvider() + ", provider: " + location.getProvider());
        if (currentLocationUpdateCount >= LOCATION_UPDATE_COUNT){
            if (this.onDetectLocationListener != null){
                if (location != null){
                    this.logD("onLocationChanged -> isFromMockProvider: " + location.isFromMockProvider());
                    if (!location.isFromMockProvider()){
                        this.onDetectLocationListener.onLocationDetect(location);
                    }
                    else{
                        this.onDetectLocationListener.onError(GPS014);
                    }
                    stopDetection();
                }
                else{
                    this.onDetectLocationListener.onError(GPS012);
                    stopDetection();
                }
            }
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        this.logD("onProviderDisabled -> provider : " + provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        this.logD("onProviderEnabled -> provider : " + provider);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        this.logD("onStatusChanged -> provider : " + provider + "status : " + status);

    }

    private void startTimer(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (onDetectLocationListener != null){
                    onDetectLocationListener.onError(GPS013);
                }
                stopDetection();
            }
        };

        handler.postDelayed(runnable, TIMEOUT);
    }

    private void stopTimer(){
        if (handler != null && runnable != null){
            handler.removeCallbacks(runnable);
        }
    }
}