package com.example.cryptoprice3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinGeckoApi {
    @GET("coins/bitcoin/market_chart")
    Call<MarketChartApiResponseModel> getMarketChart(
            @Query("vs_currency") String currency,
            @Query("days") int days,
            @Query("interval") String interval,
            @Query("precision") int precision
    );
}
