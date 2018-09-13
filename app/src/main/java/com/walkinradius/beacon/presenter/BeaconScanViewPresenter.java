package com.walkinradius.beacon.presenter;

import android.os.Build;
import android.util.Log;

import com.walkinradius.beacon.adapter.RemoteBeaconsScanAdapter;
import com.walkinradius.beacon.model.BLEScannedDevice;

public class BeaconScanViewPresenter implements BeaconScanViewContract.ViewCallbacks, RemoteBeaconsScanAdapter.OnBLEDeviceScannedCallback {

    private final BeaconScanViewContract.BeaconScanView mScanView;
    private RemoteBeaconsScanAdapter remoteBeaconsScanAdapter;

    public BeaconScanViewPresenter(BeaconScanViewContract.BeaconScanView scanView) {
        this.mScanView = scanView;
    }

    @Override
    public void onActivityCreated() {
        remoteBeaconsScanAdapter = new RemoteBeaconsScanAdapter(mScanView, this);

        doBLEScan();
    }

    private void doBLEScan() {

        if (!remoteBeaconsScanAdapter.isBLESupported()) {
            mScanView.showMessage("BLE not supported by this device");
            return;
        }

        if (!remoteBeaconsScanAdapter.isBLEEnabled()) {
            mScanView.askBTEnablePermission();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!remoteBeaconsScanAdapter.isLocationPermissionGranted()) {
                mScanView.askLocationPermission();
                //mScanView.showMessage("Please enable the location");
                return;
            }
        }

        if (!remoteBeaconsScanAdapter.isLocationEnabled()) {
            mScanView.askToEnableLocation();
            mScanView.finishActivity();
            return;
        }

        mScanView.showProgressBar();

        try {
            remoteBeaconsScanAdapter.startScan();
        } catch (RemoteBeaconsScanAdapter.BLENotEnabledException e) {
            mScanView.hideProgressBar();
            Log.e(BeaconScanViewPresenter.class.getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void onActivityStopped() {
        remoteBeaconsScanAdapter.stopScan();
    }

    @Override
    public void onLocationPermissionGrant() {
        doBLEScan();
    }

    @Override
    public void onBTEnabled(boolean isBTEnabled) {
        if (isBTEnabled) {
            doBLEScan();
        } else {
            mScanView.finishActivityWithMessage("Bluetooth permission must be granted");
        }
    }

    @Override
    public void onLocationEnabled(boolean isLocationEnabled) {
        if (isLocationEnabled) {
            doBLEScan();
        } else {
            mScanView.finishActivityWithMessage("Location must be enabled.");
        }
    }

    @Override
    public void onBLEDeviceScanned(BLEScannedDevice bleScannedDevice) {
        mScanView.hideProgressBar();
        remoteBeaconsScanAdapter.stopScan();
        mScanView.handleScannedBeaconDevice(bleScannedDevice);
    }
}
