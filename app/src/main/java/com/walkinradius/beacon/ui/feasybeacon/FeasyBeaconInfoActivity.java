package com.walkinradius.beacon.ui.feasybeacon;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.bean.FeasyBeacon;
import com.walkinradius.beacon.R;
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

        setContentView(R.layout.activity_feasy_beacon_info);

        mSelectedBeacon = (BluetoothDeviceWrapper) getIntent().getSerializableExtra(KEY_SELECTED_BEACON);

        if (null != mSelectedBeacon.getFeasyBeacon() &&
                FeasyBeacon.BLE_KEY_WAY.equals(mSelectedBeacon.getFeasyBeacon().getEncryptionWay())) {

            FeasyBeaconPinDialog feasyBeaconPinDialog = new FeasyBeaconPinDialog(this);
            feasyBeaconPinDialog.show();
        }

        mFeasyBeaconSdkFacade = FeasyBeaconSdkFacade.getInstance();
        mFeasyBeaconSdkFacade.setSdkCallback(new SdkCallback());
    }

    public void onPinEntered(String pin) {

        if (TextUtils.isEmpty(pin)) {
            showMessageAndFinishActivity("Input PIN should not be empty");
            return;
        }
        mPin = pin;

        showProgressbar(R.string.label_beacon_connect_default);
    }

    private void showProgressbar(int messageId) {
        setProgressLayoutVisibility(true);

        TextView textViewPgBar = findViewById(R.id.textView_Pgbar_BeaconInfo);
        textViewPgBar.setText(getString(messageId));
    }

    private void hidProgressbar() {
        setProgressLayoutVisibility(false);
    }

    private void setProgressLayoutVisibility(boolean visible) {
        View layoutProgressbar = findViewById(R.id.layout_beaconinfo_pgbar);
        layoutProgressbar.setVisibility(visible ? View.VISIBLE : View.GONE);
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
