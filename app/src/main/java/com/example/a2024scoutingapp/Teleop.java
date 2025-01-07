package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.util.Locale;

public class Teleop extends AppCompatActivity {
    // TODO: redo like most of this
    private ScoutingForm m_currentForm;
    private Button m_exit, teleopL4, teleopL3, teleopL2, teleopL1, teleopProcessor, teleopVet;
    private CheckBox deleteMode, crossedLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teleop);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");

        teleopL4.setText("L4 Coral" + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL4Coral)));;
        teleopL4 = findViewById(R.id.teleopL4Coral);
        teleopL3.setText("L3 Coral" + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL3Coral)));;
        teleopL3 = findViewById(R.id.teleopL3Coral);
        teleopL2.setText("L2 Coral" + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL2Coral)));;
        teleopL2 = findViewById(R.id.teleopL2Coral);
        teleopL1.setText("L1 Coral" + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL1Coral)));;
        teleopL1 = findViewById(R.id.teleopL1Coral);
        teleopVet = findViewById(R.id.teleopVet);
        teleopVet.setText("Algae Vet" + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopVet)));;
        teleopProcessor = findViewById(R.id.teleopProcessor);
        teleopProcessor.setText("Algae Processor" + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopProcessor)));;


        speakerScored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.autoSpeaker = Math.max(m_currentForm.autoSpeaker - 1, 0);
                } else {
                    m_currentForm.autoSpeaker++;
                }
                speakerScored.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoSpeaker)));;
            }
        });
        missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean AddPoints = deleteMode.isChecked();
                if (AddPoints) {
                    m_currentForm.autoMissed = Math.max(m_currentForm.autoMissed - 1, 0);
                } else {
                    m_currentForm.autoMissed++;
                }
                missed.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoMissed)));;
            }
        });
        ampScored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.autoAmp = Math.max(m_currentForm.autoAmp - 1, 0);
                } else {
                    m_currentForm.autoAmp++;
                }
                ampScored.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoAmp)));;
            }
        });
        m_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teleop.this, MatchActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
    }
}
