package com.walkinradius.beacon.presenter;

public class LoginViewContract {

    public interface LoginView {

        void showProgressBar();

        void hideProgressBar();

        void showMessage(String message);

        void showUserNamePasswordBlankMessage();

        void showDashboard();
    }

    public interface LoginViewCallbacks {

        void onLoginButtonClick(String userName, String password);

    }
}
