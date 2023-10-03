package com.reactnativemaestroexample;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class NetworkModuleOld extends ReactContextBaseJavaModule implements LifecycleEventListener {
    private static int selectedNetworkType = 1; // 1 = wifi, 0 = mobile data
    private static final String TAG = "NetworkModule";
    private static ConnectivityManager mConnectivityManager;
    private static ConnectivityNetworkCallback mNetworkCallback;

    private ReactApplicationContext appContext;

    NetworkModuleOld(ReactApplicationContext context) {
        super(context);
        appContext = context;
        context.addLifecycleEventListener(this);
        mConnectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkCallback = new ConnectivityNetworkCallback();
    }

    @Override
    public String getName() {
        return "NetworkModuleOld";
    }

    @ReactMethod
    public void changeNetworkToMobileData() {
        Log.d(TAG, "Changing Network to Mobile Data");
        selectedNetworkType = 0;
        forceConnection(appContext);
    }

    @ReactMethod
    public void changeNetworkToWifi() {
        Log.d(TAG, "Changing Network both mobile and wifi");
        selectedNetworkType = 1;
        forceConnection(appContext);
    }

    public static void forceConnection(ReactApplicationContext context) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkRequest.Builder request = new NetworkRequest.Builder();
            Log.d("NetworkModule", "Request Transport Cellular");
            if(selectedNetworkType == 0) {
                request.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
                request.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
            mConnectivityManager.requestNetwork(request.build(),mNetworkCallback );
        }
    }

    @Override
    public void onHostResume() {
        Log.d(TAG, "onHostResume");
        forceConnection(appContext);
    }

    @Override
    public void onHostPause() {
        Log.d(TAG, "onHostPause");
    }

    @Override
    public void onHostDestroy() {
        Log.d(TAG, "onHostDestroy");
    }

    private class ConnectivityNetworkCallback extends ConnectivityManager.NetworkCallback {
        @Override
        public void onAvailable(Network network) {
            super.onAvailable(network);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(selectedNetworkType == 0) {
                    Log.d(TAG, "Binding app to cellular");
                    mConnectivityManager.bindProcessToNetwork(network);
                } else {
                    Log.d(TAG, "Binding app to default");
                    mConnectivityManager.bindProcessToNetwork(null);
                }
            }
        }

        @Override
        public void onUnavailable() {
            Log.d(TAG, "Unavailable");
        }

        @Override
        public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
            super.onCapabilitiesChanged(network, networkCapabilities);
            final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
            Log.d(
                    TAG,
                    "NetworkStatusChanged: \nUnmetered: " + unmetered + "\nInternetAvailable: " + networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) + "\nNetViaMobileInternet: " + networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) + "\nNetViaWIFI: " + networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            );
        }
    }
}