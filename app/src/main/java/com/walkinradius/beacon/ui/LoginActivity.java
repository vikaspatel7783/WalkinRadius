package com.walkinradius.beacon.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.walkinradius.beacon.R;
import com.walkinradius.beacon.presenter.LoginActivityPresenter;
import com.walkinradius.beacon.presenter.LoginViewContract;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginViewContract.LoginView {

    private EditText edtTextPassword;
    private EditText edtTextUserName;
    private ProgressBar pgBarLogin;
    private LoginActivityPresenter loginActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtTextUserName = findViewById(R.id.edtTxtUsername);
        edtTextPassword = findViewById(R.id.edtTxtPassword);
        pgBarLogin = findViewById(R.id.pgBarLogin);

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        loginActivityPresenter = new LoginActivityPresenter(this);
    }

    @Override
    public void onClick(View view) {
        loginActivityPresenter.onLoginButtonClick(getUserName(), getPassword());
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
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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
