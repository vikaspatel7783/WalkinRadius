package com.walkinradius.beacon.networking.retrofit;

import com.walkinradius.beacon.networking.AndroidNetworking;
import com.walkinradius.beacon.networking.model.Note;
import com.walkinradius.beacon.networking.model.SubscriberCredentials;

import retrofit2.Call;
import retrofit2.Response;

public class RetrofitNetworking implements AndroidNetworking {

    private ServiceApi serviceApi = RetrofitFactory.getServiceApi();

    private Callback mCallback;

    @Override
    public void validateCredentials(String userName, String password, Callback callback) {

        this.mCallback = callback;

        SubscriberCredentials subscriberCredentials = new SubscriberCredentials();
        subscriberCredentials.setUserName(userName);
        subscriberCredentials.setPassword(password);

        //Call<SubscriberCredentials> serviceCall = serviceApi.validateSubscriber(subscriberCredentials);

        serviceApi.saveNote(getNote()).enqueue(noteCallback);

        //serviceCall.enqueue(subscriberAuthCallback);

    }

    retrofit2.Callback<Note> noteCallback = new retrofit2.Callback<Note>() {

        @Override
        public void onResponse(Call<Note> call, Response<Note> response) {
            if (response.isSuccessful()) {
                mCallback.onSuccess();
            }
        }

        @Override
        public void onFailure(Call<Note> call, Throwable t) {
                mCallback.onFailure();
        }
    };

    retrofit2.Callback<SubscriberCredentials> subscriberAuthCallback = new retrofit2.Callback<SubscriberCredentials>() {


        @Override
        public void onResponse(Call<SubscriberCredentials> call, Response<SubscriberCredentials> response) {
            if (response.isSuccessful()) {
                mCallback.onSuccess();
            }
        }

        @Override
        public void onFailure(Call<SubscriberCredentials> call, Throwable t) {
            mCallback.onFailure();
        }
    };

    private Note getNote() {
        Note note = new Note();

        note.setTitle("Title-Vikas");
        note.setBody("Body1");
        note.setUserId(1);

        return note;
    }

}
