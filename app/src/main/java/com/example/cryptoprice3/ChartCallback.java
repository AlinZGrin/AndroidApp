package com.example.cryptoprice3;

public interface ChartCallback {

    void onDataReceived(MarketChartApiResponseModel data, int code);

    void onFailure(Throwable t);
}
