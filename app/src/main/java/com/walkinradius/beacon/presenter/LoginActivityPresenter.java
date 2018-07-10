package com.walkinradius.beacon.presenter;

public class LoginActivityPresenter implements LoginViewContract.LoginViewCallbacks {

    private final LoginViewContract.LoginView mLoginActivity;

    public LoginActivityPresenter(LoginViewContract.LoginView loginActivity) {
        this.mLoginActivity = loginActivity;
    }

    @Override
    public void onLoginButtonClick(String userName, String password) {

        LoginFieldsValidator loginFieldsValidator = new LoginFieldsValidator();

        boolean isUserNameLengthNonZero = loginFieldsValidator.isLengthNonZero(userName);
        if (!isUserNameLengthNonZero) {
            mLoginActivity.showMessage("User name should not be empty");
            return;
        }

        boolean isPasswordLengthNonZero = loginFieldsValidator.isLengthNonZero(password);
        if (!isPasswordLengthNonZero) {
            mLoginActivity.showMessage("Password should not be empty");
            return;
        }

        mLoginActivity.showProgressBar();
    }
}
