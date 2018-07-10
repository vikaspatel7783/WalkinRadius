package com.walkinradius.beacon.presenter;

class LoginFieldsValidator {


    public boolean isLengthNonZero(String userName) {

        if (null == userName || userName.length() == 0) {
            return false;
        }

        return true;
    }
}
