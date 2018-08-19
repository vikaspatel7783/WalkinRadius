package com.walkinradius.beacon.presenter;

import com.walkinradius.beacon.networking.model.BeaconInfo;

public class DashboardViewContract {

    public interface DashboardView {

        void showProgressBar();

        void hideProgressBar();

        void showMessage(String message);

        void handleBeaconsList(String beaconsListJson);

        void showNextActivity(BeaconInfo beaconInfo);
    }

    public interface DashboardViewCallbacks {

        void onActivityCreated();

        void onBeaconSelected(BeaconInfo beaconInfo);

    }

}
