package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import androidx.appcompat.app.AppCompatActivity;

import pl.droidsonroids.gif.GifImageView;

import static com.example.a2024scoutingapp.MainActivity.MAIN_DIRECTORY_NAME;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class Endgame extends AppCompatActivity {
    private static final String TAG = "EndgameActivity";
    private ScoutingForm m_currentForm;
    private Button teleop, send, main;
    private EditText notes;
    private GifImageView gifs;
    private Button defense0, defense1, defense2, defense3, defense4, defense5;
    private final int[] defensePercentages = {100, 80, 60, 40, 20, 0};
    private Button fast, medium, slow, none;
    private TextView matchNum, teamNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endgame);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        if (m_currentForm == null) {
            m_currentForm = new ScoutingForm();
        }
        teleop = findViewById(R.id.teleop);
        main = findViewById(R.id.main);
        send = findViewById(R.id.exitbutton);
        fast = findViewById(R.id.fastClimb);
        medium = findViewById(R.id.mediumClimb);
        slow = findViewById(R.id.slowClimb);
        none = findViewById(R.id.noClimb);
        notes = findViewById(R.id.notes);
        gifs = findViewById(R.id.gifs);
        defense0 = findViewById(R.id.defense0);
        defense1 = findViewById(R.id.defense1);
        defense2 = findViewById(R.id.defense2);
        defense3 = findViewById(R.id.defense3);
        defense4 = findViewById(R.id.defense4);
        defense5 = findViewById(R.id.defense5);
        matchNum = findViewById(R.id.matchNum);
        matchNum.setText("Match: " + m_currentForm.matchNumber);
        teamNum = findViewById(R.id.teamNum);
        teamNum.setText("Team: " + m_currentForm.teamNumber);

        if (m_currentForm.notes == null || m_currentForm.notes.equals("depression")) {
            notes.setText("Extra Notes");
        } else {
            notes.setText(m_currentForm.notes);
        }
        if (MainActivity.loaded) {
            main.setText("DISCARD CHANGES");
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.loaded = false;
                    Intent intent = new Intent(Endgame.this, MainActivity.class);
                    intent.putExtra("SCOUTING_FORM", m_currentForm);
                    startActivity(intent);
                }
            });
        } else {
            main.setText("EXIT WITHOUT SAVE");
            main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Endgame.this, MatchActivity.class);
                    intent.putExtra("SCOUTING_FORM", m_currentForm);
                    startActivity(intent);
                }
            });
        }
        Button[] percents = {defense5, defense4, defense3, defense2, defense1, defense0};
        int[] percentColors = {
                getResources().getColor(R.color.redTeam),
                getResources().getColor(R.color.orangeColor),
                getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.blueTeam),
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.buttonSelectedColor)
        };


        for (int cry = 0; cry < percents.length; cry++) {
            if (m_currentForm.defencePercent == defensePercentages[cry]) {
                percents[cry].setBackgroundColor(percentColors[cry]);
            } else {
                percents[cry].setBackgroundColor(percentColors[percentColors.length - 1]);
            }
        }


        for (int p = 0; p < percents.length; p++) {
            int finalP = p;
            percents[finalP].setOnClickListener(v -> {
                m_currentForm.defencePercent = defensePercentages[finalP];
                for (int i = 0; i < percents.length; i++) {
                    if (m_currentForm.defencePercent == defensePercentages[i]) {
                        percents[i].setBackgroundColor(percentColors[i]);
                    } else {
                        percents[i].setBackgroundColor(percentColors[percentColors.length - 1]);
                    }
                }
            });
        }


        Button[] climbs = {fast, medium, slow, none};
        int[] climbColors = {
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.orangeColor),
                getResources().getColor(R.color.redTeam),
                getResources().getColor(R.color.purple),
                getResources().getColor(R.color.buttonSelectedColor)
        };


        for (int i = 0; i < climbs.length; i++) {
            climbs[i].setBackgroundColor(climbColors[4]);
        }


        switch (m_currentForm.climbSpeed) {
            case 3:
                fast.setBackgroundColor(climbColors[0]);
                loadGif(R.drawable.twoseconds);
                break;
            case 2:
                medium.setBackgroundColor(climbColors[1]);
                loadGif(R.drawable.fourseconds);
                break;
            case 1:
                slow.setBackgroundColor(climbColors[2]);
                loadGif(R.drawable.eightseconds);
                break;
            case 0:
                none.setBackgroundColor(climbColors[3]);
                loadGif(R.drawable.noclimb);
                break;
        }

        for (int i = 0; i < climbs.length; i++) {
            int finalI = i;
            climbs[i].setOnClickListener(v -> {
                for (int j = 0; j < climbs.length; j++) {
                    climbs[j].setBackgroundColor(climbColors[4]);
                }
                climbs[finalI].setBackgroundColor(climbColors[finalI]);


                switch (finalI) {
                    case 0:
                        m_currentForm.climbSpeed = 3;
                        loadGif(R.drawable.twoseconds);
                        break;
                    case 1:
                        m_currentForm.climbSpeed = 2;
                        loadGif(R.drawable.fourseconds);
                        break;
                    case 2:
                        m_currentForm.climbSpeed = 1;
                        loadGif(R.drawable.eightseconds);
                        break;
                    case 3:
                        m_currentForm.climbSpeed = 0;
                        loadGif(R.drawable.noclimb);
                        break;
                }
            });
        }


        teleop.setOnClickListener(v -> {
            m_currentForm.notes = notes.getText().toString().replace(",", "");
            Intent intent = new Intent(Endgame.this, Teleop.class);
            intent.putExtra("SCOUTING_FORM", m_currentForm);
            startActivity(intent);
        });


        send.setOnClickListener(v -> {
            MainActivity.loaded = false;
            m_currentForm.notes = notes.getText().toString().replace(",", "") + ".";
            saveFormToFile();
            Intent intent = new Intent(Endgame.this, MainActivity.class);
            intent.putExtra("SCOUTING_FORM", m_currentForm);
            startActivity(intent);
        });
    }


    private void saveFormToFile() {
        File logsDir = new File(getExternalFilesDir(null), MAIN_DIRECTORY_NAME);
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


    private void loadGif(int gifResource) {
        gifs.setImageResource(gifResource);
    }
}
