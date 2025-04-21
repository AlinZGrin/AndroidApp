package com.alingrin.cryptoprice3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class PriceNotificationService {
    private static final String CHANNEL_ID = "price_alerts";
    private static final int BITCOIN_NOTIFICATION_ID = 1;
    private static final int ETHEREUM_NOTIFICATION_ID = 2;
    private final Context context;
    private final SharedPreferences sharedPreferences;
    private double lastBitcoinPrice = 0;
    private double lastEthereumPrice = 0;

    public PriceNotificationService(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(SettingsActivity.PREFS_NAME, Context.MODE_PRIVATE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Price Alerts";
            String description = "Notifications for significant price changes";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void checkPriceChange(String coin, double currentPrice, double lastPrice) {
        if (lastPrice == 0) {
            if (coin.equals("bitcoin")) {
                lastBitcoinPrice = currentPrice;
            } else {
                lastEthereumPrice = currentPrice;
            }
            Log.d("PriceNotification", coin + ": First price update, skipping notification. Current price: " + currentPrice);
            return;
        }

        float threshold = SettingsActivity.getNotificationThreshold(sharedPreferences);
        double percentageChange = Math.abs((currentPrice - lastPrice) / lastPrice * 100);

        Log.d("PriceNotification", String.format("%s: Current Price: %.2f, Last Price: %.2f, Change: %.2f%%, Threshold: %.2f%%", 
            coin, currentPrice, lastPrice, percentageChange, threshold));

        if (percentageChange >= threshold) {
            String direction = currentPrice > lastPrice ? "increased" : "decreased";
            String message = String.format("%s price has %s by %.2f%%", 
                coin, direction, percentageChange);
            
            Log.d("PriceNotification", "Showing notification: " + message);
            showNotification(coin, message);
        } else {
            Log.d("PriceNotification", "Change not significant enough for notification");
        }

        if (coin.equals("bitcoin")) {
            lastBitcoinPrice = currentPrice;
        } else {
            lastEthereumPrice = currentPrice;
        }
    }

    private void showNotification(String coin, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        int notificationId = coin.equals("bitcoin") ? BITCOIN_NOTIFICATION_ID : ETHEREUM_NOTIFICATION_ID;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(coin.toUpperCase() + " Price Alert")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId, builder.build());
    }
} 