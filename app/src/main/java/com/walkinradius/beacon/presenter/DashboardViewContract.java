package com.walkinradius.beacon.presenter;

import com.walkinradius.beacon.networking.model.BeaconInfo;

import java.util.List;

public class DashboardViewContract {

    public interface DashboardView {

        void showProgressBar();

        void hideProgressBar();

        void showMessage(String message);

        void handleBeaconsList(List<BeaconInfo> beaconsList);
    }

    public interface DashboardViewCallbacks {

        void onActivityCreated();

    }

}
