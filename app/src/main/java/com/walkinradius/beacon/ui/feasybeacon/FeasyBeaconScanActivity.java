package com.walkinradius.beacon.ui.feasybeacon;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.walkinradius.beacon.R;
import com.walkinradius.beacon.feasybeacon.FeasyBeaconSdkCallback;
import com.walkinradius.beacon.feasybeacon.FeasyBeaconSdkFacade;
import com.walkinradius.beacon.presenter.BeaconScanViewPresenter;
import com.walkinradius.beacon.ui.ParentActivity;
import com.walkinradius.beacon.ui.UiUtils;

public class FeasyBeaconScanActivity extends ParentActivity {

    private ProgressBar pgBarLogin;
    private BeaconScanViewPresenter mBeaconScanViewPresenter;

    public static final String KEY_BEACON_UUID = "BeaconUUID";
    public static final String KEY_BEACON_MODEL = "BeaconModel";
    public static final String KEY_BEACON_TEMP_NAME = "BeaconTempName";
    public static final String KEY_BEACON_TEMP_LINK = "BeaconTempLink";
    public static final String KEY_BEACON_LOCATION = "BeaconLocation";
    public static final String KEY_BEACON_STATUS = "BeaconStatus";

    private static final int REQUEST_FINE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_COARSE_LOCATION = 3;
    private static final int REQUEST_ENABLE_LOCATION = 4;

    private String mUUID;
    private int mMajorValue;
    private int mMinorValue;

    private FeasyBeaconSdkFacade mFeasyBeaconSdkFacade;
    private static final int PERMISSIONS_REQUEST_CODE = 1;
    private static final int ENABLE_BT_REQUEST_ID = 2;

    private static String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.BLUETOOTH_PRIVILEGED
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan);

        FeasyBeaconSdkFacade.initializeSdk(this);
        mFeasyBeaconSdkFacade = FeasyBeaconSdkFacade.getInstance();
        mFeasyBeaconSdkFacade.setSdkCallback(new ScanCallback(this));

        pgBarLogin = findViewById(R.id.pgBarLogin);
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

        //showProgressBar();

        mFeasyBeaconSdkFacade.startScan();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        public void blePeripheralFound(BluetoothDeviceWrapper device, int rssi, byte[] record) {
            mFeasyBeaconScanActivity.parseScannedData(device);
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

    public void finishActivity() {
        finish();
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

    private void parseScannedData(BluetoothDeviceWrapper bleScannedDevice) {

        //setMajorMinorUUIDValues();

        // Device name
        TextView name = (TextView) findViewById(R.id.device_name);
        name.setText("Name: "+bleScannedDevice.getName());

        // MAC address
        TextView address = (TextView) findViewById(R.id.device_address);
        address.setText("MAC: " + bleScannedDevice.getAddress());

       /* // UUID
        TextView uuid = (TextView) findViewById(R.id.device_uuid);
        uuid.setText("UUID: ");

        // Major
        TextView major = (TextView) findViewById(R.id.device_major);
        major.setText("Major: "+mMajorValue);

        // Minor
        TextView minor = (TextView) findViewById(R.id.device_minor);
        minor.setText("Minor: "+mMinorValue);*/


        TextView rssi = (TextView) findViewById(R.id.device_rssi);
        rssi.setText("RSSI: " + bleScannedDevice.getRssi());
    }

    private void setMajorMinorUUIDValues(byte[] scanRecord) {
        int startByte = 2;
        boolean patternFound = false;
        while (startByte <= 5) {
            if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an iBeacon
                    ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                patternFound = true;
                break;
            }
            startByte++;
        }

        if (patternFound) {
            //Convert to hex String
            byte[] uuidBytes = new byte[16];
            System.arraycopy(scanRecord, startByte+4, uuidBytes, 0, 16);
            String hexString = bytesToHex(uuidBytes);

            //Here is your UUID
            mUUID =  hexString.substring(0,8) + "-" +
                    hexString.substring(8,12) + "-" +
                    hexString.substring(12,16) + "-" +
                    hexString.substring(16,20) + "-" +
                    hexString.substring(20,32);

            //Here is your Major value
            mMajorValue = (scanRecord[startByte+20] & 0xff) * 0x100 + (scanRecord[startByte+21] & 0xff);

            //Here is your Minor value
            mMinorValue = (scanRecord[startByte+22] & 0xff) * 0x100 + (scanRecord[startByte+23] & 0xff);

        }
    }

    /**
     * bytesToHex method
     * Found on the internet
     * http://stackoverflow.com/a/9855338
     */
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}
