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

    private String mPin;
    private FeasyBeaconSdkFacade mFeasyBeaconSdkFacade;
    private BluetoothDeviceWrapper mSelectedBeacon;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feasy_beacon_info);
        mHandler = new Handler();

        mSelectedBeacon = (BluetoothDeviceWrapper) getIntent().getSerializableExtra(KEY_SELECTED_BEACON);

        if (null != mSelectedBeacon.getFeasyBeacon() &&
                FeasyBeacon.BLE_KEY_WAY.equals(mSelectedBeacon.getFeasyBeacon().getEncryptionWay())) {

            FeasyBeaconPinDialog feasyBeaconPinDialog = new FeasyBeaconPinDialog(this);
            feasyBeaconPinDialog.show();
        }

        FeasyBeaconSdkFacade.initializeSdk(this);
        mFeasyBeaconSdkFacade = FeasyBeaconSdkFacade.getInstance();
        mFeasyBeaconSdkFacade.setSdkCallback(new SdkCallback(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFeasyBeaconSdkFacade.setSdkCallback(null);
        mFeasyBeaconSdkFacade.disconnect();
    }

    public void onPinEntered(String pin) {

        if (TextUtils.isEmpty(pin)) {
            showMessageAndFinishActivity("Input PIN should not be empty");
            return;
        }
        mPin = pin;

        mFeasyBeaconSdkFacade.connectToBeacon(mSelectedBeacon, pin);

        showProgressbar(R.string.label_beacon_connect_default);
    }

    private void showProgressbar(int messageId) {
        setProgressLayoutVisibility(true);

        TextView textViewPgBar = findViewById(R.id.textView_Pgbar_BeaconInfo);
        textViewPgBar.setText(getString(messageId));
    }

    private void hideProgressbar() {
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
        hideProgressbar();
    }

    private static class SdkCallback extends FeasyBeaconSdkCallback {

        private final FeasyBeaconInfoActivity mFeasyBeaconInfoActivity;

        public SdkCallback(FeasyBeaconInfoActivity activity) {
            mFeasyBeaconInfoActivity = activity;
        }

        @Override
        public void blePeripheralConnected(BluetoothGatt gatt, BluetoothDevice device) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    mFeasyBeaconInfoActivity.showProgressbar(R.string.label_beacon_connect_successful);
                    //TODO: get the beacon information
                }
            };
            postOnUI(runnable);
        }

        @Override
        public void connectProgressUpdate(BluetoothDevice device, int status) {

            Runnable runnable;

            switch (status) {
                case CommandBean.PASSWORD_CHECK:
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            mFeasyBeaconInfoActivity.showProgressbar(R.string.label_beacon_connect_password_check);
                        }
                    };
                    postOnUI(runnable);
                    break;

                case CommandBean.PASSWORD_SUCCESSFULE:
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            mFeasyBeaconInfoActivity.showProgressbar(R.string.label_beacon_connect_password_success);
                            //TODO: get the beacon information
                        }
                    };
                    postOnUI(runnable);
                    break;

                case CommandBean.PASSWORD_FAILED:
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            mFeasyBeaconInfoActivity.hideProgressbar();
                            mFeasyBeaconInfoActivity.showMessageAndFinishActivity(
                                    mFeasyBeaconInfoActivity.getString(R.string.label_beacon_connect_password_failed));
                        }
                    };
                    postOnUI(runnable);
                    break;

                case CommandBean.PASSWORD_TIME_OUT:
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            mFeasyBeaconInfoActivity.hideProgressbar();
                            mFeasyBeaconInfoActivity.showMessageAndFinishActivity("Timed-Out. \nCould not connect to Beacon.");
                        }
                    };
                    postOnUI(runnable);
                    break;
            }
        }

        private void postOnUI(Runnable runnable) {
            mFeasyBeaconInfoActivity.mHandler.post(runnable);
        }

    }
}
