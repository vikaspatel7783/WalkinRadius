package com.walkinradius.beacon.ui.feasybeacon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.walkinradius.beacon.R;

/**
 * Copyright 2017 Shenzhen Feasycom Technology co.,Ltd
 */
public class FeasyBeaconPinDialog extends Dialog implements View.OnClickListener {


    private final Activity activity;
    EditText inputPin;
    Button btnNo;
    Button btnYes;

    private View view;

    public FeasyBeaconPinDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        initUI();
    }

    private void initUI() {
        view = getLayoutInflater().inflate(R.layout.dialog_pin_input, null,
                false);

        inputPin = (EditText) view.findViewById(R.id.editText_input_pin);
        btnNo = (Button) view.findViewById(R.id.btnPin_No);
        btnYes = (Button) view.findViewById(R.id.btnPin_Yes);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        this.addContentView(view, lp);
        setCanceledOnTouchOutside(false);

        btnNo.setOnClickListener(this);
        btnYes.setOnClickListener(this);
    }


    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnPin_Yes:
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                dismiss();

                String pin = inputPin.getText().toString();
                ((FeasyBeaconInfoActivity)activity).onPinEntered(pin);
                break;

            case R.id.btnPin_No:
                dismiss();
                activity.finish();
                break;
        }
    }
}
