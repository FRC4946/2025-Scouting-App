package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.a2024scoutingapp.MainActivity.MAIN_DIRECTORY_NAME;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class Teleop extends AppCompatActivity {
    private static final String TAG = "TeleopActivity";
    private ScoutingForm m_currentForm;
    private Button teleopL4, main, teleopL3, teleopL2, teleopL1, teleopProcessor, teleopNet, auto, send, endgame;
    private CheckBox deleteMode, disabled;
    private TextView matchNum, teamNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teleop);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        if (m_currentForm == null) {
            m_currentForm = new ScoutingForm();
        }
        teleopL4 = findViewById(R.id.teleopL4Coral);
        teleopL4.setText("L4 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL4Coral)));
        teleopL3 = findViewById(R.id.teleopL3Coral);
        teleopL3.setText("L3 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL3Coral)));
        teleopL2 = findViewById(R.id.teleopL2Coral);
        teleopL2.setText("L2 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL2Coral)));
        teleopL1 = findViewById(R.id.teleopL1Coral);
        teleopL1.setText("L1 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL1Coral)));
        teleopNet = findViewById(R.id.teleopNet);
        teleopNet.setText("Algae Net: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopNet)));
        teleopProcessor = findViewById(R.id.teleopProcessor);
        teleopProcessor.setText("Algae Processor: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopProcessor)));
        matchNum = findViewById(R.id.matchNum);
        matchNum.setText("Match: " + m_currentForm.matchNumber);
        teamNum = findViewById(R.id.teamNum);
        teamNum.setText("Team: " + m_currentForm.teamNumber);
        auto = findViewById(R.id.auto);
        send = findViewById(R.id.exitbutton);
        main = findViewById(R.id.main);
        endgame = findViewById(R.id.endgame);
        disabled = findViewById(R.id.disabled);
        deleteMode = findViewById(R.id.delete);
        disabled.setChecked(m_currentForm.disabled);
        Button[] Buttons = {teleopL4, teleopL3, teleopL2, teleopL1, teleopProcessor, teleopNet};
        for (int i = 0; i < Buttons.length; i++) {
            if (m_currentForm.team.equals("BLUE")) {
                Buttons[i].setBackgroundResource(R.drawable.bluebutton);
            } else {
                Buttons[i].setBackgroundResource(R.drawable.redbutton);
            }
        }
        if (MainActivity.loaded) {
            main.setText("DISCARD CHANGES");
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_currentForm.disabled = disabled.isChecked();
                    MainActivity.loaded = false;
                    Intent intent = new Intent(Teleop.this, MainActivity.class);
                    intent.putExtra("SCOUTING_FORM", m_currentForm);
                    startActivity(intent);
                }
            });
        } else {
            main.setText("EXIT WITHOUT SAVE");
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    m_currentForm.disabled = disabled.isChecked();
                    Intent intent = new Intent(Teleop.this, MatchActivity.class);
                    intent.putExtra("SCOUTING_FORM", m_currentForm);
                    startActivity(intent);
                }
            });
        }
        teleopL4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.teleopL4Coral = Math.max(m_currentForm.teleopL4Coral - 1, 0);
                } else {
                    m_currentForm.teleopL4Coral = Math.min(m_currentForm.teleopL4Coral + 1, 12);
                }
                teleopL4.setText("L4 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL4Coral)));
            }
        });

        teleopL3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.teleopL3Coral = Math.max(m_currentForm.teleopL3Coral - 1, 0);
                } else {
                    m_currentForm.teleopL3Coral = Math.min(m_currentForm.teleopL3Coral + 1, 12);
                }
                teleopL3.setText("L3 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL3Coral)));
            }
        });

        teleopL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.teleopL2Coral = Math.max(m_currentForm.teleopL2Coral - 1, 0);
                } else {
                    m_currentForm.teleopL2Coral = Math.min(m_currentForm.teleopL2Coral + 1, 12);
                }
                teleopL2.setText("L2 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL2Coral)));
            }
        });

        teleopL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.teleopL1Coral = Math.max(m_currentForm.teleopL1Coral - 1, 0);
                } else {
                    m_currentForm.teleopL1Coral = Math.min(m_currentForm.teleopL1Coral + 1, 50);
                }
                teleopL1.setText("L1 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL1Coral)));
            }
        });

        teleopNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.teleopNet = Math.max(m_currentForm.teleopNet - 1, 0);
                } else {
                    m_currentForm.teleopNet = Math.min(m_currentForm.teleopNet + 1, 18);
                }
                teleopNet.setText("Algae Net: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopNet)));
            }
        });

        teleopProcessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.teleopProcessor = Math.max(m_currentForm.teleopProcessor - 1, 0);
                } else {
                    m_currentForm.teleopProcessor = Math.min(m_currentForm.teleopProcessor + 1, 50);
                }
                teleopProcessor.setText("Algae Processor: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopProcessor)));
            }
        });

        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.disabled = disabled.isChecked();
                Intent intent = new Intent(Teleop.this, Auto.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        endgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.disabled = disabled.isChecked();
                Intent intent = new Intent(Teleop.this, Endgame.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.loaded = false;
                m_currentForm.disabled = disabled.isChecked();
                saveFormToFile();
                Intent intent = new Intent(Teleop.this, MainActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
    }

    private void saveFormToFile() {
        File logsDir = new File(getExternalFilesDir(null), MAIN_DIRECTORY_NAME);
        if (!logsDir.exists() && !logsDir.mkdirs()) {
            Log.e(TAG, "Failed to create Logs directory.");
            return;
        }

        File logFile = new File(logsDir, String.format(Locale.getDefault(), "match-%d-team-%d.log", m_currentForm.matchNumber, m_currentForm.teamNumber));

        try (FileWriter writer = new FileWriter(logFile)) {
            writer.write(m_currentForm.toString());
            Log.i(TAG, "Form saved successfully: " + logFile.getAbsolutePath());
        } catch (IOException e) {
            Log.e(TAG, "Failed to save form", e);
        }
    }
}
