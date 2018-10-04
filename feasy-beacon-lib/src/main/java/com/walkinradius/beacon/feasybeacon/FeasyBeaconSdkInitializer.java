package com.walkinradius.beacon.feasybeacon;

import android.app.Activity;

import com.feasycom.controler.FscBeaconApiImp;

public class FeasyBeaconSdkInitializer {

    private static FscBeaconApiImp instance;

    public static void initializeSdk(Activity activity) {
        instance = FscBeaconApiImp.getInstance(activity);
        instance.initialize();
    }

    public static FscBeaconApiImp getSdkInstance() {
        if (null == instance) {
            throw new NullPointerException("SDK not initialized.");
        }
        return instance;
    }

}
