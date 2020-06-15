package com.ashtray.movie360;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import android.view.View;
import android.widget.Toast;

import com.ashtray.movie360.entities.MyFragment;
import com.ashtray.movie360.entities.MyFragment.MyFragmentNames;
import com.ashtray.movie360.entities.MyFragment.MyFragmentCallBacks;

import com.ashtray.movie360.features.FragmentAdminLogin;
import com.ashtray.movie360.features.FragmentHome;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MyFragmentCallBacks  {

    private AdView myAdView;
    private MyFragment myFragmentObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAdView = findViewById(R.id.adView);
        myAdView.setVisibility(View.GONE);

        showFragment(MyFragmentNames.HOME);
    }


    @Override
    protected void onPause() {
        super.onPause();

        //since app is in pause state so close ads but in different thread so that user don't see anything
        new Handler().postDelayed(this::closeAds, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Initialize mobile ads after 1 second
        //new Handler().postDelayed(this::loadAds, 100);
    }

    @Override
    public void onBackPressed(){
        if(!myFragmentObject.handleBackButtonPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    public void showFragment(MyFragmentNames fragmentNames) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }

        switch (fragmentNames){
            case HOME:
                myFragmentObject = new FragmentHome();
                break;
            case ADMIN_LOGIN:
                myFragmentObject = new FragmentAdminLogin();
                break;
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, myFragmentObject);
        fragmentTransaction.show(myFragmentObject);
        fragmentTransaction.commit();
    }

    private void loadAds(){
        new Handler(Looper.getMainLooper()).post(() -> {
            if(!MyApplication.getInstance().isInternetAvailable()){
                return;
            }

            if(!MyApplication.getInstance().isIsMobileAdsInitialized()){
                return;
            }

            List<String> testDeviceIds = Collections.singletonList("33BE2250B43518CCDA7DE426D04EE231");
            RequestConfiguration configuration = new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
            MobileAds.setRequestConfiguration(configuration);

            AdRequest adRequest = new AdRequest.Builder().build();
            myAdView.loadAd(adRequest);
            myAdView.setVisibility(View.VISIBLE);
        });
    }

    private void closeAds(){
        if(myAdView != null){
            myAdView.destroy();
            myAdView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showToastMessage(String message, boolean forShortTime) {
        runOnUiThread(() -> Toast.makeText(this, message, (forShortTime)? Toast.LENGTH_SHORT:Toast.LENGTH_LONG).show());
    }

    @Override
    public void setActivityTitle(String title) {
        runOnUiThread(() -> {
            if(getSupportActionBar() != null){
                getSupportActionBar().setTitle(title);
            }
        });
    }

    @Override
    public void setBackButtonEnabled(boolean enabled) {
        runOnUiThread(() -> {
            if(getSupportActionBar() != null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(enabled);
            }
        });
    }
}
