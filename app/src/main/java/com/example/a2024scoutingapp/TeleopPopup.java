package com.example.a2024scoutingapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


import androidx.appcompat.app.AppCompatActivity;


import com.example.a2024scoutingapp.forms.ScoutingForm;


import java.util.Locale;


public class TeleopPopup extends AppCompatActivity {
    private ScoutingForm m_currentForm;
    private Button speakerScored, missed, m_exit;
    private CheckBox deleteMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teleop_popup);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        speakerScored = findViewById(R.id.speakerScored2);
        missed = findViewById(R.id.missed2);
        deleteMode = findViewById(R.id.deleteMode2);
        m_exit = findViewById(R.id.teleop2ExitButton);
        System.out.println(m_currentForm.team);
        missed.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.missed)));
        speakerScored.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.speaker)));

        missed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.missed = Math.max(m_currentForm.missed - 1, 0);
                    m_currentForm.allCoords = m_currentForm.allCoords.substring(0, m_currentForm.allCoords.lastIndexOf("ඞ") == -1 ? 0 : m_currentForm.allCoords.lastIndexOf("ඞ"));
                } else {
                    m_currentForm.missed++;
                    m_currentForm.allCoords += m_currentForm.tempCoords;
                    m_currentForm.tempCoords = "";
                }
                missed.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.missed)));
            }
        });
        speakerScored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteMode.isChecked()) {
                    m_currentForm.speaker = Math.max(m_currentForm.speaker - 1, 0);
                    m_currentForm.allCoords = m_currentForm.allCoords.substring(0, m_currentForm.allCoords.lastIndexOf("ඞ") == -1 ? 0 : m_currentForm.allCoords.lastIndexOf("ඞ"));
                } else {
                    m_currentForm.speaker++;
                    m_currentForm.allCoords += m_currentForm.tempCoords;
                    m_currentForm.tempCoords = "";


                }
                speakerScored.setText(String.format(Locale.getDefault(), Integer.toString(m_currentForm.speaker)));
            }
        });
        m_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (m_currentForm.isInverted) {
                    Intent intent = new Intent(TeleopPopup.this, InvertedTeleopActivity.class);
                    intent.putExtra("SCOUTING_FORM", m_currentForm);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(TeleopPopup.this, TeleopActivity.class);
                    intent.putExtra("SCOUTING_FORM", m_currentForm);
                    startActivity(intent);
                }
            }
        });
    }
}



