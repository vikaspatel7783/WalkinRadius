package com.walkinradius.beacon.ui.feasybeacon;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.bean.CommandBean;
import com.feasycom.bean.FeasyBeacon;
import com.walkinradius.beacon.R;
import com.walkinradius.beacon.feasybeacon.FeasyBeaconSdkCallback;
import com.walkinradius.beacon.feasybeacon.FeasyBeaconSdkFacade;
import com.walkinradius.beacon.ui.ParentActivity;
import com.walkinradius.beacon.ui.UiUtils;

public class FeasyBeaconInfoActivity extends ParentActivity {

    public static final String KEY_SELECTED_BEACON = "SELECTED_BEACON";
    private static final int CONNECT_TIME = 12000; // 12 sec

    private String mPin;
    private FeasyBeaconSdkFacade mFeasyBeaconSdkFacade;
    private BluetoothDeviceWrapper mSelectedBeacon;
    private Handler mHandler = new Handler();

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
        mFeasyBeaconSdkFacade.setSdkCallback(new SdkCallback(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(checkConnect);
        mFeasyBeaconSdkFacade.setSdkCallback(null);
        mFeasyBeaconSdkFacade.disconnect();
    }

    public void onPinEntered(String pin) {

        if (TextUtils.isEmpty(pin)) {
            showMessageAndFinishActivity("Input PIN should not be empty");
            return;
        }
        mPin = pin;

        mHandler.postDelayed(checkConnect, CONNECT_TIME);
        mFeasyBeaconSdkFacade.connectToBeacon(mSelectedBeacon, pin);

        showProgressbar(R.string.label_beacon_connect_default);
    }

    Runnable checkConnect = new Runnable() {
        @Override
        public void run() {
            if (!mFeasyBeaconSdkFacade.isConnected()) {
                mFeasyBeaconSdkFacade.setSdkCallback(null);
                mFeasyBeaconSdkFacade.disconnect();
                showMessageAndFinishActivity("Timed-out while connecting.");
            }
        }
    };

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

        private final FeasyBeaconInfoActivity mFeasyBeaconInfoActivity;

        public SdkCallback(FeasyBeaconInfoActivity activity) {
            mFeasyBeaconInfoActivity = activity;
        }

        @Override
        public void blePeripheralConnected(BluetoothGatt gatt, BluetoothDevice device) {
            mFeasyBeaconInfoActivity.showProgressbar(R.string.label_beacon_connect_successful);
        }

        @Override
        public void connectProgressUpdate(BluetoothDevice device, int status) {

            switch (status) {
                case CommandBean.PASSWORD_CHECK:
                    mFeasyBeaconInfoActivity.showProgressbar(R.string.label_beacon_connect_password_check);
                    break;

                case CommandBean.PASSWORD_SUCCESSFULE:
                    mFeasyBeaconInfoActivity.showProgressbar(R.string.label_beacon_connect_password_success);
                    break;

                case CommandBean.PASSWORD_FAILED:
                    mFeasyBeaconInfoActivity.showProgressbar(R.string.label_beacon_connect_password_failed);
                    break;

                case CommandBean.PASSWORD_TIME_OUT:
                    mFeasyBeaconInfoActivity.showProgressbar(R.string.label_beacon_connect_time_out);
                    break;
            }

        }

    }
}
