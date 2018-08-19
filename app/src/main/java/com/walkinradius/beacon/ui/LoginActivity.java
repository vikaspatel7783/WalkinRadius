package com.walkinradius.beacon.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.walkinradius.beacon.R;
import com.walkinradius.beacon.presenter.LoginViewContract;
import com.walkinradius.beacon.presenter.LoginViewPresenter;

public class LoginActivity extends ParentActivity implements View.OnClickListener, LoginViewContract.LoginView {

    private EditText edtTextPassword;
    private EditText edtTextUserName;
    private ProgressBar pgBarLogin;
    private LoginViewPresenter loginViewPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtTextUserName = findViewById(R.id.edtTxtUsername);
        edtTextPassword = findViewById(R.id.edtTxtPassword);
        pgBarLogin = findViewById(R.id.pgBarLogin);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        loginViewPresenter = new LoginViewPresenter(this);
    }

    @Override
    public void onClick(View view) {
        loginViewPresenter.onLoginButtonClick(getUserName(), getPassword());
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
        UiUtils.getAlertDialog(this, "LOGIN", message, null).show();
    }

    @Override
    public void showUserNamePasswordBlankMessage() {
        UiUtils.getAlertDialog(this, "LOGIN", getResources().getString(R.string.message_username_or_password_blank), null).show();
    }

    @Override
    public void showDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);

        finish();
    }

    public String getUserName() {
        return edtTextUserName.getText().toString();
    }

    public String getPassword() {
        return edtTextPassword.getText().toString();
    }
}
