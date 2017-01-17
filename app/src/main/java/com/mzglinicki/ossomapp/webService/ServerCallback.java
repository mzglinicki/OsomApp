package com.mzglinicki.ossomapp.webService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by User on 2017-01-16.
 */
public class ServerCallback<T> implements Callback<T> {

    private final ServerListener<T> serverListener;

    public ServerCallback(final ServerListener<T> serverListener) {
        this.serverListener = serverListener;
    }

    @Override
    public void onResponse(final Call<T> call, final Response<T> response) {
        initResponseMethod(response, serverListener);
    }

    @Override
    public void onFailure(final Call<T> call, final Throwable throwable) {
        serverListener.onFailureResponse();
    }

    private <ResponseType> void initResponseMethod(final Response<ResponseType> response, final ServerListener<ResponseType> serverListener) {
        if (response.isSuccessful()) {
            serverListener.onSuccessfulResponse(response);
        } else {
            serverListener.onUnsuccessfulResponse(response);
        }
    }
}