package com.reactnativemaestroexample

import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class NetworkModule(reactContext: ReactApplicationContext): ReactContextBaseJavaModule(reactContext) {
    private val appContext = reactContext;
    private val connectivityManager = appContext.getSystemService(ConnectivityManager::class.java);
    private val TAG = "NetworkModule";

    override fun getName() = "NetworkModule";

    @ReactMethod fun changeNetworkToCellular(promise: Promise) {
        // TODO: Before binding check if mobile data is on
        bindAppToCellularTransportNetwork(promise);
    }

    @ReactMethod fun changeNetworkToWifi() {
        bindAppToDefaultNetwork();
    }

    private fun bindAppToCellularTransportNetwork(promise: Promise) {
        val cellularNetworkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build();

        connectivityManager.requestNetwork(cellularNetworkRequest, object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network : Network) {
                printNetwork("Available Network: ", network)
                connectivityManager.bindProcessToNetwork(network)
                promise.resolve("Switched to cellular");
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                printNetwork("Losing Network: ", network)
            }

            override fun onLost(network : Network) {
                printNetwork("Lost Network: ", network)
            }

            override fun onUnavailable() {
                Log.d(TAG, "Unavailable Network")
                promise.reject("SwitchToCellular", "Unable to switch, probably data is off")
            }

            override fun onCapabilitiesChanged(network : Network, networkCapabilities : NetworkCapabilities) {
                printNetwork("Capabilities changed : ", network)
            }

            override fun onLinkPropertiesChanged(network : Network, linkProperties : LinkProperties) {
                Log.d(TAG, "Link properties changed: $linkProperties")
            }
        }, 1000)
    }

    private fun bindAppToDefaultNetwork() {
        Log.d(TAG, "Listen to wifi")
        connectivityManager.bindProcessToNetwork(null);
    }

    private fun printNetwork(prefix: String, network: Network) {
        val currActiveNetworkCap = connectivityManager.getNetworkCapabilities(network);
        if(currActiveNetworkCap !== null) {
            val isCellular = currActiveNetworkCap.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            val isWifi = currActiveNetworkCap.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            Log.d(TAG, "$prefix\nisCellular: $isCellular\nisWifi: $isWifi");
        }
    }
}