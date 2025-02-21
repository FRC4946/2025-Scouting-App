package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class Auto extends AppCompatActivity {
    private static final String TAG = "AutoActivity";
    private static final String DIRECTORY_NAME = "Logs";

    private ScoutingForm m_currentForm;
    private Button autoL4, autoL3, autoL2, autoL1, autoProcessor, autoNet, endgame, teleop, send, exit, main;
    private CheckBox deleteMode, disabled;
    private TextView matchNum, teamNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");

        autoL4 = findViewById(R.id.autoL4Coral);
        autoL4.setText("L4 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL4Coral)));
        autoL3 = findViewById(R.id.autoL3Coral);
        autoL3.setText("L3 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL3Coral)));
        autoL2 = findViewById(R.id.autoL2Coral);
        autoL2.setText("L2 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL2Coral)));
        autoL1 = findViewById(R.id.autoL1Coral);
        autoL1.setText("L1 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL1Coral)));
        autoNet = findViewById(R.id.autoNet);
        autoNet.setText("Algae Net: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoNet)));
        autoProcessor = findViewById(R.id.autoProcessor);
        autoProcessor.setText("Algae Processor: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoProcessor)));
        matchNum = findViewById(R.id.matchNum);
        matchNum.setText("Match: " + m_currentForm.matchNumber);
        teamNum = findViewById(R.id.teamNum);
        teamNum.setText("Team: " + m_currentForm.teamNumber);
        teleop = findViewById(R.id.teleop);
        main = findViewById(R.id.main);
        endgame = findViewById(R.id.endgame);
        send = findViewById(R.id.exitbutton);
        disabled = findViewById(R.id.disabled);
        deleteMode = findViewById(R.id.delete);
        disabled.setChecked(m_currentForm.disabled);
        Button[] Buttons = {autoL4, autoL3, autoL2, autoL1, autoProcessor, autoNet};
        for (int i = 0; i < Buttons.length; i++) {
            if (m_currentForm.team.equals("RED")) {
                Buttons[i].setBackgroundResource(R.drawable.redbutton);
            } else {
                Buttons[i].setBackgroundResource(R.drawable.bluebutton);
            }
        }

        autoL4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.autoL4Coral = Math.max(m_currentForm.autoL4Coral - 1, 0);
                } else {
                    m_currentForm.autoL4Coral = Math.min(m_currentForm.autoL4Coral+1, 12);
                }
                autoL4.setText("L4 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL4Coral)));
            }
        });

        autoL3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.autoL3Coral = Math.max(m_currentForm.autoL3Coral - 1, 0);
                } else {
                    m_currentForm.autoL3Coral = Math.min(m_currentForm.autoL3Coral+1, 12);
                }
                autoL3.setText("L3 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL3Coral)));
            }
        });

        autoL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.autoL2Coral = Math.max(m_currentForm.autoL2Coral - 1, 0);
                } else {
                    m_currentForm.autoL2Coral = Math.min(m_currentForm.autoL2Coral+1, 12);
                }
                autoL2.setText("L2 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL2Coral)));
            }
        });

        autoL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.autoL1Coral = Math.max(m_currentForm.autoL1Coral - 1, 0);
                } else {
                    m_currentForm.autoL1Coral++;
                }
                autoL1.setText("L1 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL1Coral)));
            }
        });

        autoNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.autoNet = Math.max(m_currentForm.autoNet - 1, 0);
                } else {
                    m_currentForm.autoNet = Math.min(m_currentForm.autoNet+1, 18);
                }
                autoNet.setText("Algae Net: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoNet)));
            }
        });

        autoProcessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.autoProcessor = Math.max(m_currentForm.autoProcessor - 1, 0);
                } else {
                    m_currentForm.autoProcessor++;
                }
                autoProcessor.setText("Algae Processor: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoProcessor)));
            }
        });
        endgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.disabled = disabled.isChecked();
                Intent intent = new Intent(Auto.this, Endgame.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });

        teleop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.disabled = disabled.isChecked();
                Intent intent = new Intent(Auto.this, Teleop.class);
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
                Intent intent = new Intent(Auto.this, MainActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.disabled = disabled.isChecked();
                if (!MainActivity.loaded){
                    m_currentForm.matchNumber--;
                }
                Intent intent = new Intent(Auto.this, MatchActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
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
