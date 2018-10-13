package com.walkinradius.beacon.feasybeacon;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.controler.FscBeaconApiImp;
import com.feasycom.controler.FscBeaconCallbacks;

public class FeasyBeaconSdkFacade {

    private static FeasyBeaconSdkFacade wrapperInstance;
    private static FscBeaconApiImp fscBeaconApi;

    public static void initializeSdk(Activity activity) {
        fscBeaconApi = FscBeaconApiImp.getInstance(activity);
        fscBeaconApi.initialize();
    }

    public static FeasyBeaconSdkFacade getInstance() {
        if (null == fscBeaconApi) {
            throw new NullPointerException("SDK not initialized.");
        }
        if (null == wrapperInstance) {
            wrapperInstance = new FeasyBeaconSdkFacade();
        }
        return wrapperInstance;
    }

    public boolean isBLEHardwarePresent() {
        return fscBeaconApi.checkBleHardwareAvailable();
    }

    public boolean isFineLocationPermissionGranted(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED;
    }

    public boolean isCoarseLocationPermissionGranted(Context context) {
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                android.content.pm.PackageManager.PERMISSION_GRANTED;
    }

    public boolean isBluetoothEnabled() {
        return fscBeaconApi.isBtEnabled();
    }

    public void setSdkCallback(FscBeaconCallbacks fscBeaconCallbacks) {
        fscBeaconApi.setCallbacks(fscBeaconCallbacks);
    }

    public void startScan(int millis) {
        fscBeaconApi.startScan(millis);
    }

    public void stopScan() {
        fscBeaconApi.stopScan();
    }

    public boolean isConnected() {
        return fscBeaconApi.isConnected();
    }

    public void disconnect() {
        if (fscBeaconApi.isConnected()) {
            fscBeaconApi.disconnect();
        }
    }

    public void connectToBeacon(BluetoothDeviceWrapper mSelectedBeacon, String pin) {
        fscBeaconApi.connect(mSelectedBeacon, pin);
    }

    public void setConnect(boolean isBeaconConnectable) {
        fscBeaconApi.setConnectable(isBeaconConnectable);
    }

    interface ScanCompletedCallback {
        void onScanCompleted();
    }
}
