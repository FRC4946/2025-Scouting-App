package com.example.a2024scoutingapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

public class InvertedTeleopActivity extends AppCompatActivity {

    private RelativeLayout mainLayout;
    private ScoutingForm m_currentForm;
    private int x, y;
    private Button invertedTeleopExitButton;
    //yes
    private Button teleopDefenceButton, passedButton;
    private CheckBox disabledCheckBox, deleteModeCheckBox;
    // This makes the code readable but idk if this breaks something so be advised
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inverted_teleop);
        mainLayout = findViewById(R.id.inverted_teleop);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        teleopDefenceButton = findViewById(R.id.defencemode);
        passedButton = findViewById(R.id.passed2);
        disabledCheckBox = findViewById(R.id.disabledCheckbox2);
        deleteModeCheckBox = findViewById(R.id.deletemodepassed2);
        disabledCheckBox.setChecked(m_currentForm.disabled);


        System.out.println("Team number: " + m_currentForm.teamNumber);

        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x = (int) event.getX();
                y = (int) event.getY();

                showCoords();

                if (x <= 400 && m_currentForm.team != Constants.Team.RED) {
                    // If the team is red, they can't score in the blue area
                    if (43 <= x && x <= 285 && y >= 15 && y <= 160) {
                        // Checks if in amp area to score in it.
                        updateCoords();
                        Intent intent = new Intent(InvertedTeleopActivity.this, TeleopPopupAmp.class);
                        intent.putExtra("SCOUTING_FORM", m_currentForm);
                        startActivity(intent);
                    } else {
                        updateCoords();
                        Intent intent = new Intent(InvertedTeleopActivity.this, TeleopPopup.class);
                        intent.putExtra("SCOUTING_FORM", m_currentForm);
                        startActivity(intent);
                    }
                } else if (x >= 625 && m_currentForm.team != Constants.Team.BLUE) {
                    // If the team is blue, they can't score in the red area
                    if (x <= 980 && x >= 740 && y >= 15 && y <= 125) {
                        // Checks if in amp area to score in it.
                        updateCoords();
                        Intent intent = new Intent(InvertedTeleopActivity.this, TeleopPopupAmp.class);
                        intent.putExtra("SCOUTING_FORM", m_currentForm);
                        startActivity(intent);
                    } else {
                        updateCoords();
                        Intent intent = new Intent(InvertedTeleopActivity.this, TeleopPopup.class);
                        intent.putExtra("SCOUTING_FORM", m_currentForm);
                        startActivity(intent);
                    }
                } //else {
//                    new AlertDialog.Builder(InvertedTeleopActivity.this)
//                            .setTitle("Error")
//                            .setMessage("You are not allowed to score in this area")
//                            .setPositiveButton("OK", null)
//                            .show();
//                }
                return true;
            }
        });
//yess
        invertedTeleopExitButton = findViewById(R.id.inverted_teleop_exit_screen);
        invertedTeleopExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvertedTeleopActivity.this, MatchActivity.class);
                m_currentForm.disabled = disabledCheckBox.isChecked();
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        teleopDefenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvertedTeleopActivity.this, DefencePopup.class);
                m_currentForm.disabled = disabledCheckBox.isChecked();
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        passedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteModeCheckBox.isChecked()) {
                    m_currentForm.passed = Math.max(m_currentForm.passed - 1, 0);
                } else {
                    m_currentForm.passed++;
                }
            }
        });
        
    }
    public void showCoords() {
        new AlertDialog.Builder(this)
                .setTitle("Coordinates")
                .setMessage("X: " + x + " Y: " + y)
                .setPositiveButton("OK", null)
                .show();
    }

    public void updateCoords() {
        m_currentForm.tempCoords = "ඞ" + x + "%" + y;
    }
}