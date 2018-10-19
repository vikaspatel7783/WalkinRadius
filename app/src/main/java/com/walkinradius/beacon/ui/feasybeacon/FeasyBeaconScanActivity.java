package com.walkinradius.beacon.ui.feasybeacon;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.walkinradius.beacon.R;
import com.walkinradius.beacon.feasybeacon.FeasyBeaconSdkCallback;
import com.walkinradius.beacon.feasybeacon.FeasyBeaconSdkFacade;
import com.walkinradius.beacon.feasybeacon.FeasyScanBeaconsListAdapter;
import com.walkinradius.beacon.ui.ParentActivity;
import com.walkinradius.beacon.ui.UiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeasyBeaconScanActivity extends ParentActivity {

    private static final int REQUEST_FINE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_COARSE_LOCATION = 3;
    private static final int REQUEST_ENABLE_LOCATION = 4;

    private static final int SCAN_TIME_WINDOW_MILLIS = 10000;

    private FeasyBeaconSdkFacade mFeasyBeaconSdkFacade;
    private Button btnScan;
    private ProgressBar pgBarLogin;
    private RecyclerView mBeaconsList;
    private ScanCallback mScanCallback;
    private FeasyScanBeaconsListAdapter mFeasyScanBeaconsListAdapter;
    private HashMap<String, BluetoothDeviceWrapper> mMapScannedFeasyBeacons;
    private List<BluetoothDeviceWrapper> mListScannedFeasyBeacons;

    private Handler mHandler = new Handler();

    private static String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feasy_beacon_scan);
        btnScan = (Button)findViewById(R.id.btnScan);

        FeasyBeaconSdkFacade.initializeSdk(this);
        mFeasyBeaconSdkFacade = FeasyBeaconSdkFacade.getInstance();
        mScanCallback = new ScanCallback(this);

        pgBarLogin = findViewById(R.id.pgBarLogin);
        mListScannedFeasyBeacons = new ArrayList<>();

        mBeaconsList = (RecyclerView) findViewById(R.id.beacons_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mBeaconsList.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mBeaconsList.setLayoutManager(mLayoutManager);

        mFeasyScanBeaconsListAdapter = new FeasyScanBeaconsListAdapter(mListScannedFeasyBeacons, new BeaconSelectCallback());
        mBeaconsList.setAdapter(mFeasyScanBeaconsListAdapter);

    }

    private class BeaconSelectCallback implements FeasyScanBeaconsListAdapter.BeaconSelectListener {

        @Override
        public void onBeaconSelected(BluetoothDeviceWrapper beaconInfo) {

            mFeasyBeaconSdkFacade.stopScan();
            mFeasyBeaconSdkFacade.setSdkCallback(null);

            Intent intent = new Intent(FeasyBeaconScanActivity.this, FeasyBeaconInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(FeasyBeaconInfoActivity.KEY_SELECTED_BEACON, beaconInfo);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPreconditionsAndStartScan();
    }

    private void checkPreconditionsAndStartScan() {
        if (!mFeasyBeaconSdkFacade.isBLEHardwarePresent()) {
            finishActivityWithMessage("BLE hardware not present on this device.");
        }

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!mFeasyBeaconSdkFacade.isFineLocationPermissionGranted(this)) {
                askFineLocationPermission();
                return;
            }
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!mFeasyBeaconSdkFacade.isCoarseLocationPermissionGranted(this)) {
                askCoarseLocationPermission();
                return;
            }
        }

        if (!isLocationEnabled(this)) {
            askToEnableLocation();
            return;
        }

        if (!mFeasyBeaconSdkFacade.isBluetoothEnabled()) {
            askBTEnablePermission();
            return;
        }

        showProgressBar();
        btnScan.setClickable(false);

        mListScannedFeasyBeacons.clear();
        mFeasyScanBeaconsListAdapter.notifyDataSetChanged();
        mFeasyBeaconSdkFacade.setSdkCallback(mScanCallback);
        mFeasyBeaconSdkFacade.startScan(SCAN_TIME_WINDOW_MILLIS);

        mHandler.postDelayed(scanTimeoutRunnable, SCAN_TIME_WINDOW_MILLIS);

        btnScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkPreconditionsAndStartScan();
            }
        });
    }

    Runnable scanTimeoutRunnable = new Runnable() {
        @Override
        public void run() {
            mFeasyBeaconSdkFacade.stopScan();
            btnScan.setClickable(true);
            FeasyBeaconScanActivity.this.hideProgressBar();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(scanTimeoutRunnable);
        mFeasyBeaconSdkFacade.setSdkCallback(null);
        mFeasyBeaconSdkFacade.stopScan();
    }

    // only for Kitkat and onwards
    public boolean isLocationEnabled(Context context) {
        int locationMode = 0;

        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkPreconditionsAndStartScan();
    }

    private static class ScanCallback extends FeasyBeaconSdkCallback {

        private final FeasyBeaconScanActivity mFeasyBeaconScanActivity;

        public ScanCallback(FeasyBeaconScanActivity activity) {
            mFeasyBeaconScanActivity = activity;
        }

        @Override
        public void blePeripheralFound(BluetoothDeviceWrapper remoteDevice, int rssi, byte[] record) {

            // supporting on gBeacon (Eddystone)
            if (null == remoteDevice.getgBeacon()) {
                return;
            }

            boolean found = false;

            // Iterate though the list to check same remoteDevice presence.
            for (BluetoothDeviceWrapper storedDevice :
                    mFeasyBeaconScanActivity.mListScannedFeasyBeacons) {

                // supporting on gBeacon (Eddystone)
                if (storedDevice.getgBeacon().getUrl().equals(remoteDevice.getgBeacon().getUrl())) {
                    int indexOfExistingDevice = mFeasyBeaconScanActivity.mListScannedFeasyBeacons.indexOf(storedDevice);
                    mFeasyBeaconScanActivity.mListScannedFeasyBeacons.set(indexOfExistingDevice, remoteDevice);
                    found = true;
                    break; // No need to iterate anymore.
                }

                // Same remoteDevice already present in list then update with new remoteDevice info
            }

            if (!found) {
                // Add new remoteDevice to list
                mFeasyBeaconScanActivity.mListScannedFeasyBeacons.add(remoteDevice);
            }

            // notify adapter to refresh list
            mFeasyBeaconScanActivity.mFeasyScanBeaconsListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void showProgressBar() {
        pgBarLogin.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        pgBarLogin.setVisibility(View.GONE);
    }

    private void showMessage(String message) {

        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        UiUtils.getAlertDialog(this, "BEACONS SCAN", message, clickListener).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void askFineLocationPermission() {
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_FINE_LOCATION);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void askCoarseLocationPermission() {
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_COARSE_LOCATION);
    }

    public void askToEnableLocation() {
        Intent intent= new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_ENABLE_LOCATION);
    }

    public void askBTEnablePermission() {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    public void finishActivityWithMessage(String message) {
        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        UiUtils.getAlertDialog(this, "BEACONS SCAN", message, clickListener).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            finish();
        }
    }


}
