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

public class TeleopActivity extends AppCompatActivity {

    private RelativeLayout mainLayout2;
    private TextView coordinatesTextView;
    private ScoutingForm m_currentForm;
    static int x, y;
    static private Button teleopExitButton;

    private Button teleopDefenceButton, passedButton;
    private CheckBox disabledCheckBox, deleteModeCheckBox;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");

        setContentView(R.layout.teleop);
        mainLayout2 = findViewById(R.id.teleop);
        teleopExitButton = findViewById(R.id.teleop_exit_screen);
        teleopDefenceButton = findViewById(R.id.defencemode);
        passedButton = findViewById(R.id.passed);
        disabledCheckBox = findViewById(R.id.disabledCheckbox);
        deleteModeCheckBox = findViewById(R.id.deletemodepassed);
        disabledCheckBox.setChecked(m_currentForm.disabled);

        mainLayout2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x  = (int) event.getX();
                y = (int) event.getY();

                showCoords();
//yes
                if (x >=360 && m_currentForm.team != Constants.Team.RED) {
                    // If the team is red, they can't score in the blue area
                    if (740 <= x && x <= 990 && y >= 330 && y <= 521) {
                        // Checks if in amp area to score in it.
                        updateCoords();
                        Intent intent = new Intent(TeleopActivity.this, TeleopPopupAmp.class);
                        intent.putExtra("SCOUTING_FORM", m_currentForm);
                        startActivity(intent);
                    } else {
                        updateCoords();
                        Intent intent = new Intent(TeleopActivity.this, TeleopPopup.class);
                        intent.putExtra("SCOUTING_FORM", m_currentForm);
                        startActivity(intent);
                    }
                } else if (x <= 660 && m_currentForm.team != Constants.Team.BLUE) {
                    // If the team is blue, they can't score in the red area
                    if (x <= 285 && x >= 50 && y >= 330 && y <= 521) {
                        // Checks if in amp area to score in it.
                        updateCoords();
                        Intent intent = new Intent(TeleopActivity.this, TeleopPopupAmp.class);
                        intent.putExtra("SCOUTING_FORM", m_currentForm);
                        startActivity(intent);
                    } else {
                        updateCoords();
                        Intent intent = new Intent(TeleopActivity.this, TeleopPopup.class);
                        intent.putExtra("SCOUTING_FORM", m_currentForm);
                        startActivity(intent);
                    }
                } //else {
//                    new AlertDialog.Builder(TeleopActivity.this)
//                            .setTitle("Error")
//                            .setMessage("You are not allowed to score in this area")
//                            .setPositiveButton("OK", null)
//                            .show();
//                }
                return true;
            }
        });

        teleopExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeleopActivity.this, MatchActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                m_currentForm.disabled = disabledCheckBox.isChecked();
                startActivity(intent);
            }
        });
        teleopDefenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeleopActivity.this, DefencePopup.class);
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
