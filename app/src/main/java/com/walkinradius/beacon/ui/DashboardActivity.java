package com.walkinradius.beacon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.walkinradius.beacon.R;
import com.walkinradius.beacon.adapter.AccountBeaconsListAdapter;
import com.walkinradius.beacon.networking.model.BeaconInfo;
import com.walkinradius.beacon.presenter.DashboardViewContract;
import com.walkinradius.beacon.presenter.DashboardViewPresenter;

import java.util.List;

public class DashboardActivity extends ParentActivity implements DashboardViewContract.DashboardView, AccountBeaconsListAdapter.BeaconSelectListener {

    private ProgressBar pgBarLogin;
    private RecyclerView mRecyclerView;
    private DashboardViewPresenter mDashboardViewPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacons_list);

        pgBarLogin = findViewById(R.id.pgBarLogin);

        mDashboardViewPresenter = new DashboardViewPresenter(this);
        mDashboardViewPresenter.onActivityCreated();
    }

    @Override
    public void showProgressBar() {
        pgBarLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        pgBarLogin.setVisibility(View.GONE);
    }

    @Override
    public void showMessage(String message) {
        UiUtils.getAlertDialog(this, "NO BEACONS", message, null).show();
    }

    @Override
    public void handleBeaconsList(String beaconsListJson) {
        List<BeaconInfo> beaconsList = new Gson().fromJson(beaconsListJson, new TypeToken<List<BeaconInfo>>(){}.getType());
        /*addMockBeaconInfoToList(beaconsList);
        addMockBeaconInfoToList(beaconsList);
        addMockBeaconInfoToList(beaconsList);*/
        handleBeaconsList(beaconsList);
    }

    @Override
    public void showNextActivity(BeaconInfo beaconInfo) {
        Intent intentScanActivity = new Intent(this, BeaconScanActivity.class);

        intentScanActivity.putExtra(BeaconScanActivity.KEY_BEACON_TEMP_NAME, beaconInfo.temp_name);
        intentScanActivity.putExtra(BeaconScanActivity.KEY_BEACON_LOCATION, beaconInfo.location);
        intentScanActivity.putExtra(BeaconScanActivity.KEY_BEACON_MODEL, beaconInfo.ibeacon_model_no);
        intentScanActivity.putExtra(BeaconScanActivity.KEY_BEACON_STATUS, beaconInfo.status);
        intentScanActivity.putExtra(BeaconScanActivity.KEY_BEACON_TEMP_LINK, beaconInfo.temp_link);
        intentScanActivity.putExtra(BeaconScanActivity.KEY_BEACON_UUID, beaconInfo.uuid_no);

        startActivity(intentScanActivity);
    }

    private void addMockBeaconInfoToList(List<BeaconInfo> beaconsList) {
        BeaconInfo beaconInfo = new BeaconInfo();
        beaconInfo.uuid_no = "U333";
        beaconInfo.temp_link = "http://www.google.com";
        beaconInfo.status = "Active";
        beaconInfo.location = "North side";
        beaconInfo.ibeacon_model_no = "Win4000R";
        beaconInfo.temp_name = "Beacon temp name";

        beaconsList.add(beaconInfo);
    }

    public void handleBeaconsList(List<BeaconInfo> beaconsList) {

        if (null == beaconsList || beaconsList.size() == 0) {
            showMessage("No beacons received.");
            return;
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.beacons_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        AccountBeaconsListAdapter accountBeaconsListAdapter = new AccountBeaconsListAdapter(beaconsList, this);
        mRecyclerView.setAdapter(accountBeaconsListAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.HORIZONTAL));

    }

    @Override
    public void onBeaconSelected(BeaconInfo beaconInfo) {
        mDashboardViewPresenter.onBeaconSelected(beaconInfo);
    }
}
