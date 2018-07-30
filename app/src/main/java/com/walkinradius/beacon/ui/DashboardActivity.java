package com.walkinradius.beacon.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.walkinradius.beacon.R;
import com.walkinradius.beacon.adapter.BeaconsListAdapter;
import com.walkinradius.beacon.networking.model.BeaconInfo;
import com.walkinradius.beacon.presenter.DashboardViewContract;
import com.walkinradius.beacon.presenter.DashboardViewPresenter;

import java.util.List;

public class DashboardActivity extends Activity implements DashboardViewContract.DashboardView {

    private ProgressBar pgBarLogin;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacons_list);

        pgBarLogin = findViewById(R.id.pgBarLogin);

        DashboardViewPresenter mDashboardViewPresenter = new DashboardViewPresenter(this);
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
        //((TextView)findViewById(R.id.beacons_info)).setText(message);
    }

    @Override
    public void handleBeaconsList(List<BeaconInfo> beaconsList) {
        mRecyclerView = (RecyclerView) findViewById(R.id.beacons_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        BeaconsListAdapter beaconsListAdapter = new BeaconsListAdapter(beaconsList);
        mRecyclerView.setAdapter(beaconsListAdapter);
    }
}
