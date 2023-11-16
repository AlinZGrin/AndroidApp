package com.example.cryptoprice3;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://pro-api.coinmarketcap.com/";
    private MyResponseModel myResponseModel;
    // Define a callback interface

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        }


    public void onBtnClick (View view) throws JSONException {
            MyApiClient myApiClient = new MyApiClient();
            myApiClient.fetchDataAsync(new MyCallback() {
                                       @Override
                                       public void onDataReceived(MyResponseModel data, int code) {
                                           // Handle the successful response
                                           Log.d(TAG, "API Response: " + data.data.getOne().getQuote().getUSD().getPrice());
                                           //Log.d(TAG, "API Response: " + data.price);
                                           Log.d(TAG, "API Response: " + code);
                                           TextView BTCName = findViewById(R.id.BitconPriceName);
                                           TextView ETHName = findViewById(R.id.EthPriceName);
                                           TextView price = findViewById(R.id.BTCPrice);
                                           TextView ETHprice = findViewById(R.id.ETHPrice);
                                           //String btcName = getString(R.string.BTCName,String.valueOf(data.data.getOne().getName()));
                                           String CurrentPrice = getString(R.string.bitcoinPrice,"$",String.valueOf(data.data.getOne().getQuote().getUSD().getPrice()));
                                           //BTCName.setText(btcName);
                                           price.setText(CurrentPrice);
                                       }
                @Override
                public void onFailure(Throwable t) {
                    // Handle the failure
                    t.printStackTrace();
                }
            });
        }


        }




