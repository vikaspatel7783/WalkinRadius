package com.walkinradius.beacon.networking.retrofit;

import com.walkinradius.beacon.networking.model.Note;
import com.walkinradius.beacon.networking.model.Curdata;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceApi {

    @GET("client_login.php")
        //@FormUrlEncoded
    Call<Curdata> validateSubscriber(@Query("username") String userName,
                                     @Query("password") String password);


    @POST("/posts")
        //@FormUrlEncoded
    Call<Note> saveNote(/*@Field("title") String title,
                        @Field("body") String body,
                        @Field("userId") long userId*/
            @Body Note note);

}
