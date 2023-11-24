package com.example.cryptoprice3;
import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApiClient {

    private static final String BASE_URL = "https://pro-api.coinmarketcap.com/v2/";

    public void fetchDataAsync(MyCallback callback) {
        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // ApiService interface
        ApiService apiService = retrofit.create(ApiService.class);

        // Make a GET request asynchronously
        Call<QuoteLatestResponseModel> call = apiService.getCryptoData("bitcoin,ethereum", "USD", "ffb8840c-b444-41ef-b260-cfd1544312a6", "*/*");
        call.enqueue(new Callback<QuoteLatestResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<QuoteLatestResponseModel> call, @NonNull Response<QuoteLatestResponseModel> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body(), response.code());
                } else {
                    callback.onFailure(new Exception("API call unsuccessful"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<QuoteLatestResponseModel> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
