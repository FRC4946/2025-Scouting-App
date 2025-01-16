package com.example.a2024scoutingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ScoutingForm m_currentForm = new ScoutingForm();
    private String m_loadName;
    private Button start;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        // Initialize UI components
        start = findViewById(R.id.start);
        if (start == null) {
            Log.e(TAG, "Button 'start' not found in layout");
            return;
        }

        // Handle incoming Intent
        Intent intent = getIntent();
        if (intent != null) {
            m_loadName = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (intent.hasExtra("SCOUTING_FORM")) {
                m_currentForm = (ScoutingForm) intent.getSerializableExtra("SCOUTING_FORM");
                Log.d(TAG, "Received ScoutingForm from intent: " + m_currentForm.toString());
            } else {
                Log.d(TAG, "No ScoutingForm in intent, initializing a new form.");
                m_currentForm = new ScoutingForm();
            }
        } else {
            Log.e(TAG, "Intent is null. Initializing with default values.");
        }

        // Set OnClickListener for start button
        start.setOnClickListener(v -> {
            Log.d(TAG, "Start button clicked, starting MatchActivity.");
            m_currentForm = new ScoutingForm(); // Reset form

            // Launch MatchActivity
            Intent matchIntent = new Intent(MainActivity.this, MatchActivity.class);
            matchIntent.putExtra("SCOUTING_FORM", m_currentForm);
            startActivity(matchIntent);
        });
    }
}
