package com.alingrin.cryptoprice3;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity  extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    // Define a callback interface
    private SharedPreferences sharedPreferences;
    private static final long INTERVAL = 45 * 1000; // 60 seconds in milliseconds
    private Handler handler;
    private MarketChartApiClient marketChartApiClient;
    private boolean isBTCNameClicked = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.CryptoPrice);
        //swipeRefreshLayout.setOnRefreshListener(this);
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
        price.setText("$" + initialBtcPrice);
        ETHprice.setText("$" + initialEthPrice);
        BTC24.setText(initialBtc24Change + "%");

        if (initialBtc24Change < 0) {
            BTC24.setTextColor(Color.parseColor("#FF0000"));
        } else if (initialBtc24Change > 0) {
            BTC24.setTextColor(Color.parseColor("#00FF00"));
        }
        ETH24.setText(initialEth24Change + "%");

        if (initialEth24Change < 0) {
            ETH24.setTextColor(Color.parseColor("#FF0000"));
        } else if (initialEth24Change > 0) {
            ETH24.setTextColor(Color.parseColor("#00FF00"));
        }
        BTCName.performClick();
        isBTCNameClicked = true;
        //ETHName.performClick();
        fetchDataWithMyApiClient();

        handler = new Handler();
        handler.post(runnable); // Start the recurring task immediately

    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            TextView BTCName = findViewById(R.id.BitconPriceName);
            TextView ETHName = findViewById(R.id.EthPriceName);
            //BTCName.performClick();
            //ETHName.performClick();
            fetchDataWithMyApiClient();

            // Restart the task after the interval
            handler.postDelayed(this, INTERVAL);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the recurring task when the activity is destroyed
        handler.removeCallbacks(runnable);
    }

    public void onBTCNameBtnClick(View view) throws JSONException {


        Button dayButton = findViewById(R.id.DayButton);
        Button weekbutton = findViewById(R.id.WeekButton);
        Button monthButton = findViewById(R.id.MonthButton);
        Button yearButton = findViewById(R.id.YearButton);
        int defaultColor = getResources().getColor(R.color.purple);
        dayButton.setBackgroundColor(defaultColor);
        weekbutton.setBackgroundColor(defaultColor);
        monthButton.setBackgroundColor(Color.RED);
        yearButton.setBackgroundColor(defaultColor);

        marketChartApiClient = new MarketChartApiClient(this);

        // Example usage: Fetch Bitcoin data
        fetchDataForBitcoin(30,"daily");
        isBTCNameClicked = true;

        //fetchDataWithMyApiClient();
    }
    public void onDayButtonClick(View view) throws JSONException{
        Button dayButton = findViewById(R.id.DayButton);
        Button weekbutton = findViewById(R.id.WeekButton);
        Button monthButton = findViewById(R.id.MonthButton);
        Button yearButton = findViewById(R.id.YearButton);
        int defaultColor = getResources().getColor(R.color.purple);
        dayButton.setBackgroundColor(Color.RED);
        weekbutton.setBackgroundColor(defaultColor);
        monthButton.setBackgroundColor(defaultColor);
        yearButton.setBackgroundColor(defaultColor);
        if (isBTCNameClicked) {
            fetchDataForBitcoin(1, "");
        }
        else {
            fetchDataForEthereum(1, "");
        }
    }
    public void onWeekButtonClick(View view) throws JSONException{
        Button dayButton = findViewById(R.id.DayButton);
        Button weekbutton = findViewById(R.id.WeekButton);
        Button monthButton = findViewById(R.id.MonthButton);
        Button yearButton = findViewById(R.id.YearButton);
        int defaultColor = getResources().getColor(R.color.purple);
        dayButton.setBackgroundColor(defaultColor);
        weekbutton.setBackgroundColor(Color.RED);
        monthButton.setBackgroundColor(defaultColor);
        yearButton.setBackgroundColor(defaultColor);
        if (isBTCNameClicked) {
            fetchDataForBitcoin(7, "daily");
        }
        else {
            fetchDataForEthereum(7, "daily");
        }
    }
    public void onMonthButtonClick(View view) throws JSONException{
        Button dayButton = findViewById(R.id.DayButton);
        Button weekbutton = findViewById(R.id.WeekButton);
        Button monthButton = findViewById(R.id.MonthButton);
        Button yearButton = findViewById(R.id.YearButton);
        int defaultColor = getResources().getColor(R.color.purple);
        dayButton.setBackgroundColor(defaultColor);
        weekbutton.setBackgroundColor(defaultColor);
        monthButton.setBackgroundColor(Color.RED);
        yearButton.setBackgroundColor(defaultColor);
        if (isBTCNameClicked) {
            fetchDataForBitcoin(30, "daily");
        }
        else {
            fetchDataForEthereum(30, "daily");
        }
    }
    public void onYearButtonClick(View view) throws JSONException{
        Button dayButton = findViewById(R.id.DayButton);
        Button weekbutton = findViewById(R.id.WeekButton);
        Button monthButton = findViewById(R.id.MonthButton);
        Button yearButton = findViewById(R.id.YearButton);
        int defaultColor = getResources().getColor(R.color.purple);
        dayButton.setBackgroundColor(defaultColor);
        weekbutton.setBackgroundColor(defaultColor);
        monthButton.setBackgroundColor(defaultColor);
        yearButton.setBackgroundColor(Color.RED);
        if (isBTCNameClicked) {
            fetchDataForBitcoin(365, "daily");
        }
        else {
            fetchDataForEthereum(365, "daily");
        }
    }
    private void fetchDataForBitcoin(int day,String interval) {
        // Define the callback for handling the API response
        ChartCallback callback = new ChartCallback() {
            @Override
            public void onDataReceived(MarketChartApiResponseModel data, int code) {
                Log.d(TAG, "Chart API Response: " + code);
                Log.d(TAG, "Chart data point: " + data.getPrices());

                assert data.getPrices() != null;
                List<List<Double>> dataPoints = data.getPrices();
                List<List<String>> dataPointsOut = new ArrayList<>();
                LineGraphView lineGraphView = findViewById(R.id.lineGraphView);
                List<LineGraphView.DataPoint> dataPoints1 = new ArrayList<>();
                for (int i = 0; i < dataPoints.size(); i++) {
                    List<Double> datapoint = dataPoints.get(i);

                    Double d = datapoint.get(0);
                    Double p = datapoint.get(1);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalDateTime dateTime = LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(Math.round(d)),
                                ZoneId.of("EST")

                        );
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        String formattedDateTime = dateTime.format(formatter);

                        System.out.println(formattedDateTime + " " + p);
                        dataPointsOut.add(i, Collections.singletonList(formattedDateTime + "," + p));
                        dataPoints1.add(new LineGraphView.DataPoint(formattedDateTime, p));
                        System.out.println("DataPointsOut: " + dataPointsOut);

                    }
                    lineGraphView.setDataPoints(dataPoints1);
                    lineGraphView.setCoinName("Bitcoin");

                }

            }

            @Override
            public void onFailure(Throwable t) {


            }
        };
        // Call the API to fetch Bitcoin data
        marketChartApiClient.fetchDataAsyncBTC(callback, day, interval);
    }

    public void onETHNameBtnClick(View view) throws JSONException {

        Button dayButton = findViewById(R.id.DayButton);
        Button weekbutton = findViewById(R.id.WeekButton);
        Button monthButton = findViewById(R.id.MonthButton);
        Button yearButton = findViewById(R.id.YearButton);
        int defaultColor = getResources().getColor(R.color.purple);
        dayButton.setBackgroundColor(defaultColor);
        weekbutton.setBackgroundColor(defaultColor);
        monthButton.setBackgroundColor(Color.RED);
        yearButton.setBackgroundColor(defaultColor);

        MarketChartApiClient marketChartApiClient = new MarketChartApiClient(this);
        // Example usage: Fetch Ethereum data
        fetchDataForEthereum(30,"daily");
        //fetchDataWithMyApiClient();
        isBTCNameClicked = false;
    }

    private void fetchDataForEthereum(int day,String interval) {
        ChartCallback callback = new ChartCallback() {
            @Override
            public void onDataReceived(MarketChartApiResponseModel data, int code) {
                Log.d(TAG, "Chart API Response: " + code);
                Log.d(TAG, "Chart data point: " + data.getPrices());
                assert data.getPrices() != null;
                List<List<Double>> dataPoints = data.getPrices();
                List<List<String>> dataPointsOut = new ArrayList<>();
                LineGraphView lineGraphView = findViewById(R.id.lineGraphView);
                List<LineGraphView.DataPoint> dataPoints1 = new ArrayList<>();

                for (int i = 0; i < dataPoints.size(); i++) {
                    List<Double> datapoint = dataPoints.get(i);

                    Double d = datapoint.get(0);
                    Double p = datapoint.get(1);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalDateTime dateTime = LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(Math.round(d)),
                                ZoneId.of("EST")

                        );
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        String formattedDateTime = dateTime.format(formatter);

                        System.out.println(formattedDateTime + " " + p);
                        dataPointsOut.add(i, Collections.singletonList(formattedDateTime + "," + p));
                        dataPoints1.add(new LineGraphView.DataPoint(formattedDateTime, p));
                        System.out.println("DataPointsOut: " + dataPointsOut);

                    }
                    String coinName = "Ethereum";
                    lineGraphView.setDataPoints(dataPoints1);
                    lineGraphView.setCoinName("Ethereum");

                }

            }

            @Override
            public void onFailure(Throwable t) {

            }
        };

        // Call the API to fetch Ethereum data
        marketChartApiClient.fetchDataAsyncEthereum(callback, day, interval);
    }
    // Use MyApiClient to fetch data


    public void fetchDataWithMyApiClient() {
        MyApiClient myApiClient = new MyApiClient();
        myApiClient.fetchDataAsync(new MyCallback() {
            @Override
            public void onDataReceived(QuoteLatestResponseModel data, int code) {


                Log.d(TAG, "API Response: " + code);
                TextView BTCName = findViewById(R.id.BitconPriceName);
                TextView ETHName = findViewById(R.id.EthPriceName);
                TextView price = findViewById(R.id.BTCPrice);
                TextView ETHprice = findViewById(R.id.ETHPrice);
                TextView BTC24 = findViewById(R.id.BTC24PercentChange);
                TextView ETH24 = findViewById(R.id.ETH24PercentChange);
                Map<String, QuoteLatestResponseModel.CryptoCurrency> cryptoCurrencyMap = data.getData();
                QuoteLatestResponseModel.CryptoCurrency bitcoin = cryptoCurrencyMap.get("1");
                QuoteLatestResponseModel.CryptoCurrency ethereum = cryptoCurrencyMap.get("1027");
                assert bitcoin != null;
                String btcName = getString(R.string.BTCName, bitcoin.getName());
                assert ethereum != null;
                String ethName = getString(R.string.EName, ethereum.getName());
                double btcP = Math.round(bitcoin.getQuote().getUSD().getPrice() * 100.0) / 100.0;
                double ethP = Math.round(ethereum.getQuote().getUSD().getPrice() * 100.0) / 100.0;
                String btcPrice = getString(R.string.bitcoinPrice, "$", String.valueOf(btcP));
                String ethPrice = getString(R.string.ethereumPrice, "$", String.valueOf(ethP));
                double btc24c = Math.round(bitcoin.getQuote().getUSD().getpercent_change_24h() * 100.0) / 100.0;
                double eth24c = Math.round(ethereum.getQuote().getUSD().getpercent_change_24h() * 100.0) / 100.0;
                String btc24Change = getString(R.string.btc24change, String.valueOf(btc24c), "%");
                String eth24Change = getString(R.string.eth24change, String.valueOf(eth24c), "%");
                Log.d(TAG, "ethName: " + ethName);
                Log.d(TAG, "btcName: " + btcName);
                BTCName.setText(btcName);
                ETHName.setText(ethName);
                price.setText(btcPrice);
                sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                float initialBtcPrice = sharedPreferences.getFloat("btcPrice", (float) btcP);
                float initialEthPrice = sharedPreferences.getFloat("ethPrice", (float) ethP);


                animatePriceChange((float) btcP, initialBtcPrice, price);
                animatePriceChange((float) ethP, initialEthPrice, ETHprice);


                ETHprice.setText(ethPrice);
                BTC24.setText(btc24Change);
                if (btc24c < 0) {
                    BTC24.setTextColor(Color.parseColor("#FF0000"));
                } else if (btc24c > 0) {
                    BTC24.setTextColor(Color.parseColor("#00FF00"));
                }
                ETH24.setText(eth24Change);
                if (eth24c < 0) {
                    ETH24.setTextColor(Color.parseColor("#FF0000"));
                } else if (eth24c > 0) {
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

            private void animatePriceChange(float finalPrice, float initialPrice, TextView
                    priceTextView) {
                ValueAnimator animator = ValueAnimator.ofFloat(initialPrice, finalPrice);
                animator.setDuration(2000); // Animation duration in milliseconds
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.addUpdateListener(animation -> {
                    float currentPrice = (float) animation.getAnimatedValue();
                    priceTextView.setText(String.format(Locale.US, "$%.2f", currentPrice));
                    if (initialPrice < finalPrice) {
                        priceTextView.setTextColor(Color.parseColor("#00FF00"));
                    } else if (finalPrice < initialPrice) {
                        priceTextView.setTextColor(Color.parseColor("#FF0000"));
                    }
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















