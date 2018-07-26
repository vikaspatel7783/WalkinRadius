package com.walkinradius.beacon.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.walkinradius.beacon.R;
import com.walkinradius.beacon.presenter.DashboardViewContract;
import com.walkinradius.beacon.presenter.DashboardViewPresenter;

public class DashboardActivity extends Activity implements DashboardViewContract.DashboardView {

    private DashboardViewPresenter mDashboardViewPresenter;

    private ProgressBar pgBarLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacons_subscriber);

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
        ((TextView)findViewById(R.id.beacons_info)).setText(message);
    }
}
