package com.walkinradius.beacon.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.walkinradius.beacon.R;
import com.walkinradius.beacon.model.BLEScannedDevice;
import com.walkinradius.beacon.presenter.BeaconScanViewContract;
import com.walkinradius.beacon.presenter.BeaconScanViewPresenter;

public class BeaconScanActivity extends ParentActivity implements BeaconScanViewContract.BeaconScanView {

    private ProgressBar pgBarLogin;
    private BeaconScanViewPresenter mBeaconScanViewPresenter;

    public static final String KEY_BEACON_UUID = "BeaconUUID";
    public static final String KEY_BEACON_MODEL = "BeaconModel";
    public static final String KEY_BEACON_TEMP_NAME = "BeaconTempName";
    public static final String KEY_BEACON_TEMP_LINK = "BeaconTempLink";
    public static final String KEY_BEACON_LOCATION = "BeaconLocation";
    public static final String KEY_BEACON_STATUS = "BeaconStatus";

    private static final int REQUEST_COARSE_LOCATION = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private String mUUID;
    private int mMajorValue;
    private int mMinorValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_scan);

        pgBarLogin = findViewById(R.id.pgBarLogin);

        mBeaconScanViewPresenter = new BeaconScanViewPresenter(this);
        mBeaconScanViewPresenter.onActivityCreated();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mBeaconScanViewPresenter.onActivityStopped();
    }

    @Override
    public void showProgressBar() {
        pgBarLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        pgBarLogin.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {

        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        UiUtils.getAlertDialog(this, "BEACONS SCAN", message, clickListener).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void askLocationPermission() {
        this.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_COARSE_LOCATION);
    }

    @Override
    public void askBTEnablePermission() {
        Intent enableBtIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }

    @Override
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
    public void handleScannedBeaconDevice(BLEScannedDevice bleScannedDevice) {
        parseScannedData(bleScannedDevice);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                mBeaconScanViewPresenter.onBTEnabled(resultCode == RESULT_OK);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mBeaconScanViewPresenter.onLocationPermissionGrant();
                } else {
                    finish();
                }

                break;
        }
    }

    private void parseScannedData(BLEScannedDevice bleScannedDevice) {

        setMajorMinorUUIDValues(bleScannedDevice.getScanRecord());

        // Device name
        TextView name = (TextView) findViewById(R.id.device_name);
        name.setText("Name: "+bleScannedDevice.getDevice().getName());

        // MAC address
        TextView address = (TextView) findViewById(R.id.device_address);
        address.setText("MAC: " + bleScannedDevice.getDevice().getAddress());

        // UUID
        TextView uuid = (TextView) findViewById(R.id.device_uuid);
        uuid.setText("UUID: "+mUUID);

        // Major
        TextView major = (TextView) findViewById(R.id.device_major);
        major.setText("Major: "+mMajorValue);

        // Minor
        TextView minor = (TextView) findViewById(R.id.device_minor);
        minor.setText("Minor: "+mMinorValue);


        TextView rssi = (TextView) findViewById(R.id.device_rssi);
        rssi.setText("RSSI: " + Integer.toString(bleScannedDevice.getRssi()));
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
