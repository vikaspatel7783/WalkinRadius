package com.walkinradius.beacon.ui.feasybeacon;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.bean.FeasyBeacon;
import com.walkinradius.beacon.feasybeacon.FeasyBeaconSdkCallback;
import com.walkinradius.beacon.feasybeacon.FeasyBeaconSdkFacade;
import com.walkinradius.beacon.ui.ParentActivity;
import com.walkinradius.beacon.ui.UiUtils;

public class FeasyBeaconInfoActivity extends ParentActivity {

    public static final String KEY_SELECTED_BEACON = "SELECTED_BEACON";

    private String mPin;
    private FeasyBeaconSdkFacade mFeasyBeaconSdkFacade;
    private BluetoothDeviceWrapper mSelectedBeacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSelectedBeacon = (BluetoothDeviceWrapper) getIntent().getSerializableExtra(KEY_SELECTED_BEACON);

        if (null != mSelectedBeacon.getFeasyBeacon() &&
                FeasyBeacon.BLE_KEY_WAY.equals(mSelectedBeacon.getFeasyBeacon().getEncryptionWay())) {

            FeasyBeaconPinDialog feasyBeaconPinDialog = new FeasyBeaconPinDialog(this);
            feasyBeaconPinDialog.show();
        }
    }

    public void onPinEntered(String pin) {

        if (TextUtils.isEmpty(pin)) {
            showMessageAndFinishActivity("Input PIN should not be empty");
            return;
        }
        mPin = pin;
        mFeasyBeaconSdkFacade = FeasyBeaconSdkFacade.getInstance();
        mFeasyBeaconSdkFacade.setSdkCallback(new SdkCallback());
    }

    public void showMessageAndFinishActivity(String message) {

        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        UiUtils.getAlertDialog(this, "BEACON INPUT PIN", message, clickListener).show();
    }

    private static class SdkCallback extends FeasyBeaconSdkCallback {


    }
}
