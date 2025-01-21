package com.example.a2024scoutingapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;


public class MatchActivity extends AppCompatActivity {
    private ScoutingForm m_currentForm;
    private static final String TAG = "AutoActivity";
    private static final String DIRECTORY_NAME = "Logs";
    private String m_loadName;
    private EditText m_teamNumber, m_matchNumber, m_scoutName;
    private Button red, blue, m_sendButton, load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        m_loadName = intent.getStringExtra(intent.EXTRA_TEXT);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        if (m_currentForm == null) {
            m_currentForm = new ScoutingForm();
        }
        m_scoutName = findViewById(R.id.scoutName);
        m_teamNumber = findViewById(R.id.teamNumber);
        m_matchNumber = findViewById(R.id.matchNumber);
        m_sendButton = findViewById(R.id.exitbutton);
        load = findViewById(R.id.loadButton);
        m_scoutName.setText(m_currentForm.scoutName);
        m_matchNumber.setText("" + (m_currentForm.matchNumber));

        red = findViewById(R.id.red);
        blue = findViewById(R.id.blue);
        m_teamNumber.setText("" + m_currentForm.teamNumber);
        m_matchNumber.setText("" + (m_currentForm.matchNumber));
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.team = Constants.Team.RED;

                try {
                    // Validate scout name
                    String scoutName = m_scoutName.getText().toString().trim();
                    if (scoutName.isEmpty()) {
                        Toast.makeText(MatchActivity.this, "Scouting name cannot be empty", Toast.LENGTH_SHORT).show();
                        return; // Stop further execution
                    }
                    m_currentForm.scoutName = scoutName.replace(",", "");

                    // Validate and parse team number
                    String teamNumberInput = m_teamNumber.getText().toString().trim();
                    if (teamNumberInput.isEmpty()) {
                        Toast.makeText(MatchActivity.this, "Team number cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    m_currentForm.teamNumber = Integer.parseInt(teamNumberInput);

                    // Validate and parse match number
                    String matchNumberInput = m_matchNumber.getText().toString().trim();
                    if (matchNumberInput.isEmpty()) {
                        Toast.makeText(MatchActivity.this, "Match number cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    m_currentForm.matchNumber = Integer.parseInt(matchNumberInput);

                } catch (NumberFormatException e) {
                    // Catch invalid number format errors
                    Toast.makeText(MatchActivity.this, "Match number or Team number must be valid integers", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }
                Intent intent = new Intent(MatchActivity.this, Auto.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);

            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.team = Constants.Team.BLUE;

                try {
                    // Validate scout name
                    String scoutName = m_scoutName.getText().toString().trim().replace(",", "");
                    if (scoutName.isEmpty()) {
                        Toast.makeText(MatchActivity.this, "Scouting name cannot be empty", Toast.LENGTH_SHORT).show();
                        return; // Stop further execution
                    }
                    m_currentForm.scoutName = scoutName;

                    // Validate and parse team number
                    String teamNumberInput = m_teamNumber.getText().toString().trim().replace(",", "");
                    if (teamNumberInput.isEmpty()) {
                        Toast.makeText(MatchActivity.this, "Team number cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    m_currentForm.teamNumber = Integer.parseInt(teamNumberInput);

                    // Validate and parse match number
                    String matchNumberInput = m_matchNumber.getText().toString().trim().replace(",", "");
                    if (matchNumberInput.isEmpty()) {
                        Toast.makeText(MatchActivity.this, "Match number cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    m_currentForm.matchNumber = Integer.parseInt(matchNumberInput);

                } catch (NumberFormatException e) {
                    // Catch invalid number format errors
                    Toast.makeText(MatchActivity.this, "Match number or Team number must be valid integers", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }
                Intent intent = new Intent(MatchActivity.this, Auto.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);

            }
        });
        m_sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchActivity.this, SendMessageActivity.class);
                startActivity(intent);
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String scoutName = m_scoutName.getText().toString().trim().replace(",", "");
                int teamNumber = Integer.parseInt(m_teamNumber.getText().toString().trim());
                int matchNumber = Integer.parseInt(m_matchNumber.getText().toString().trim());
                if (!scoutName.isEmpty() && teamNumber > 0 && matchNumber > 0) {
                    saveFormToFile();
                }
                Intent intent = new Intent(MatchActivity.this, LoadActivity.class);
                startActivity(intent);
            }
        });

    }
    private void saveFormToFile () {
        File logsDir = new File(getExternalFilesDir(null), DIRECTORY_NAME);
        if (!logsDir.exists() && !logsDir.mkdirs()) {
            Log.e(TAG, "Failed to create Logs directory.");
            return;
        }

        File logFile = new File(logsDir, String.format(Locale.getDefault(),
                "match-%d-team-%d.log", m_currentForm.matchNumber, m_currentForm.teamNumber));

        try (FileWriter writer = new FileWriter(logFile)) {
            writer.write(m_currentForm.toString());
            Log.i(TAG, "Form saved successfully: " + logFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Failed to save form", e);
        }
    }

}