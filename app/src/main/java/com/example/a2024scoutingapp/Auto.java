package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.util.Locale;

public class Auto extends AppCompatActivity {
    // TODO: redo like most of this
    private ScoutingForm m_currentForm;
    private Button m_auto, speakerScored, ampScored, missed, m_exit;
    private CheckBox deleteMode, crossedLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        speakerScored = findViewById(R.id.speakerScoredAuto);
        ampScored = findViewById(R.id.ampScoredAuto);
        missed = findViewById(R.id.missedAuto);
        deleteMode = findViewById(R.id.deleteModeAuto);
//        crossedLine = findViewById(R.id.autoTraversal);
        m_exit = findViewById(R.id.exitButtonAuto);
        missed.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoMissed)));;
        speakerScored.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoSpeaker)));;
        ampScored.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoAmp)));;

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
                Intent intent = new Intent(Auto.this, MatchActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
    }
}
