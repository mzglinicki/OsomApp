package com.mzglinicki.ossomapp.webService;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Marcin on 16.01.2017.
 */
public interface WebService {

    @GET("/talks")
    Call<ServerData> getAllData(@Query("page") int page);

    @GET("/talks/{id}")
    Call<ListItem> getItem(@Path("id") int id);

    @PUT("/talks/{id}")
    Call<Void> updateData(@Body ListItem listItem,
                          @Path("id") int id);
}