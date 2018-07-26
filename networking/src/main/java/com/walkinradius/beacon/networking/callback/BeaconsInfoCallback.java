package com.walkinradius.beacon.networking.callback;

import com.walkinradius.beacon.networking.AndroidNetworking;
import com.walkinradius.beacon.networking.model.BeaconInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BeaconsInfoCallback implements retrofit2.Callback<List<BeaconInfo>> {

    private final AndroidNetworking.Callback mCallback;

    public BeaconsInfoCallback(AndroidNetworking.Callback callback) {
        this.mCallback = callback;
    }

    @Override
    public void onResponse(Call<List<BeaconInfo>> call, Response<List<BeaconInfo>> response) {
        if (response.isSuccessful()) {
            mCallback.onSuccess(response.body().get(0).ibeacon_model_no);
        } else {
            mCallback.onFailure(response.message());
        }
    }

    private BeaconInfo getBeacon(BeaconInfo beaconInfo) {
        return null;
    }

    @Override
    public void onFailure(Call<List<BeaconInfo>> call, Throwable t) {
        mCallback.onFailure(t.getMessage());
    }
}
