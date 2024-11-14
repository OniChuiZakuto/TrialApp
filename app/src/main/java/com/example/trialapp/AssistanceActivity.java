package com.example.trialapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class AssistanceActivity extends AppCompatActivity {

    private ArrayList<String> retrievedPhoneNumbers = new ArrayList<>();

    private static final int[] BUTTON_IDS = {
            R.id.button1, R.id.button2, R.id.button3, R.id.button4,
            R.id.button5, R.id.button6, R.id.button7, R.id.button8,
            R.id.button9, R.id.button10, R.id.button11, R.id.button12,
            R.id.button13, R.id.button14, R.id.button15, R.id.button16,
            R.id.button17, R.id.button18, R.id.button19, R.id.button20,
            R.id.button21, R.id.button22, R.id.button23, R.id.button24
    };

    private static final String[] BUTTON_MESSAGES = {
            "G2", "G2", "B3L2D3", "B3L2D4", "G2", "G2", "B3L1D1", "B3L1D2",
            "B2L2D4", "B2L1D2", "O2", "O2", "B2L2D3", "B2L1D1", "O1", "O1",
            "G1", "G1", "B1L1D1", "B1L1D2", "B1L2D3", "B1L2D4", "G1", "G1"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistance);

        // Retrieve phone numbers from the intent
        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<String> phoneNumbers = intent.getStringArrayListExtra("phone_numbers");
            if (phoneNumbers != null) {
                retrievedPhoneNumbers = phoneNumbers;
                Log.d("AssistanceActivity", "Retrieved phone numbers: " + retrievedPhoneNumbers);
            } else {
                Toast.makeText(this, "No phone numbers received.", Toast.LENGTH_SHORT).show();
            }
        }

        // Set up all buttons to open DetailsActivity with the button message and phone numbers
        for (int i = 0; i < BUTTON_IDS.length; i++) {
            setupButton(BUTTON_IDS[i], BUTTON_MESSAGES[i]);
        }
    }

    private void setupButton(int buttonId, String message) {
        Button button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> {
                // Open DetailsActivity with the button message and phone numbers
                Intent detailsIntent = new Intent(AssistanceActivity.this, DetailsActivity.class);
                detailsIntent.putExtra("button_text", message);
                detailsIntent.putStringArrayListExtra("phone_numbers", retrievedPhoneNumbers);
                startActivity(detailsIntent);
            });
        } else {
            Log.e("AssistanceActivity", "Button with ID " + buttonId + " not found");
        }
    }
}
