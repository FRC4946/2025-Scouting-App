package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.util.Locale;

public class Auto extends AppCompatActivity {
    // TODO: redo like most of this
    private ScoutingForm m_currentForm;
    private Button m_exit, autoL4, autoL3, autoL2, autoL1, autoProcessor, autoNet, teleop, send;
    private ToggleButton deleteMode, disabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");

        autoL4.setText("L4 Coral" + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL4Coral)));;
        autoL4 = findViewById(R.id.autoL4Coral);
        autoL3.setText("L3 Coral" + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL3Coral)));;
        autoL3 = findViewById(R.id.autoL3Coral);
        autoL2.setText("L2 Coral" + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL2Coral)));;
        autoL2 = findViewById(R.id.autoL2Coral);
        autoL1.setText("L1 Coral" + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL1Coral)));;
        autoL1 = findViewById(R.id.autoL1Coral);
        autoNet = findViewById(R.id.autoNet);
        autoNet.setText("Algae Net" + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoNet)));;
        autoProcessor = findViewById(R.id.autoProcessor);
        autoProcessor.setText("Algae Processor" + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoProcessor)));
        auto = findViewById(R.id.auto);
        send = findViewById(R.id.send);
        disabled = findViewById(R.id.disabled);
        deleteMode = findViewById(R.id.deleteMode);



        autoL4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.autoL4Coral = Math.max(m_currentForm.autoL4Coral - 1, 0);
                } else {
                    m_currentForm.autoL4Coral++;
                }
                autoL4.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL4Coral)));;
            }
        });
        autoL3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.autoL3Coral = Math.max(m_currentForm.autoL3Coral - 1, 0);
                } else {
                    m_currentForm.autoL3Coral++;
                }
                autoL3.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL3Coral)));;
            }
        });
        autoL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.autoL2Coral = Math.max(m_currentForm.autoL2Coral - 1, 0);
                } else {
                    m_currentForm.autoL2Coral++;
                }
                autoL2.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL2Coral)));;
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
                autoL1.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL1Coral)));;
            }
        });
        m_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(auto.this, MatchActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(auto.this, Auto.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(auto.this, SendMessageActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
    }
}
