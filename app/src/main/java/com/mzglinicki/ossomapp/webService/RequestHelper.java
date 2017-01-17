package com.mzglinicki.ossomapp.webService;

import retrofit2.Call;

/**
 * Created by User on 2017-01-16.
 */

public class RequestHelper {

    private final RestService restService;

    public RequestHelper() {
        this.restService = new RestService();
    }

    private WebService getWebService() {
        return restService.getWebService();
    }

    private <ResponseType> void onRequestResult(final Call<ResponseType> call, final ServerListener<ResponseType> serverListener) {
        call.enqueue(new ServerCallback<>(serverListener));
    }

    public void getAllData(int page, final ServerListener<ServerData> serverListener) {
        onRequestResult(getWebService().getAllData(page), serverListener);
    }

    public void getItem(final int itemId, final ServerListener<ListItem> serverListener) {
        onRequestResult(getWebService().getItem(itemId), serverListener);
    }

    public void updateItem(final ListItem listItem, final int position, final ServerListener<Void> serverListener) {
        onRequestResult(getWebService().updateData(listItem, position), serverListener);
    }
}