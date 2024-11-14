package com.example.trialapp;
import android.widget.Toast;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class BERTActivity extends AppCompatActivity {
    private final EditText[] contactInputs = new EditText[18];
    private SharedPreferences sharedPreferences;

    private final int[] contactEditTextIDs = {
            R.id.BERTcontact1, R.id.BERTcontact2, R.id.BERTcontact3, R.id.BERTcontact4,
            R.id.BERTcontact5, R.id.BERTcontact6, R.id.BERTcontact7, R.id.BERTcontact8,
            R.id.BERTcontact9, R.id.BERTcontact10, R.id.BERTcontact11, R.id.BERTcontact12,
            R.id.BERTcontact13, R.id.BERTcontact14, R.id.BERTcontact15, R.id.BERTcontact16,
            R.id.BERTcontact17, R.id.BERTcontact18
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bert);

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        // Initialize EditText fields
        for (int i = 0; i < contactInputs.length; i++) {
            contactInputs[i] = findViewById(contactEditTextIDs[i]);
        }

        loadContacts();

        // Handle back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBackButtonPressed(null);
            }
        });
    }

    public void onBackButtonPressed(View view) {
        saveContacts();
        ArrayList<String> phoneNumbers = getContacts();

        if (!phoneNumbers.isEmpty()) {
            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra("phone_numbers", phoneNumbers);
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            // Inform user that no valid phone numbers were entered
            Toast.makeText(this, "Please enter at least one phone number", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveContacts();
    }

    private void saveContacts() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for (int i = 0; i < contactInputs.length; i++) {
            editor.putString("contact" + (i + 1), contactInputs[i].getText().toString());
        }
        editor.apply();
    }

    private void loadContacts() {
        for (int i = 0; i < contactInputs.length; i++) {
            String contact = sharedPreferences.getString("contact" + (i + 1), "");
            contactInputs[i].setText(contact);
        }
    }

    private ArrayList<String> getContacts() {
        ArrayList<String> contacts = new ArrayList<>();
        for (EditText contactInput : contactInputs) {
            String contact = contactInput.getText().toString().trim();
            if (!contact.isEmpty()) {
                contacts.add(contact);
            }
        }
        return contacts;
    }
}
