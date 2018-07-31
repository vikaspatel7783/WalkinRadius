package com.walkinradius.beacon.ui;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.walkinradius.beacon.R;

public class ParentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.actionbar)));
            actionBar.setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        playInAnimation();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        playOutAnimation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void playInAnimation() {
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void playOutAnimation(){
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

}
