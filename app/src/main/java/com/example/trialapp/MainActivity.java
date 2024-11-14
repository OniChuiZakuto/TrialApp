package com.example.trialapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.content.SharedPreferences;
import java.util.ArrayList;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS = 123;
    private static final String ASSISTANCE_MESSAGE = "Someone is in need of assistance!";

    private SharedPreferences sharedPreferences;
    private TextView userNameTextView;
    private ArrayList<String> retrievedPhoneNumbers = new ArrayList<>();  // Store retrieved phone numbers

    private final ActivityResultLauncher<Intent> bertActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        ArrayList<String> phoneNumbers = data.getStringArrayListExtra("phone_numbers");
                        if (phoneNumbers != null) {
                            retrievedPhoneNumbers = phoneNumbers; // Store phone numbers for SMS
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        userNameTextView = findViewById(R.id.userNameTextView);
        loadData();

        requestPermissions();

        Button startServiceButton = findViewById(R.id.startServiceButton);
        Button stopServiceButton = findViewById(R.id.stopServiceButton);
        Button yesButton = findViewById(R.id.yesButton);
        Button noButton = findViewById(R.id.noButton);
        Button accountButton = findViewById(R.id.accountButton);
        Button bertContactButton = findViewById(R.id.bertContactButton);

        startServiceButton.setOnClickListener(v -> {
            if (hasPermissions()) {
                Intent intent = new Intent(MainActivity.this, VoiceRecognitionService.class);
                startForegroundService(intent);
            } else {
                Toast.makeText(this, "Permissions are not granted", Toast.LENGTH_SHORT).show();
            }
        });

        stopServiceButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VoiceRecognitionService.class);
            stopService(intent);
        });

        yesButton.setOnClickListener(v -> {
            if (!retrievedPhoneNumbers.isEmpty()) {
                sendSms(retrievedPhoneNumbers);  // Send SMS to retrieved phone numbers
                Intent intent = new Intent(MainActivity.this, AssistanceActivity.class);
                intent.putStringArrayListExtra("phone_numbers", retrievedPhoneNumbers);  // Pass phone numbers to AssistanceActivity
                Log.d("MainActivity", "Passing phone numbers to AssistanceActivity: " + retrievedPhoneNumbers);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No phone numbers found. Please enter contact details.", Toast.LENGTH_SHORT).show();
            }
        });

        noButton.setOnClickListener(v -> finish());

        accountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
        });

        bertContactButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BERTActivity.class);
            bertActivityLauncher.launch(intent);
        });
    }

    private void requestPermissions() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.FOREGROUND_SERVICE_MICROPHONE,
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.SEND_SMS
            }, REQUEST_CODE_PERMISSIONS);
        }
    }

    private boolean hasPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }

    private void loadData() {
        String givenName = sharedPreferences.getString("givenName", "User");
        userNameTextView.setText(getString(R.string.welcome_message, givenName));
    }

    private void sendSms(ArrayList<String> phoneNumbers) {
        SmsManager smsManager = SmsManager.getDefault();  // Use SmsManager.getDefault() for simplicity
        for (String phoneNumber : phoneNumbers) {
            try {
                smsManager.sendTextMessage(phoneNumber, null, ASSISTANCE_MESSAGE, null, null);
                Log.d("MainActivity", "Sent SMS to: " + phoneNumber);
            } catch (Exception e) {
                Log.e("MainActivity", "Error sending SMS", e);  // Better logging
                Toast.makeText(this, "Failed to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (hasPermissions()) {
                // Optional: Log or perform an action when permissions are granted
                Log.d("MainActivity", "Permissions granted");
            } else {
                // Permissions denied
                Toast.makeText(this, "Permissions are not granted", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
