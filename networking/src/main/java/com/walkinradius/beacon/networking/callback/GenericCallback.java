package com.walkinradius.beacon.networking.callback;

public abstract class GenericCallback<T> {

    abstract void onSuccess(T message);

    abstract void onFailure(T message);

}
