package com.ashtray.movie360;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.ads.MobileAds;

public class MyApplication extends Application {

    private static MyApplication application;
    public static MyApplication getInstance() {
        return application;
    }

    private boolean IS_MOBILE_ADS_INITIALIZED;
    public boolean isIsMobileAdsInitialized() { return IS_MOBILE_ADS_INITIALIZED; }

    @Override
    public void onCreate() {
        super.onCreate();

        // saving the application object for handling context
        application = this;

        //initializing mobile ads sdk
        IS_MOBILE_ADS_INITIALIZED = false;
        if (isInternetAvailable()) {
            MobileAds.initialize(MyApplication.getInstance(), initializationStatus -> IS_MOBILE_ADS_INITIALIZED = true);
        }
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager == null) return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}