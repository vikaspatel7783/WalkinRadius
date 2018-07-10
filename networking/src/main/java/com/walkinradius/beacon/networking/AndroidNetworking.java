package com.walkinradius.beacon.networking;

public interface AndroidNetworking {

    interface Callback {

        void onSuccess();

    }

    void validateCredentials(String userName, String password, Callback callback);

}
