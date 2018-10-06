package com.walkinradius.beacon.feasybeacon;

import com.feasycom.bean.BluetoothDeviceWrapper;
import com.feasycom.controler.FscBeaconCallbacksImp;

public class FeasyBeaconSdkCallback extends FscBeaconCallbacksImp {

    @Override
    public void blePeripheralFound(BluetoothDeviceWrapper device, int rssi, byte[] record) {

    }

}
