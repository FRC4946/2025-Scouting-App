package com.example.a2024scoutingapp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.util.Locale;

public class Auto extends AppCompatActivity {
    // TODO: redo like most of this
    private ScoutingForm m_currentForm;
    private Button  autoL4, autoL3, autoL2, autoL1, autoProcessor, autoNet, teleop, send;
    private CheckBox deleteMode, disabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auto);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        autoL4 = findViewById(R.id.autoL4Coral);
        autoL4.setText("L4 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL4Coral)));;
        autoL3 = findViewById(R.id.autoL3Coral);
        autoL3.setText("L3 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL3Coral)));;
        autoL2 = findViewById(R.id.autoL2Coral);
        autoL2.setText("L2 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL2Coral)));;
        autoL1 = findViewById(R.id.autoL1Coral);
        autoL1.setText("L1 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL1Coral)));;
        autoNet = findViewById(R.id.autoNet);
        autoNet.setText("Algae Net: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoNet)));;
        autoProcessor = findViewById(R.id.autoProcessor);
        autoProcessor.setText("Algae Processor: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoProcessor)));
        teleop = findViewById(R.id.teleop);
        send = findViewById(R.id.send);
        disabled = findViewById(R.id.disabled);
        deleteMode = findViewById(R.id.delete);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        Button[] Buttons = {autoL4, autoL3, autoL2, autoL1, autoProcessor, autoNet};
        for (int i = 0; i < Buttons.length; i++){
            if (m_currentForm.team == Constants.Team.RED){
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
                    m_currentForm.autoL4Coral++;
                }
                autoL4.setText("L4 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL4Coral)));;
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
                autoL3.setText("L3 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL3Coral)));;
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
                autoL2.setText("L2 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL2Coral)));;
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
                autoL1.setText("L1 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.autoL1Coral)));;
            }
        });
        autoNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.autoNet = Math.max(m_currentForm.autoNet - 1, 0);
                } else {
                    m_currentForm.autoNet++;
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
        teleop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Auto.this, Teleop.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_currentForm.disabled = disabled.isChecked();
                        Intent intent = new Intent(Auto.this, MainActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                ActivityCompat.requestPermissions(Auto.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.WRITE_LOG_REQUEST);
                startActivity(intent);
            }
        });
    }
}
