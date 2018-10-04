package com.walkinradius.beacon.feasybeacon;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.controler.FscBeaconCallbacksImp;

public class FeasyBeaconSdkCallback extends FscBeaconCallbacksImp {

    private final BeaconScanCallback mBeaconScanCallback;

    public FeasyBeaconSdkCallback(BeaconScanCallback beaconScanCallback) {
        mBeaconScanCallback = beaconScanCallback;
    }

    @Override
    public void blePeripheralFound(BluetoothDeviceWrapper device, int rssi, byte[] record) {
        mBeaconScanCallback.onBeaconScan(device, rssi, record);
    }

    public interface BeaconScanCallback {
        void onBeaconScan(BluetoothDeviceWrapper device, int rssi, byte[] record);
    }
}
