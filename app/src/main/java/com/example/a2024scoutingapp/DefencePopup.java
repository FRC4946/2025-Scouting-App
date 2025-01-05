package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.util.Locale;

public class DefencePopup extends AppCompatActivity {
    private ScoutingForm m_currentForm = new ScoutingForm();
    private ToggleButton m_opponent1, m_opponent2, m_opponent3;
    private Button m_exit, m_resetOpponent1, m_resetOpponent2, m_resetOpponent3;

    private EditText teamnumber1, teamnumber2, teamnumber3;
    private String oppA, oppB, oppC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        setContentView(R.layout.defence_popup);
        m_exit = findViewById(R.id.defenceExitButton);
        m_opponent1 = findViewById(R.id.opponent1);
        m_opponent2 = findViewById(R.id.opponent2);
        m_opponent3 = findViewById(R.id.opponent3);
        m_resetOpponent1 = findViewById(R.id.resetOpponent1);
        m_resetOpponent2 = findViewById(R.id.resetOpponent2);
        m_resetOpponent3 = findViewById(R.id.resetOpponent3);
        teamnumber1 = findViewById(R.id.teamnumber1);
        teamnumber2 = findViewById(R.id.teamnumber2);
        teamnumber3 = findViewById(R.id.teamnumber3);

        oppA = String.format(m_currentForm.opponentA + "");
        oppB = String.format(m_currentForm.opponentB + "");
        oppC = String.format(m_currentForm.opponentC + "");

        teamnumber1.setText(oppA);
        teamnumber2.setText(oppB);
        teamnumber3.setText(oppC);

        m_resetOpponent1.setOnClickListener(v -> {
            m_currentForm.opponentADefenceTime = 0;
        });
        m_resetOpponent2.setOnClickListener(v -> {
            m_currentForm.opponentBDefenceTime = 0;
        });
        m_resetOpponent3.setOnClickListener(v -> {
            m_currentForm.opponentCDefenceTime = 0;
        });
        runTimer();
        m_exit.setOnClickListener(v -> {
            m_currentForm.opponentA = Integer.parseInt(String.valueOf(teamnumber1.getText()));
            m_currentForm.opponentB = Integer.parseInt(String.valueOf(teamnumber2.getText()));
            m_currentForm.opponentC = Integer.parseInt(String.valueOf(teamnumber3.getText()));

            Intent intent = new Intent(DefencePopup.this, MatchActivity.class);
            intent.putExtra("SCOUTING_FORM", m_currentForm);
            startActivity(intent);
        });
    }
    private void runTimer() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (m_opponent1.isChecked())
                    m_currentForm.opponentADefenceTime+=0.1;
                if (m_opponent2.isChecked())
                    m_currentForm.opponentBDefenceTime+=0.1;
                if (m_opponent3.isChecked())
                    m_currentForm.opponentCDefenceTime+=0.1;

                // Format the seconds minutes, and seconds.
                String timerADefenceText = String.format(Locale.getDefault(), "%d:%02d", (int) Math.floor(m_currentForm.opponentADefenceTime / 60), (int) Math.floor(m_currentForm.opponentADefenceTime % 60));
                String timerBDefenceText = String.format(Locale.getDefault(), "%d:%02d", (int) Math.floor(m_currentForm.opponentBDefenceTime / 60), (int) Math.floor(m_currentForm.opponentBDefenceTime) % 60);
                String timerCDefenceText = String.format(Locale.getDefault(), "%d:%02d", (int) Math.floor(m_currentForm.opponentCDefenceTime / 60), (int) Math.floor(m_currentForm.opponentCDefenceTime) % 60);

                m_opponent1.setText(timerADefenceText);
                m_opponent2.setText(timerBDefenceText);
                m_opponent3.setText(timerCDefenceText);

                handler.postDelayed(this, 100);
            }
        });
    }

}
