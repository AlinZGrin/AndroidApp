package com.example.cryptoprice3;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarketChartApiClient {
    // Create Retrofit instance

    private static final String  baseUrl = "https://api.coingecko.com/api/v3/";
    public void fetchDataAsync(ChartCallback callback) {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Create an instance of the CoinGeckoApi interface
    CoinGeckoApiBitcoin coinGeckoApiBitcoin = retrofit.create(CoinGeckoApiBitcoin.class);

    // Make the API call
    Call<MarketChartApiResponseModel> call = coinGeckoApiBitcoin.getMarketChart("usd", 30, "daily", 2);

    // Enqueue the call to execute asynchronously
        call.enqueue(new Callback<MarketChartApiResponseModel>() {
        @Override
        public void onResponse(Call<MarketChartApiResponseModel> call, Response<MarketChartApiResponseModel> response) {
            if (response.isSuccessful()) {
                MarketChartApiResponseModel apiResponse = response.body();
                // Handle the response data here


                System.out.println("Prices: " + apiResponse.getPrices().get(0));
            } else {
                System.out.println("Request failed. Code: " + response.code());
            }
            if (response.isSuccessful()) {
                callback.onDataReceived(response.body(), response.code());
            } else {
                callback.onFailure(new Exception("API call unsuccessful"));
            }
        }

        @Override
        public void onFailure(Call<MarketChartApiResponseModel> call, Throwable t) {
            System.out.println("Request failed. Error: " + t.getMessage());
        }
    });
}


    public void fetchDataAsyncEthereum(ChartCallback callback) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the CoinGeckoApi interface
        CoinGeckoApiEthereum coinGeckoApiEthereum = retrofit.create(CoinGeckoApiEthereum.class);

        // Make the API call
        Call<MarketChartApiResponseModel> call = coinGeckoApiEthereum.getMarketChart("usd", 30, "daily", 2);

        // Enqueue the call to execute asynchronously
        call.enqueue(new Callback<MarketChartApiResponseModel>() {
            @Override
            public void onResponse(Call<MarketChartApiResponseModel> call, Response<MarketChartApiResponseModel> response) {
                if (response.isSuccessful()) {
                    MarketChartApiResponseModel apiResponse = response.body();
                    // Handle the response data here
                    assert response.body() != null;
                    List<List<Double>> dataPoints = response.body().getPrices();
                    for (int i=0;i<dataPoints.size();i++)
                    {
                        List<Double> datapoint = dataPoints.get(i);
                        Double d =  datapoint.get(0);
                        Double p =  datapoint.get(1);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            LocalDateTime dateTime = LocalDateTime.ofInstant(
                                    Instant.ofEpochMilli(Math.round(d)),
                                    ZoneId.of("EST")

                            );
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                            String formattedDateTime = dateTime.format(formatter);

                            System.out.println(formattedDateTime+" "+p);
                        }


                    }

                    System.out.println("Prices: " + apiResponse.getPrices().get(0));
                } else {
                    System.out.println("Request failed. Code: " + response.code());
                }
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body(), response.code());
                } else {
                    callback.onFailure(new Exception("API call unsuccessful"));
                }
            }

            @Override
            public void onFailure(Call<MarketChartApiResponseModel> call, Throwable t) {
                System.out.println("Request failed. Error: " + t.getMessage());
            }
        });
    }
}

