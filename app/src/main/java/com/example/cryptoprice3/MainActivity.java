package com.example.cryptoprice3;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import java.util.Locale;
import java.util.Map;
import java.lang.Math;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://pro-api.coinmarketcap.com/v2/";
    private MyResponseModel myResponseModel;
    // Define a callback interface
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        String initialBtcName = sharedPreferences.getString("btcName", "Bitcoin");
        String initialEthName = sharedPreferences.getString("ethName", "Ethereum");
        float initialBtcPrice = sharedPreferences.getFloat("btcPrice", 0.0f);
        float initialEthPrice = sharedPreferences.getFloat("ethPrice", 0.0f);
        float initialBtc24Change = sharedPreferences.getFloat("btc24Change", 0.0f);
        float initialEth24Change = sharedPreferences.getFloat("eth24Change", 0.0f);

        TextView BTCName = findViewById(R.id.BitconPriceName);
        TextView ETHName = findViewById(R.id.EthPriceName);
        TextView price = findViewById(R.id.BTCPrice);
        TextView ETHprice = findViewById(R.id.ETHPrice);
        TextView BTC24 = findViewById(R.id.BTC24PercentChange);
        TextView ETH24 = findViewById(R.id.ETH24PercentChange);

        BTCName.setText(initialBtcName);
        ETHName.setText(initialEthName);
        price.setText("$"+initialBtcPrice);
        ETHprice.setText("$"+initialEthPrice);
        BTC24.setText(initialBtc24Change+"%");

        if ( initialBtc24Change < 0)
        {BTC24.setTextColor(Color.parseColor("#FF0000"));}
        else if (initialBtc24Change > 0) {
            BTC24.setTextColor(Color.parseColor("#00FF00"));
        }
        ETH24.setText(initialEth24Change+"%");

        if ( initialEth24Change < 0)
        {ETH24.setTextColor(Color.parseColor("#FF0000"));}
        else if (initialEth24Change > 0) {
            ETH24.setTextColor(Color.parseColor("#00FF00"));
        }


        }


    public void onBtnClick (View view) throws JSONException {


            MyApiClient myApiClient = new MyApiClient();
            myApiClient.fetchDataAsync(new MyCallback() {
                                       @Override
                                       public void onDataReceived(MyResponseModel data, int code) {



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
                                           assert bitcoin != null;
                                           String btcName = getString(R.string.BTCName,bitcoin.getName());
                                           assert ethereum != null;
                                           String ethName = getString(R.string.EName,ethereum.getName());
                                           double btcP = Math.round(bitcoin.getQuote().getUSD().getPrice()* 100.0) / 100.0;
                                           double ethP = Math.round(ethereum.getQuote().getUSD().getPrice()* 100.0) / 100.0;
                                           String btcPrice = getString(R.string.bitcoinPrice,"$",String.valueOf(btcP));
                                           String ethPrice = getString(R.string.ethereumPrice,"$",String.valueOf(ethP));
                                           double btc24c =  Math.round(bitcoin.getQuote().getUSD().getpercent_change_24h()* 100.0) / 100.0;
                                           double eth24c =  Math.round(ethereum.getQuote().getUSD().getpercent_change_24h()* 100.0) / 100.0;
                                           String btc24Change = getString(R.string.btc24change,String.valueOf(btc24c),"%");
                                           String eth24Change = getString(R.string.eth24change,String.valueOf(eth24c),"%");
                                           Log.d(TAG, "ethName: " + ethName);
                                           BTCName.setText(btcName);
                                           ETHName.setText(ethName);
                                           price.setText(btcPrice);
                                           sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                                           float initialBtcPrice = sharedPreferences.getFloat("btcPrice", (float) btcP);
                                           float initialEthPrice = sharedPreferences.getFloat("ethPrice", (float) ethP);






                                           animatePriceChange((float) btcP,initialBtcPrice,price);
                                           animatePriceChange((float) ethP,initialEthPrice,ETHprice);





                                           ETHprice.setText(ethPrice);
                                           BTC24.setText(btc24Change);
                                           if ( btc24c < 0)
                                           {BTC24.setTextColor(Color.parseColor("#FF0000"));}
                                           else if (btc24c > 0) {
                                               BTC24.setTextColor(Color.parseColor("#00FF00"));
                                           }
                                           ETH24.setText(eth24Change);
                                           if ( eth24c < 0)
                                           {ETH24.setTextColor(Color.parseColor("#FF0000"));}
                                           else if (eth24c > 0) {
                                               ETH24.setTextColor(Color.parseColor("#00FF00"));
                                           }


                                           SharedPreferences.Editor editor = sharedPreferences.edit();
                                           editor.putString("btcName", btcName);
                                           editor.putString("ethName", ethName);
                                           editor.putFloat("btcPrice", (float) btcP);
                                           editor.putFloat("ethPrice", (float) ethP);
                                           editor.putFloat("btc24Change", (float) btc24c);
                                           editor.putFloat("eth24Change", (float) eth24c);
                                           editor.apply();

                                       }

                private void animatePriceChange(float finalPrice, float initialPrice, TextView priceTextView) {
                    ValueAnimator animator = ValueAnimator.ofFloat(initialPrice,finalPrice );
                    animator.setDuration(2000); // Animation duration in milliseconds
                    animator.setInterpolator(new AccelerateDecelerateInterpolator());
                    animator.addUpdateListener(animation -> {
                        float currentPrice = (float) animation.getAnimatedValue();
                        priceTextView.setText(String.format(Locale.US,"$%.2f", currentPrice));
                        if (initialPrice<finalPrice)
                        {priceTextView.setTextColor(Color.parseColor("#00FF00"));}
                        else if (finalPrice<initialPrice)
                        {priceTextView.setTextColor(Color.parseColor("#FF0000"));}
                    });
                    animator.start();
                }

                @Override
                public void onFailure(Throwable t) {
                    // Handle the failure
                    t.printStackTrace();
                }
            });


        }


        }




