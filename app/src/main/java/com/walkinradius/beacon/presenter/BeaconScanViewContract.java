package com.walkinradius.beacon.presenter;

import com.walkinradius.beacon.model.BLEScannedDevice;

public class BeaconScanViewContract {

    public interface BeaconScanView {

        void showProgressBar();

        void hideProgressBar();

        void showMessage(String message);

        void askLocationPermission();

        void askToEnableLocation();

        void askBTEnablePermission();

        void finishActivity();

        void finishActivityWithMessage(String message);

        void handleScannedBeaconDevice(BLEScannedDevice bleScannedDevice);
    }


    public interface ViewCallbacks {

        void onActivityCreated();

        void onActivityStopped();

        void onLocationPermissionGrant();

        void onBTEnabled(boolean isBTEnabled);

        void onLocationEnabled(boolean isLocationEnabled);
    }

}
