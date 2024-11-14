package com.example.trialapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;

public class AccountActivity extends AppCompatActivity {
    private EditText givenNameEditText;
    private EditText currentNonPrescriptionEditText;
    private EditText genderEditText;
    private EditText chronicIllnessesEditText;
    private EditText dateOfBirthEditText;
    private EditText otherDiagnosedConditionsEditText;
    private EditText bloodTypeEditText;
    private EditText recentTreatmentsEditText;
    private EditText gradeAndSectionEditText;
    private EditText advisorEditText;
    private EditText hereditaryFamilyHistoryEditText;
    private EditText allergiesEditText;
    private EditText currentPrescriptionEditText;
    private String[] phoneNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Initialize EditTexts
        givenNameEditText = findViewById(R.id.givenNameEditText);
        currentNonPrescriptionEditText = findViewById(R.id.currentNonPrescriptionEditText);
        genderEditText = findViewById(R.id.genderEditText);
        chronicIllnessesEditText = findViewById(R.id.chronicIllnessesEditText);
        dateOfBirthEditText = findViewById(R.id.dateOfBirthEditText);
        otherDiagnosedConditionsEditText = findViewById(R.id.otherDiagnosedConditionsEditText);
        bloodTypeEditText = findViewById(R.id.bloodTypeEditText);
        recentTreatmentsEditText = findViewById(R.id.recentTreatmentsEditText);
        gradeAndSectionEditText = findViewById(R.id.gradeAndSectionEditText);
        advisorEditText = findViewById(R.id.advisorEditText);
        hereditaryFamilyHistoryEditText = findViewById(R.id.hereditaryFamilyHistoryEditText);
        allergiesEditText = findViewById(R.id.allergiesEditText);
        currentPrescriptionEditText = findViewById(R.id.currentPrescriptionEditText);


        loadData();
        loadPhoneNumbers();
        checkForPhoneNumbersAndNotify();
        sendInitialTextMessage();  // Call the method to send the initial "hello" message

        // Check permissions on launch
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
        }
    }

    private void checkForPhoneNumbersAndNotify() {
        if (phoneNumbers != null && phoneNumbers.length > 0) {
            Toast.makeText(this, "Phone numbers available for SMS.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No phone numbers available for SMS.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackButtonPressed(View view) {
        saveData();
        finish();
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("givenName", givenNameEditText.getText().toString());
        editor.putString("currentNonPrescription", currentNonPrescriptionEditText.getText().toString());
        editor.putString("gender", genderEditText.getText().toString());
        editor.putString("chronicIllnesses", chronicIllnessesEditText.getText().toString());
        editor.putString("dateOfBirth", dateOfBirthEditText.getText().toString());
        editor.putString("otherDiagnosedConditions", otherDiagnosedConditionsEditText.getText().toString());
        editor.putString("bloodType", bloodTypeEditText.getText().toString());
        editor.putString("recentTreatments", recentTreatmentsEditText.getText().toString());
        editor.putString("gradeAndSection", gradeAndSectionEditText.getText().toString());
        editor.putString("advisor", advisorEditText.getText().toString());
        editor.putString("hereditaryFamilyHistory", hereditaryFamilyHistoryEditText.getText().toString());
        editor.putString("allergies", allergiesEditText.getText().toString());
        editor.putString("currentPrescription", currentPrescriptionEditText.getText().toString());
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        givenNameEditText.setText(sharedPreferences.getString("givenName", ""));
        currentNonPrescriptionEditText.setText(sharedPreferences.getString("currentNonPrescription", ""));
        genderEditText.setText(sharedPreferences.getString("gender", ""));
        chronicIllnessesEditText.setText(sharedPreferences.getString("chronicIllnesses", ""));
        dateOfBirthEditText.setText(sharedPreferences.getString("dateOfBirth", ""));
        otherDiagnosedConditionsEditText.setText(sharedPreferences.getString("otherDiagnosedConditions", ""));
        bloodTypeEditText.setText(sharedPreferences.getString("bloodType", ""));
        recentTreatmentsEditText.setText(sharedPreferences.getString("recentTreatments", ""));
        gradeAndSectionEditText.setText(sharedPreferences.getString("gradeAndSection", ""));
        advisorEditText.setText(sharedPreferences.getString("advisor", ""));
        hereditaryFamilyHistoryEditText.setText(sharedPreferences.getString("hereditaryFamilyHistory", ""));
        allergiesEditText.setText(sharedPreferences.getString("allergies", ""));
        currentPrescriptionEditText.setText(sharedPreferences.getString("currentPrescription", ""));
    }

    private void loadPhoneNumbers() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        phoneNumbers = new String[18];
        for (int i = 1; i <= 18; i++) {
            phoneNumbers[i - 1] = sharedPreferences.getString("BERTcontact" + i, "");
        }
    }

    // New method to send an initial "hello" message
    private void sendInitialTextMessage() {
        if (phoneNumbers != null && phoneNumbers.length > 0) {
            SmsManager smsManager = SmsManager.getDefault();
            for (String phoneNumber : phoneNumbers) {
                if (!phoneNumber.isEmpty()) {
                    smsManager.sendTextMessage(phoneNumber, null, "hello", null, null);
                    Toast.makeText(this, "Initial 'hello' message sent to: " + phoneNumber, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
