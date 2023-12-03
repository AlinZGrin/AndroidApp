package com.alingrin.cryptoprice3;

public interface MyCallback {

    void onDataReceived(QuoteLatestResponseModel data, int code);

    void onFailure(Throwable t);
}

