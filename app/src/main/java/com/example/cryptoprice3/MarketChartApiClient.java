package com.example.cryptoprice3;
import android.content.Context;
import android.widget.Toast;

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
    private Context context;

    // Constructor
    public MarketChartApiClient(Context context) {
        this.context = context;
    }

    // Setter method
    public void setContext(Context context) {
        this.context = context;
    }
    private static final String  baseUrl = "https://api.coingecko.com/api/v3/";
    public void fetchDataAsyncBTC(ChartCallback callback, int days, String interval) {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    // Create an instance of the CoinGeckoApi interface
    CoinGeckoApiBitcoin coinGeckoApiBitcoin = retrofit.create(CoinGeckoApiBitcoin.class);

    // Make the API call
    Call<MarketChartApiResponseModel> call = coinGeckoApiBitcoin.getMarketChart("USD" ,days ,interval, 2);

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


    public void fetchDataAsyncEthereum(ChartCallback callback,int days, String interval) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the CoinGeckoApi interface
        CoinGeckoApiEthereum coinGeckoApiEthereum = retrofit.create(CoinGeckoApiEthereum.class);

        // Make the API call
        Call<MarketChartApiResponseModel> call = coinGeckoApiEthereum.getMarketChart("USD", days, interval, 2);

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
                    Callback<MarketChartApiResponseModel> context=this;
                    if (response.code() == 429 && context != null) {


                            showToast("Too many requests. Please try again later.");
                        } else {
                            showToast("An error occurred. Please try again.");
                        }






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

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

}
}
