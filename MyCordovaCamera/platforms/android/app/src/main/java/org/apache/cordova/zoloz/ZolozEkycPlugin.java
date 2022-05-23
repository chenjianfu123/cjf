package org.apache.cordova.zoloz;


import android.content.Context;
import android.os.Handler;
import android.util.Log;



import com.alibaba.fastjson.JSON;
import com.ap.zoloz.hummer.api.IZLZCallback;
import com.ap.zoloz.hummer.api.ZLZConstants;
import com.ap.zoloz.hummer.api.ZLZFacade;
import com.ap.zoloz.hummer.api.ZLZRequest;
import com.ap.zoloz.hummer.api.ZLZResponse;


import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class ZolozEkycPlugin extends CordovaPlugin {
    Context context;
    private static final String TAG = "ZolozEkycPlugin";
    private ZolozEkycInfo zolozEkycInfo;


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        context = cordova.getContext();
        if (action.equals("getMetaInfo")) {

            this.metaInfo(callbackContext);
            return true;
        } else if (action.equals("zolozEkyc")) {
            startZolozEkyc(args,callbackContext);
            return true;
        }
        return false;
    }

    private void metaInfo(CallbackContext callbackContext) {
        String metaInfo = getMetaInfo();
        if (metaInfo != null && metaInfo.length() > 0) {

            callbackContext.success(metaInfo);

        } else {
            callbackContext.error("metaInfo is null");
        }
    }

    private String getMetaInfo() {
        return ZLZFacade.getMetaInfo(context);
    }


    private void startZolozEkyc(JSONArray args, CallbackContext callbackContext) {
        try {
            String object = args.getString(0);
            zolozEkycInfo = com.alibaba.fastjson.JSON.parseObject(object, ZolozEkycInfo.class);
            final ZLZFacade zlzFacade = ZLZFacade.getInstance();
            final ZLZRequest request = new ZLZRequest();
            request.zlzConfig = zolozEkycInfo.clientCfg;

            request.bizConfig.put(ZLZConstants.CONTEXT, cordova.getActivity());
            request.bizConfig.put(ZLZConstants.PUBLIC_KEY, zolozEkycInfo.rsaPubKey);
//                request.bizConfig.put(ZLZConstants.CHAMELEON_CONFIG_PATH, "config_reallId.zip");
            request.bizConfig.put(ZLZConstants.LOCALE, zolozEkycInfo.lang);

            new Handler().postAtFrontOfQueue(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "start zoloz");
                    zlzFacade.start(request, new IZLZCallback() {
                        @Override
                        public void onCompleted(ZLZResponse response) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("transactionId", zolozEkycInfo.transactionId);
                                jsonObject.put("response",JSON.toJSONString(response));
                                callbackContext.success(jsonObject);
                            } catch (JSONException e) {
                                callbackContext.error(e.getMessage());
                            }
                        }

                        @Override
                        public void onInterrupted(ZLZResponse response) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("transactionId", zolozEkycInfo.transactionId);
                                jsonObject.put("response",JSON.toJSONString(response));
                                callbackContext.error(jsonObject);
                            } catch (JSONException e) {
                                callbackContext.error(e.getMessage());
                            }
                        }
                    });
                }
            });
        } catch (JSONException e) {
            callbackContext.error(e.getMessage());
        }
    }

}
