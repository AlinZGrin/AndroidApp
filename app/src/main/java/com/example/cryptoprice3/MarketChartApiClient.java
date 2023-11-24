package com.example.cryptoprice3;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
    CoinGeckoApi coinGeckoApi = retrofit.create(CoinGeckoApi.class);

    // Make the API call
    Call<MarketChartApiResponseModel> call = coinGeckoApi.getMarketChart("usd", 7, "daily", 2);

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

                         if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                             LocalDateTime dateTime = LocalDateTime.ofInstant(
                                 Instant.ofEpochMilli(Math.round(d)),
                                 ZoneId.of("EST")

                         );
                             DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");
                             String formattedDateTime = dateTime.format(formatter);

                             System.out.println(formattedDateTime);
                         }


                     }

                System.out.println("Prices: " + apiResponse.getPrices().get(0));
            } else {
                System.out.println("Request failed. Code: " + response.code());
            }
        }

        @Override
        public void onFailure(Call<MarketChartApiResponseModel> call, Throwable t) {
            System.out.println("Request failed. Error: " + t.getMessage());
        }
    });
}
}

