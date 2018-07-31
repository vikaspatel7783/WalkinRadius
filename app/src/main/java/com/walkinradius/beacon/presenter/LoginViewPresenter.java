package com.walkinradius.beacon.presenter;

import com.walkinradius.beacon.networking.AndroidNetworking;
import com.walkinradius.beacon.networking.retrofit.RetrofitNetworking;

public class LoginViewPresenter implements LoginViewContract.LoginViewCallbacks {

    private final LoginViewContract.LoginView mLoginActivity;

    private AndroidNetworking mAndroidNetworking = new RetrofitNetworking();

    public LoginViewPresenter(LoginViewContract.LoginView loginActivity) {
        this.mLoginActivity = loginActivity;
    }

    @Override
    public void onLoginButtonClick(String userName, String password) {

        if (!isCredentialsValidForLength(userName, password)) {
            return;
        }

        mLoginActivity.showProgressBar();

        mAndroidNetworking.validateCredentials(userName, password, callback);
    }

    private AndroidNetworking.Callback callback = new AndroidNetworking.Callback() {

        @Override
        public void onSuccess(String message) {
            //mLoginActivity.showMessage(message);
            mLoginActivity.hideProgressBar();

            mLoginActivity.showDashboard();
        }

        @Override
        public void onFailure(String message) {
            mLoginActivity.showMessage(message);
            mLoginActivity.hideProgressBar();
        }
    };

    private boolean isCredentialsValidForLength(String userName, String password) {

        LoginFieldsValidator loginFieldsValidator = new LoginFieldsValidator();

        boolean isUserNameLengthNonZero = loginFieldsValidator.isLengthNonZero(userName);
        if (!isUserNameLengthNonZero) {
            mLoginActivity.showUserNamePasswordBlankMessage();
            return false;
        }

        boolean isPasswordLengthNonZero = loginFieldsValidator.isLengthNonZero(password);
        if (!isPasswordLengthNonZero) {
            mLoginActivity.showUserNamePasswordBlankMessage();
            return false;
        }

        return true;
    }
}
