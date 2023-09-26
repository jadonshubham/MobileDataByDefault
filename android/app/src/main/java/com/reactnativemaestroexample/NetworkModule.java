package com.reactnativemaestroexample;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class NetworkModule extends ReactContextBaseJavaModule {
    NetworkModule(ReactApplicationContext context) {
        super(context);
    }

    @Override
    public String getName() {
        return "NetworkModule";
    }

    @ReactMethod
    public void changeNetworkToMobileData() {
        Log.d("NetworkModule", "Changing Network to Mobile Data");
        forceConnectionToMobile(getReactApplicationContext());
    }

    @ReactMethod
    public void changeNetworkToWifi() {
        Log.d("NetworkModule", "Changing Network both mobile and wifi");
        forceConnectionToMobileAndWifi(getReactApplicationContext());
    }

    public static void forceConnectionToMobile(ReactApplicationContext context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkRequest.Builder request = new NetworkRequest.Builder();
            Log.d("NetworkModule", "Request Transport Cellular");
            request.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
            request.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

            connectivityManager.requestNetwork(request.build(), new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.d("NetworkModule", "Binding app to cellular");
                        connectivityManager.bindProcessToNetwork(network);
                    }
                }

                @Override
                public void onUnavailable() {
                    Log.d("NetworkModule", "Unavailable");
                }

                @Override
                public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities);
                    final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
                    Log.d(
                            "NetworkModule",
                            "NetworkStatusChanged: \nUnmetered: " + unmetered + "\nInternetAvailable: " + networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) + "\nNetViaMobileInternet: " + networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) + "\nNetViaWIFI: " + networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            );
                }

            });
        }
    }

    public static void forceConnectionToMobileAndWifi(ReactApplicationContext context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkRequest.Builder request = new NetworkRequest.Builder();
            Log.d("NetworkModule", "Request Transport Cellular");
            request.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR);
            request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            request.addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);

            connectivityManager.requestNetwork(request.build(), new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Log.d("NetworkModule", "Binding app to cellular");
                        connectivityManager.bindProcessToNetwork(network);
                    }
                }

                @Override
                public void onUnavailable() {
                    Log.d("NetworkModule", "Unavailable");
                }

                @Override
                public void onCapabilitiesChanged(@NonNull Network network, @NonNull NetworkCapabilities networkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities);
                    Log.d("NetworkModule", "onCapabilitesChanged");
                    final boolean unmetered = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED);
                    Log.d(
                            "NetworkModule",
                            "NetworkStatusChanged: \nUnmetered: " + unmetered + "\nInternetAvailable: " + networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) + "\nNetViaMobileInternet: " + networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) + "\nNetViaWIFI: " + networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    );
                }

            });
        }
    }

}