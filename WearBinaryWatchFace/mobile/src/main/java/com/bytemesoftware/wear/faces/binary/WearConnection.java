package com.bytemesoftware.wear.faces.binary;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by daniel on 7/4/14.
 */
public class WearConnection {

    public static final String DOT_COLOR_PATH = "/dot_color";
    public static final String DOT_COLOR_RESET_PATH = "/dot_reset";

    private final GoogleApiClient mGoogleAppiClient;
    private String TAG = "wear.bridge";

    public  WearConnection(Context context) {
        mGoogleAppiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();

    }

    public void connect(){
        mGoogleAppiClient.connect();
    }

    public void disconnect() {
        mGoogleAppiClient.disconnect();
    }

    public void sendColor(String path, int color) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(path);
        dataMap.getDataMap().putInt("value", color);
        PutDataRequest request = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleAppiClient, request);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(DataApi.DataItemResult dataItemResult) {
                Log.d("wear", "sent: " + dataItemResult);
            }
        });

    }

}
