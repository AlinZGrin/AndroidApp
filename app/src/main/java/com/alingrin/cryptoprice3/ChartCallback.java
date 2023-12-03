package com.alingrin.cryptoprice3;

public interface ChartCallback {

    void onDataReceived(MarketChartApiResponseModel data, int code);

    void onFailure(Throwable t);
}
