package com.example.cryptoprice3;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.util.Map;
import java.lang.Math;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://pro-api.coinmarketcap.com/v2/";
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
                                           //Log.d(TAG, "API Response: " + data.data.getOne().getQuote().getUSD().getPrice());
                                           //Log.d(TAG, "API Response: " + data.price);
                                           Log.d(TAG, "API Response: " + code);
                                           TextView BTCName = findViewById(R.id.BitconPriceName);
                                           TextView ETHName = findViewById(R.id.EthPriceName);
                                           TextView price = findViewById(R.id.BTCPrice);
                                           TextView ETHprice = findViewById(R.id.ETHPrice);
                                           TextView BTC24 = findViewById(R.id.BTC24PercentChange);
                                           TextView ETH24 = findViewById(R.id.ETH24PercentChange);
                                           Map<String, MyResponseModel.CryptoCurrency> cryptoCurrencyMap = data.getData();
                                           MyResponseModel.CryptoCurrency bitcoin = cryptoCurrencyMap.get("1");
                                           MyResponseModel.CryptoCurrency ethereum  = cryptoCurrencyMap.get("1027");
                                           String btcName = getString(R.string.BTCName,bitcoin.getName());
                                           String ethName = getString(R.string.EName,ethereum.getName());
                                           String btcPrice = getString(R.string.bitcoinPrice,"$",String.valueOf((double)Math.round(bitcoin.getQuote().getUSD().getPrice()* 100.0) / 100.0));
                                           String ethPrice = getString(R.string.ethereumPrice,"$",String.valueOf((double)Math.round(ethereum.getQuote().getUSD().getPrice()* 100.0) / 100.0));
                                           String btc24Change = getString(R.string.btc24change,String.valueOf((double)Math.round(bitcoin.getQuote().getUSD().getpercent_change_24h()* 100.0) / 100.0),"%");
                                           String eth24Change = getString(R.string.eth24change,String.valueOf((double)Math.round(ethereum.getQuote().getUSD().getpercent_change_24h()* 100.0) / 100.0),"%");
                                           Log.d(TAG, "ethName: " + ethName);
                                           BTCName.setText(btcName);
                                           ETHName.setText(ethName);
                                           price.setText(btcPrice);
                                           ETHprice.setText(ethPrice);
                                           BTC24.setText(btc24Change);
                                           if ( bitcoin.getQuote().getUSD().getpercent_change_24h() < 0)
                                           {BTC24.setTextColor(Color.parseColor("#FF0000"));}
                                           else if (bitcoin.getQuote().getUSD().getpercent_change_24h() > 0) {
                                               BTC24.setTextColor(Color.parseColor("#00FF00"));
                                           }
                                           ETH24.setText(eth24Change);
                                           if ( ethereum.getQuote().getUSD().getpercent_change_24h() < 0)
                                           {ETH24.setTextColor(Color.parseColor("#FF0000"));}
                                           else if (ethereum.getQuote().getUSD().getpercent_change_24h() > 0) {
                                               ETH24.setTextColor(Color.parseColor("#00FF00"));
                                           }
                                       }
                @Override
                public void onFailure(Throwable t) {
                    // Handle the failure
                    t.printStackTrace();
                }
            });
        }


        }




