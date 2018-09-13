package com.walkinradius.beacon.presenter;

import android.os.Build;
import android.util.Log;

import com.walkinradius.beacon.adapter.BeaconsScanAdapter;
import com.walkinradius.beacon.model.BLEScannedDevice;

public class BeaconScanViewPresenter implements BeaconScanViewContract.ViewCallbacks, BeaconsScanAdapter.OnBLEDeviceScannedCallback {

    private final BeaconScanViewContract.BeaconScanView mScanView;
    private BeaconsScanAdapter beaconsScanAdapter;

    public BeaconScanViewPresenter(BeaconScanViewContract.BeaconScanView scanView) {
        this.mScanView = scanView;
    }

    @Override
    public void onActivityCreated() {
        beaconsScanAdapter = new BeaconsScanAdapter(mScanView, this);

        doBLEScan();
    }

    private void doBLEScan() {

        if (!beaconsScanAdapter.isBLESupported()) {
            mScanView.showMessage("BLE not supported by this device");
            return;
        }

        if (!beaconsScanAdapter.isBLEEnabled()) {
            mScanView.askBTEnablePermission();
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!beaconsScanAdapter.isLocationPermissionGranted()) {
                mScanView.askLocationPermission();
                //mScanView.showMessage("Please enable the location");
                return;
            }
        }

        if (!beaconsScanAdapter.isLocationEnabled()) {
            mScanView.askToEnableLocation();
            mScanView.finishActivity();
            return;
        }

        mScanView.showProgressBar();

        try {
            beaconsScanAdapter.startScan();
        } catch (BeaconsScanAdapter.BLENotEnabledException e) {
            mScanView.hideProgressBar();
            Log.e(BeaconScanViewPresenter.class.getSimpleName(), e.getMessage());
        }
    }

    @Override
    public void onActivityStopped() {
        beaconsScanAdapter.stopScan();
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
        beaconsScanAdapter.stopScan();
        mScanView.handleScannedBeaconDevice(bleScannedDevice);
    }
}
