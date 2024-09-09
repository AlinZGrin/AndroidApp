package com.alingrin.cryptoprice3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface CoinGeckoApiBitcoin {
    @Headers("Authorization: Bearer CG-YVbRN4Sir4qJNtEbLZnHcb8n")
    @GET("coins/bitcoin/market_chart")
    Call<MarketChartApiResponseModel> getMarketChart(
            @Query("vs_currency") String currency,
            @Query("days") int days,
            @Query("interval") String interval,
            @Query("precision") int precision

    );
}
