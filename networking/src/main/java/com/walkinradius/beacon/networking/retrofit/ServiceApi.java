package com.walkinradius.beacon.networking.retrofit;

import com.walkinradius.beacon.networking.model.Note;
import com.walkinradius.beacon.networking.model.SubscriberCredentials;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ServiceApi {

    @POST("/posts")
        //@FormUrlEncoded
    Call<SubscriberCredentials> validateSubscriber(/*@Field("title") String title,
                        @Field("body") String body,
                        @Field("userId") long userId*/
            @Body SubscriberCredentials credentials);


    @POST("/posts")
        //@FormUrlEncoded
    Call<Note> saveNote(/*@Field("title") String title,
                        @Field("body") String body,
                        @Field("userId") long userId*/
            @Body Note note);

}
