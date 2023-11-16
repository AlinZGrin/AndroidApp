package com.example.cryptoprice3;

public interface MyCallback {

    void onDataReceived(MyResponseModel data, int code);

    void onFailure(Throwable t);
}
