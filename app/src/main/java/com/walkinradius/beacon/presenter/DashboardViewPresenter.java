package com.walkinradius.beacon.presenter;

import com.walkinradius.beacon.networking.model.BeaconInfo;
import com.walkinradius.beacon.util.UserNameAndStatusHolder;
import com.walkinradius.beacon.networking.AndroidNetworking;
import com.walkinradius.beacon.networking.retrofit.RetrofitNetworking;

public class DashboardViewPresenter implements DashboardViewContract.DashboardViewCallbacks {

    private final DashboardViewContract.DashboardView mDashboardView;

    private AndroidNetworking mAndroidNetworking = new RetrofitNetworking();

    public DashboardViewPresenter(DashboardViewContract.DashboardView dashboardView) {
        this.mDashboardView = dashboardView;
    }

    @Override
    public void onActivityCreated() {
        mDashboardView.showProgressBar();
        UserNameAndStatusHolder instance = UserNameAndStatusHolder.getInstance();
        mAndroidNetworking.getBeaconsInfo(callback, instance.getUserName(), instance.getStatus());
    }

    @Override
    public void onBeaconSelected(BeaconInfo beaconInfo) {
        mDashboardView.showNextActivity(beaconInfo);
    }

    private AndroidNetworking.Callback callback = new AndroidNetworking.Callback() {

        @Override
        public void onSuccess(String beaconsList) {
            mDashboardView.hideProgressBar();
            mDashboardView.handleBeaconsList(beaconsList);
        }

        @Override
        public void onFailure(String message) {
            mDashboardView.hideProgressBar();
            mDashboardView.showMessage(message);
        }
    };
}
