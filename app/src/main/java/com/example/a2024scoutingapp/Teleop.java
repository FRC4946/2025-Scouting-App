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

public class Teleop extends AppCompatActivity {
    // TODO: redo like most of this
    private ScoutingForm m_currentForm;
    private Button  teleopL4, teleopL3, teleopL2, teleopL1, teleopProcessor, teleopNet, auto, send;
    private CheckBox deleteMode, disabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teleop);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        teleopL4 = findViewById(R.id.teleopL4Coral);
        teleopL4.setText("L4 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL4Coral)));;
        teleopL3 = findViewById(R.id.teleopL3Coral);
        teleopL3.setText("L3 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL3Coral)));;
        teleopL2 = findViewById(R.id.teleopL2Coral);
        teleopL2.setText("L2 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL2Coral)));;
        teleopL1 = findViewById(R.id.teleopL1Coral);
        teleopL1.setText("L1 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL1Coral)));;
        teleopNet = findViewById(R.id.teleopNet);
        teleopNet.setText("Algae Net: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopNet)));;
        teleopProcessor = findViewById(R.id.teleopProcessor);
        teleopProcessor.setText("Algae Processor: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopProcessor)));
        auto = findViewById(R.id.auto);
        send = findViewById(R.id.send);
        disabled = findViewById(R.id.disabled);
        deleteMode = findViewById(R.id.delete);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        Button[] Buttons = {teleopL4, teleopL3, teleopL2, teleopL1, teleopProcessor, teleopNet};
        for (int i = 0; i < Buttons.length; i++){
            if (m_currentForm.team == Constants.Team.RED){
                Buttons[i].setBackgroundResource(R.drawable.redbutton);
            } else {
                Buttons[i].setBackgroundResource(R.drawable.bluebutton);
            }
        }
        teleopL4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.teleopL4Coral = Math.max(m_currentForm.teleopL4Coral - 1, 0);
                } else {
                    m_currentForm.teleopL4Coral++;
                }
                teleopL4.setText("L4 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL4Coral)));;
            }
        });
        teleopL3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.teleopL3Coral = Math.max(m_currentForm.teleopL3Coral - 1, 0);
                } else {
                    m_currentForm.teleopL3Coral++;
                }
                teleopL3.setText("L3 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL3Coral)));;
            }
        });
        teleopL2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.teleopL2Coral = Math.max(m_currentForm.teleopL2Coral - 1, 0);
                } else {
                    m_currentForm.teleopL2Coral++;
                }
                teleopL2.setText("L2 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL2Coral)));;
            }
        });
        teleopL1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.teleopL1Coral = Math.max(m_currentForm.teleopL1Coral - 1, 0);
                } else {
                    m_currentForm.teleopL1Coral++;
                }
                teleopL1.setText("L1 Coral: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopL1Coral)));;
            }
        });
        teleopNet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.teleopNet = Math.max(m_currentForm.teleopNet - 1, 0);
                } else {
                    m_currentForm.teleopNet++;
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
                    m_currentForm.teleopProcessor++;
                }
                teleopProcessor.setText("Algae Processor: " + String.format(Locale.getDefault(), Integer.toString(m_currentForm.teleopProcessor)));
            }
        });
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teleop.this, Auto.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teleop.this, MainActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                ActivityCompat.requestPermissions(Teleop.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.WRITE_LOG_REQUEST);
                startActivity(intent);
            }
        });
    }
}
