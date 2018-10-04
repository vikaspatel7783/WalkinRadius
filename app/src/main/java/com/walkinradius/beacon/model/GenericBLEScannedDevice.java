package com.walkinradius.beacon.model;

public class GenericBLEScannedDevice<T> {

    private T t;
    private int rssi;
    private byte[] scanRecord;

    public T getDevice() {
        return t;
    }

    public void setDevice(T t) {
        this.t = t;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public byte[] getScanRecord() {
        return scanRecord;
    }

    public void setScanRecord(byte[] scanRecord) {
        this.scanRecord = scanRecord;
    }

}
