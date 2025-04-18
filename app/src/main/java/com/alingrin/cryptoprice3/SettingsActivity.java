package com.alingrin.cryptoprice3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private EditText thresholdEditText;
    private SharedPreferences sharedPreferences;
    public static final String PREFS_NAME = "CryptoPricePrefs";
    private static final String THRESHOLD_KEY = "notification_threshold";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        thresholdEditText = findViewById(R.id.thresholdEditText);
        Button saveButton = findViewById(R.id.saveButton);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        float currentThreshold = sharedPreferences.getFloat(THRESHOLD_KEY, 3.0f);
        thresholdEditText.setText(String.valueOf(currentThreshold));

        saveButton.setOnClickListener(v -> saveThreshold());
    }

    private void saveThreshold() {
        try {
            float threshold = Float.parseFloat(thresholdEditText.getText().toString());
            if (threshold <= 0) {
                Toast.makeText(this, "Threshold must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
            
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat(THRESHOLD_KEY, threshold);
            editor.apply();
            
            Toast.makeText(this, "Threshold saved successfully", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    public static float getNotificationThreshold(SharedPreferences prefs) {
        return prefs.getFloat(THRESHOLD_KEY, 3.0f);
    }
} 