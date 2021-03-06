package com.walkinradius.beacon.adapter;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.walkinradius.beacon.model.BLEScannedDevice;
import com.walkinradius.beacon.presenter.BeaconScanViewContract;

public class RemoteBeaconsScanAdapter implements BluetoothAdapter.LeScanCallback {

    private final Context mContext;
    private final OnBLEDeviceScannedCallback mOnBLEDeviceScannedCallback;
    private BluetoothAdapter mBTAdapter;

    private boolean isScanning = false;

    public RemoteBeaconsScanAdapter(BeaconScanViewContract.BeaconScanView context, OnBLEDeviceScannedCallback onBLEDeviceScannedCallback) {
        this.mContext = (Activity)context;
        this.mOnBLEDeviceScannedCallback = onBLEDeviceScannedCallback;

        if (isBLESupported()) {
            initBTAdapter();
        }
    }

    public boolean isBLESupported() {
        return mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }

    public boolean isBLEEnabled() {
        return (mBTAdapter != null) && mBTAdapter.isEnabled();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean isLocationPermissionGranted() {
        return  (mContext.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    // only for Kitkat and onwards
    public boolean isLocationEnabled() {
        int locationMode = 0;

        try {
            locationMode = Settings.Secure.getInt(mContext.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    private void initBTAdapter() {
        BluetoothManager manager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        if (manager != null) {
            mBTAdapter = manager.getAdapter();
        }
    }

    public void startScan() throws BLENotEnabledException {
        if (!isScanning) {

            if (!isBLEEnabled()) {
                throw new BLENotEnabledException("BLE is not enabled on device.");
            }

            mBTAdapter.startLeScan(this);
            isScanning = true;
            Log.d(RemoteBeaconsScanAdapter.class.getSimpleName(), "BLE Scanning started...");
        }
    }

    public void stopScan() {
        if (isScanning) {
            mBTAdapter.stopLeScan(this);
            mBTAdapter = null;
            isScanning = false;
            Log.d(RemoteBeaconsScanAdapter.class.getSimpleName(), "BLE Scanning stopped.");
        }
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        BLEScannedDevice bleScannedDevice = new BLEScannedDevice();
        bleScannedDevice.setDevice(device);
        bleScannedDevice.setRssi(rssi);
        bleScannedDevice.setScanRecord(scanRecord);

        mOnBLEDeviceScannedCallback.onBLEDeviceScanned(bleScannedDevice);
    }

    public interface OnBLEDeviceScannedCallback {

        void onBLEDeviceScanned(BLEScannedDevice bleScannedDevice);

    }

    public static class BLENotEnabledException extends Throwable {

        BLENotEnabledException(String s) {
            super(s);
        }
    }
}
