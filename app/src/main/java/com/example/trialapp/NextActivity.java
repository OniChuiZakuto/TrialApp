package com.example.trialapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NextActivity extends AppCompatActivity {

    // Declare only the necessary fields
    private String givenName, gender, dateOfBirth, gradeAndSection, advisor, bloodType,
            allergies, hereditaryHistory, chronicIllnesses, otherConditions, currentPrescription, recentTreatments;
    private ArrayList<String> phoneNumbers; // List to hold phone numbers passed from DetailsActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        Button victimButton = findViewById(R.id.victimButton);
        Button handlerButton = findViewById(R.id.handlerButton);

        // Initialize TextViews
        TextView givenNameTextView = findViewById(R.id.givenNameTextView);
        TextView genderTextView = findViewById(R.id.genderTextView);
        TextView dateOfBirthTextView = findViewById(R.id.dateOfBirthTextView);
        TextView gradeAndSectionTextView = findViewById(R.id.gradeAndSectionTextView);
        TextView advisorTextView = findViewById(R.id.advisorTextView);
        TextView bloodTypeTextView = findViewById(R.id.bloodTypeTextView);
        TextView allergiesTextView = findViewById(R.id.allergiesTextView);
        TextView hereditaryHistoryTextView = findViewById(R.id.hereditaryFamilyHistoryTextView);
        TextView chronicIllnessesTextView = findViewById(R.id.chronicIllnessesTextView);
        TextView otherConditionsTextView = findViewById(R.id.otherDiagnosedConditionsTextView);
        TextView currentPrescriptionTextView = findViewById(R.id.currentPrescriptionTextView);
        TextView recentTreatmentsTextView = findViewById(R.id.recentTreatmentsTextView);

        // Retrieve patient info from SharedPreferences and save in local variables
        SharedPreferences sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        givenName = sharedPreferences.getString("givenName", null);
        gender = sharedPreferences.getString("gender", null);
        dateOfBirth = sharedPreferences.getString("dateOfBirth", null);
        gradeAndSection = sharedPreferences.getString("gradeAndSection", null);
        advisor = sharedPreferences.getString("advisor", null);
        bloodType = sharedPreferences.getString("bloodType", null);
        allergies = sharedPreferences.getString("allergies", null);
        hereditaryHistory = sharedPreferences.getString("hereditaryFamilyHistory", null);
        chronicIllnesses = sharedPreferences.getString("chronicIllnesses", null);
        otherConditions = sharedPreferences.getString("otherDiagnosedConditions", null);
        currentPrescription = sharedPreferences.getString("currentPrescription", null);
        recentTreatments = sharedPreferences.getString("recentTreatments", null);

        // Check if the patient info is available and display a toast message
        if (givenName != null && gender != null && dateOfBirth != null && gradeAndSection != null && advisor != null) {
            Toast.makeText(this, "Patient information successfully loaded from SharedPreferences.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Patient information not found in SharedPreferences.", Toast.LENGTH_SHORT).show();
        }

        // Populate TextViews with retrieved patient data
        givenNameTextView.setText(givenName != null ? givenName : "N/A");
        genderTextView.setText(gender != null ? gender : "N/A");
        dateOfBirthTextView.setText(dateOfBirth != null ? dateOfBirth : "N/A");
        gradeAndSectionTextView.setText(gradeAndSection != null ? gradeAndSection : "N/A");
        advisorTextView.setText(advisor != null ? advisor : "N/A");
        bloodTypeTextView.setText(bloodType != null ? bloodType : "N/A");
        allergiesTextView.setText(allergies != null ? allergies : "N/A");
        hereditaryHistoryTextView.setText(hereditaryHistory != null ? hereditaryHistory : "N/A");
        chronicIllnessesTextView.setText(chronicIllnesses != null ? chronicIllnesses : "N/A");
        otherConditionsTextView.setText(otherConditions != null ? otherConditions : "N/A");
        currentPrescriptionTextView.setText(currentPrescription != null ? currentPrescription : "N/A");
        recentTreatmentsTextView.setText(recentTreatments != null ? recentTreatments : "N/A");

        // Retrieve phone numbers passed from DetailsActivity via Intent
        Intent intent = getIntent();
        phoneNumbers = intent.getStringArrayListExtra("phoneNumbers");

        // Define click listener for the victim button
        victimButton.setOnClickListener(v -> {
            // Create an Intent to start AccountActivity
            Intent accountActivityIntent = new Intent(NextActivity.this, AccountActivity.class);

            // Pass the phone numbers to AccountActivity
            accountActivityIntent.putStringArrayListExtra("phoneNumbers", phoneNumbers);

            // Start AccountActivity
            startActivity(accountActivityIntent);
            finish(); // Optionally, finish the current activity
        });


        // Define click listener for the handler button (unchanged)
        handlerButton.setOnClickListener(v -> {
            // Start a different activity (can be customized as per your requirement)
            Intent intent1 = new Intent(NextActivity.this, AnotherActivity.class);
            startActivity(intent1);
        });
    }

    // Method to send SMS with patient details
    private void sendSms(String message) {
        SmsManager smsManager = SmsManager.getDefault();
        if (phoneNumbers != null && !phoneNumbers.isEmpty()) {
            for (String phoneNumber : phoneNumbers) {
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            }
            Toast.makeText(this, "SMS sent to the provided numbers.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No phone numbers available.", Toast.LENGTH_SHORT).show();
        }
    }

    // Example usage of sendSms method
    private void sendPatientDetails() {
        // Ensure that null values are handled and replaced with a default value
        givenName = givenName != null ? givenName : "N/A";
        gender = gender != null ? gender : "N/A";
        dateOfBirth = dateOfBirth != null ? dateOfBirth : "N/A";
        gradeAndSection = gradeAndSection != null ? gradeAndSection : "N/A";
        advisor = advisor != null ? advisor : "N/A";
        bloodType = bloodType != null ? bloodType : "N/A";
        allergies = allergies != null ? allergies : "N/A";
        hereditaryHistory = hereditaryHistory != null ? hereditaryHistory : "N/A";
        chronicIllnesses = chronicIllnesses != null ? chronicIllnesses : "N/A";
        otherConditions = otherConditions != null ? otherConditions : "N/A";
        currentPrescription = currentPrescription != null ? currentPrescription : "N/A";
        recentTreatments = recentTreatments != null ? recentTreatments : "N/A";

        // Construct the message using locally saved data
        String message = "BERT APP PATIENT HISTORY\n" +
                "Given Name: " + givenName + "\n" +
                "Gender: " + gender + "\n" +
                "Date of Birth: " + dateOfBirth + "\n" +
                "Grade and Section: " + gradeAndSection + "\n" +
                "Advisor: " + advisor + "\n" +
                "Blood Type: " + bloodType + "\n" +
                "Allergies: " + allergies + "\n" +
                "Hereditary or Family History: " + hereditaryHistory + "\n" +
                "Chronic Illnesses: " + chronicIllnesses + "\n" +
                "Other Conditions: " + otherConditions + "\n" +
                "Current Prescription: " + currentPrescription + "\n" +
                "Recent Treatments: " + recentTreatments;

        // Send the SMS using the locally saved data
        sendSms(message);
    }
}
