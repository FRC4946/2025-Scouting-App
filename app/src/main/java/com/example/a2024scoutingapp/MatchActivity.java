package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.a2024scoutingapp.MainActivity.MAIN_DIRECTORY_NAME;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;


public class MatchActivity extends AppCompatActivity {
    private ScoutingForm m_currentForm;
    public static File loadedFile;
    private static final String TAG = "MatchActivity";
    private EditText m_teamNumber, m_matchNumber, m_scoutName;
    private Button red, blue, m_sendButton, load;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
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
        InputFilter letterFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                // Loop through the characters and ensure they're letters
                for (int i = start; i < end; i++) {
                    if (!Character.isLetter(source.charAt(i))) {
                        return ""; // Block non-letter input
                    }
                }
                return null; // Accept valid input
            }
        };
        m_scoutName.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(10) // Limit to 10 characters
        });
        m_scoutName.setFilters(new InputFilter[]{letterFilter});
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.team = "RED";
                if (!checkSave()) {
                    return;
                }
                if (MainActivity.loaded) {
                    swapLoadFile();
                }
                Intent intent = new Intent(MatchActivity.this, Auto.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.team = "BLUE";
                if (!checkSave()) {
                    return;
                }
                if (MainActivity.loaded) {
                    swapLoadFile();
                }
                Intent intent = new Intent(MatchActivity.this, Auto.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        m_sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchActivity.this);
                builder.setTitle("WARNING")
                        .setMessage("Do you want to save?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            if (!checkSave()) {
                                return;
                            }
                            if (MainActivity.loaded) {
                                swapLoadFile();
                            } else {
                                saveFormToFile();
                                m_currentForm.matchNumber++;
                            }
                            Intent intent = new Intent(MatchActivity.this, SendMessageActivity.class);
                            intent.putExtra("SCOUTING_FORM", m_currentForm);
                            startActivity(intent);
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            quickSave();
                            Intent intent = new Intent(MatchActivity.this, SendMessageActivity.class);
                            intent.putExtra("SCOUTING_FORM", m_currentForm);
                            startActivity(intent);
                        });
                builder.create().show();
            }
        });
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MatchActivity.this);
                builder.setTitle("WARNING")
                        .setMessage("Do you want to save?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            if (!checkSave()) {
                                return;
                            }
                            if (MainActivity.loaded) {
                                swapLoadFile();
                            } else {
                                saveFormToFile();
                                m_currentForm.matchNumber++;
                            }
                            Intent intent = new Intent(MatchActivity.this, LoadActivity.class);
                            intent.putExtra("SCOUTING_FORM", m_currentForm);
                            startActivity(intent);
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            quickSave();
                            Intent intent = new Intent(MatchActivity.this, LoadActivity.class);
                            intent.putExtra("SCOUTING_FORM", m_currentForm);
                            startActivity(intent);
                        });
                builder.create().show();
            }
        });

    }

    private void swapLoadFile() {
        if (loadedFile.delete()) {
            Log.i(TAG, "Old file deleted: " + loadedFile.getName());
        } else {
            Log.e(TAG, "Failed to delete old file: " + loadedFile.getName());
        }
        saveFormToFile();
    }

    private void quickSave() {
        try {
            // Validate scout name
            String scoutName = m_scoutName.getText().toString().trim().replace(",", "");
            if (!scoutName.isEmpty()) {
                m_currentForm.scoutName = scoutName;
            }

            // Validate and parse team number
            String teamNumberInput = m_teamNumber.getText().toString().trim().replace(",", "");
            if (!teamNumberInput.isEmpty()) {
                m_currentForm.teamNumber = Integer.parseInt(teamNumberInput);
            }

            // Validate and parse match number
            String matchNumberInput = m_matchNumber.getText().toString().trim().replace(",", "");
            if (!matchNumberInput.isEmpty()) {
                m_currentForm.matchNumber = Integer.parseInt(matchNumberInput);
            }
        } catch (Exception e) {

        }
    }

    private boolean checkSave() {
        try {
            // Validate scout name
            String scoutName = m_scoutName.getText().toString().trim().replace(",", "");
            if (scoutName.isEmpty()) {
                Toast.makeText(MatchActivity.this, "Scouting name cannot be empty", Toast.LENGTH_SHORT).show();
                return false; // Stop further execution
            }
            m_currentForm.scoutName = scoutName;

            // Validate and parse team number
            String teamNumberInput = m_teamNumber.getText().toString().trim().replace(",", "");
            if (teamNumberInput.isEmpty()) {
                Toast.makeText(MatchActivity.this, "Team number cannot be empty", Toast.LENGTH_SHORT).show();
                return false;
            }
            m_currentForm.teamNumber = Integer.parseInt(teamNumberInput);

            // Validate and parse match number
            String matchNumberInput = m_matchNumber.getText().toString().trim().replace(",", "");
            if (matchNumberInput.isEmpty()) {
                Toast.makeText(MatchActivity.this, "Match number cannot be empty", Toast.LENGTH_SHORT).show();
                return false;
            }
            m_currentForm.matchNumber = Integer.parseInt(matchNumberInput);
            return true;
        } catch (NumberFormatException e) {
            // Catch invalid number format errors
            Toast.makeText(MatchActivity.this, "Match number or Team number must be valid integers", Toast.LENGTH_SHORT).show();
            return false; // Stop further execution
        }
    }

    private void saveFormToFile() {
        File logsDir = new File(getExternalFilesDir(null), MAIN_DIRECTORY_NAME);
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