package com.example.cryptoprice3;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ApiService {
    @Headers({"Content-Type: text/plain"})
    @GET("cryptocurrency/quotes/latest")

    Call<QuoteLatestResponseModel> getCryptoData(

            @Query("slug") String slug,
            @Query("convert") String convert,
            @Header("X-CMC_PRO_API_KEY") String apiKey,
            @Header("Accept") String accept);

}

