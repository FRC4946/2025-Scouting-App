package com.example.a2024scoutingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ScoutingForm m_currentForm = new ScoutingForm();
    private String scoutName;
    int matchNumber = 0;
    public static String MAIN_DIRECTORY_NAME = "Logs";
    public static String BACKUP_DIRECTORY_NAME = "Backups";

    public static boolean loaded = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null) {
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
        if (!loaded) {
            scoutName = m_currentForm.scoutName;
            matchNumber = m_currentForm.matchNumber;
            m_currentForm = new ScoutingForm();
            m_currentForm.scoutName = scoutName;
            m_currentForm.matchNumber = matchNumber + 1;
        }
        Intent matchIntent = new Intent(MainActivity.this, MatchActivity.class);
        matchIntent.putExtra("SCOUTING_FORM", m_currentForm);
        startActivity(matchIntent);
    }
}