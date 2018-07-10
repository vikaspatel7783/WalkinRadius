package com.walkinradius.beacon.networking;

public class AndroidNetworkingImpl implements AndroidNetworking {

    @Override
    public void validateCredentials(String userName, String password, final Callback callback) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    callback.onSuccess();
                }
            }
        }).start();
    }
}
