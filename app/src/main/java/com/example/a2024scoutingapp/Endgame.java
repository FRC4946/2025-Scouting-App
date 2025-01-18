package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class Endgame extends AppCompatActivity {
    private static final String TAG = "EndgameActivity";
    private static final String DIRECTORY_NAME = "Logs";

    private ScoutingForm m_currentForm;
    private SeekBar defenseBar;
    private TextView percentageText;
    private Button teleop, send, main;
    private Button defenseToggle;
    private CheckBox deleteMode, disabled;
    private Button fast, medium, slow, none;
    private int[] climbSpeeds = { 3, 2, 1, 0};
    private Handler handler = new Handler();
    private boolean defenseTimer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endgame);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        defenseBar = findViewById(R.id.defenseBar);
        percentageText = findViewById(R.id.percentageText);
        teleop = findViewById(R.id.teleop);
        main = findViewById(R.id.main);
        defenseToggle = findViewById(R.id.defenseToggle);
        send = findViewById(R.id.exitbutton);
        fast = findViewById(R.id.fastClimb);
        medium = findViewById(R.id.mediumClimb);
        slow = findViewById(R.id.slowClimb);
        none = findViewById(R.id.noClimb);
        defenseBar.setProgress(m_currentForm.defencePercent);
        percentageText.setText(m_currentForm.defencePercent*1.35 + "s");
        if (m_currentForm.defencePercent == 0) {
            defenseToggle.setText("Defense: ON");
        } else {
            defenseToggle.setText("DEFENSE: OFF");
        }
        Button[] climbs = {fast, medium, slow, none};
        int[] colors = {
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.orangeColor),
                getResources().getColor(R.color.redTeam),
                getResources().getColor(R.color.buttonSelectedColor)
        };
        for (int w = 0; w < climbs.length; w++) {
            climbs[w].setBackgroundColor(colors[3]);
        }
        for (int i = 0; i < climbs.length; i++) {
            if (m_currentForm.climbSpeed == climbSpeeds[i]) {
                climbs[i].setBackgroundColor(colors[i]);
            }
        }
        defenseBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                percentageText.setText(progress +"s");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                m_currentForm.defencePercent = (int) (defenseBar.getProgress() / 1.35);
            }
        });
        for (int i = 0; i < climbs.length; i++) {
            int finalI = i;
            climbs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < climbs.length; j++) {
                        climbs[j].setBackgroundColor(colors[3]);
                    }
                    climbs[finalI].setBackgroundColor(colors[finalI]);
                    m_currentForm.climbSpeed = climbSpeeds[finalI];
                }
            });
        }
            teleop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Endgame.this, Teleop.class);
                    intent.putExtra("SCOUTING_FORM", m_currentForm);
                    startActivity(intent);
                }
            });

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveFormToFile();
                    Intent intent = new Intent(Endgame.this, MainActivity.class);
                    intent.putExtra("SCOUTING_FORM", m_currentForm);
                    startActivity(intent);
                }
            });
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Endgame.this, MatchActivity.class);
                    intent.putExtra("SCOUTING_FORM", m_currentForm);
                    startActivity(intent);
                }
            });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (defenseTimer) {
                    // Log the timer running
                    Log.d(TAG, "Timer is running");

                    // Increment the SeekBar's progress
                    int currentProgress = defenseBar.getProgress();
                    currentProgress++;

                    // Ensure progress does not exceed 135
                    currentProgress = Math.min(currentProgress, 135);

                    // Update the TextView and SeekBar
                    percentageText.setText(currentProgress + "s");
                    defenseBar.setProgress(currentProgress);

                    // Repost the Runnable to the handler after 1 second
                    handler.postDelayed(this, 1000);
                }
            }
        };

// Update in onCreate: Start the timer when defenseTimer is true
        defenseToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defenseTimer = !defenseTimer;
                if (defenseTimer) {
                    Log.d(TAG, "Defense Timer ON");
                    defenseToggle.setText("DEFENSE: ON");
                    handler.post(runnable); // Start the timer when toggled on
                } else {
                    Log.d(TAG, "Defense Timer OFF");
                    defenseToggle.setText("DEFENSE: OFF");
                }
            }
        });

    }
    private void saveFormToFile() {
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
