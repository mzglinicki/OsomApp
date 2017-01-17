package com.mzglinicki.ossomapp.webService;

import retrofit2.Response;

/**
 * Created by User on 2017-01-16.
 */
public interface ServerListener<T> {

    void onSuccessfulResponse(final Response<T> response);

    void onUnsuccessfulResponse(final Response<T> response);

    void onFailureResponse();
}