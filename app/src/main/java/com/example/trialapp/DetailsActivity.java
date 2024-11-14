package com.example.trialapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private FrameLayout paneLayout;
    private String affectedArea;
    private String assistanceMessage;
    private static final int REQUEST_CODE_PERMISSIONS = 123;
    private List<String> phoneNumbers;
    private String patientInfo; // To store the patient info retrieved from SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        paneLayout = findViewById(R.id.pane_layout);
        requestPermissions();

        // Retrieve the assistance message and phone numbers passed from AssistanceActivity
        assistanceMessage = getIntent().getStringExtra("button_text");
        phoneNumbers = getIntent().getStringArrayListExtra("phone_numbers");

        // Load patient info from SharedPreferences
        loadPatientInfoFromSharedPreferences();

        // Set up buttons for selecting affected area
        Button button1 = findViewById(R.id.button1);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        Button button4 = findViewById(R.id.button4);
        Button button5 = findViewById(R.id.button5);

        View.OnClickListener buttonClickListener = v -> {
            if (v.getId() == R.id.button1) {
                affectedArea = "head";
            } else if (v.getId() == R.id.button2) {
                affectedArea = "torso";
            } else if (v.getId() == R.id.button3) {
                affectedArea = "right arm";
            } else if (v.getId() == R.id.button4) {
                affectedArea = "left arm";
            } else if (v.getId() == R.id.button5) {
                affectedArea = "legs";
            }
            showPane();
        };

        button1.setOnClickListener(buttonClickListener);
        button2.setOnClickListener(buttonClickListener);
        button3.setOnClickListener(buttonClickListener);
        button4.setOnClickListener(buttonClickListener);
        button5.setOnClickListener(buttonClickListener);
    }

    private void showPane() {
        View pane = getLayoutInflater().inflate(R.layout.pane_buttons, paneLayout, false);
        paneLayout.removeAllViews();
        paneLayout.addView(pane);
        paneLayout.setVisibility(View.VISIBLE);

        setUpPaneButtons(pane);
    }

    private void setUpPaneButtons(View pane) {
        Button paneButton1 = pane.findViewById(R.id.pane_button1);
        Button paneButton2 = pane.findViewById(R.id.pane_button2);
        Button paneButton3 = pane.findViewById(R.id.pane_button3);
        Button paneButton4 = pane.findViewById(R.id.pane_button4);
        Button paneButton5 = pane.findViewById(R.id.pane_button5);
        Button paneButton6 = pane.findViewById(R.id.pane_button6);

        paneButton1.setOnClickListener(v -> sendMessageAndOpenNext("blunt force"));
        paneButton2.setOnClickListener(v -> sendMessageAndOpenNext("burns"));
        paneButton3.setOnClickListener(v -> sendMessageAndOpenNext("chemical burns"));
        paneButton4.setOnClickListener(v -> sendMessageAndOpenNext("cuts"));
        paneButton5.setOnClickListener(v -> sendMessageAndOpenNext("object piercing"));
        paneButton6.setOnClickListener(v -> sendMessageAndOpenNext("unconscious"));
    }

    private void sendMessageAndOpenNext(String injuryType) {
        String message;

        // Construct the SMS message with the required format
        if (injuryType.equals("unconscious")) {
            message = "Patient is at " + assistanceMessage + ", experiencing unconsciousness.";
        } else {
            message = "Patient is at " + assistanceMessage + ", experiencing " + injuryType +
                    " to their " + affectedArea + ".";
        }

        sendSms(message);
        openNextActivity();
    }

    private void sendSms(String message) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            SmsManager smsManager = SmsManager.getDefault();
            for (String phoneNumber : phoneNumbers) {
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            }
            Toast.makeText(this, "SMS sent: " + message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "SMS permission is not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void openNextActivity() {
        // Pass both patient info and phone numbers to NextActivity
        Intent intent = new Intent(this, NextActivity.class);
        intent.putExtra("patient_info", patientInfo);  // Passing patient info
        intent.putStringArrayListExtra("phoneNumbers", new ArrayList<>(phoneNumbers));  // Passing phone numbers
        startActivity(intent);
    }

    private void loadPatientInfoFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        // Fetching patient info from SharedPreferences
        String givenName = sharedPreferences.getString("givenName", "");
        String currentNonPrescription = sharedPreferences.getString("currentNonPrescription", "");
        String gender = sharedPreferences.getString("gender", "");
        String chronicIllnesses = sharedPreferences.getString("chronicIllnesses", "");
        String dateOfBirth = sharedPreferences.getString("dateOfBirth", "");
        String otherDiagnosedConditions = sharedPreferences.getString("otherDiagnosedConditions", "");
        String bloodType = sharedPreferences.getString("bloodType", "");
        String recentTreatments = sharedPreferences.getString("recentTreatments", "");
        String gradeAndSection = sharedPreferences.getString("gradeAndSection", "");
        String advisor = sharedPreferences.getString("advisor", "");
        String hereditaryFamilyHistory = sharedPreferences.getString("hereditaryFamilyHistory", "");
        String allergies = sharedPreferences.getString("allergies", "");
        String currentPrescription = sharedPreferences.getString("currentPrescription", "");

        // Combining all retrieved values into a single string
        patientInfo = "Patient Info:\n" +
                "Name: " + givenName + "\n" +
                "Gender: " + gender + "\n" +
                "Date of Birth: " + dateOfBirth + "\n" +
                "Grade & Section: " + gradeAndSection + "\n" +
                "Advisor: " + advisor + "\n" +
                "Blood Type: " + bloodType + "\n" +
                "Allergies: " + allergies + "\n" +
                "Chronic Illnesses: " + chronicIllnesses + "\n" +
                "Hereditary/Family History: " + hereditaryFamilyHistory + "\n" +
                "Other Conditions: " + otherDiagnosedConditions + "\n" +
                "Current Prescription: " + currentPrescription + "\n" +
                "Current Non Prescription: " + currentNonPrescription + "\n" +
                "Recent Treatments: " + recentTreatments;
    }

    private void requestPermissions() {
        if (!hasPermissions()) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.SEND_SMS
            }, REQUEST_CODE_PERMISSIONS);
        }
    }

    private boolean hasPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }
}
