package com.walkinradius.beacon.presenter;

public class DashboardViewContract {

    public interface DashboardView {

        void showProgressBar();

        void hideProgressBar();

        void showMessage(String message);

        void handleBeaconsList(String beaconsListJson);
    }

    public interface DashboardViewCallbacks {

        void onActivityCreated();

    }

}
