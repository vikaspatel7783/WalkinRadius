package com.walkinradius.beacon.Util;

public class UserNameAndStatusHolder {

    private String mUserName;

    private String mStatus;
    private static UserNameAndStatusHolder instance;

    public static UserNameAndStatusHolder getInstance() {
        if (instance == null) {
            instance = new UserNameAndStatusHolder();
        }
        return instance;
    }


    public String getUserName() {
        return mUserName;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }
}
