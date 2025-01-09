/* package com.example.a2024scoutingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MatchActivity extends AppCompatActivity {
    private boolean isRunning = false;
    private String endgameTimerText;
    private int endgameTimerCount;
    private ScoutingForm m_currentForm;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");

        System.out.println(m_currentForm.opponentA);
        System.out.println(m_currentForm.opponentB);
        System.out.println(m_currentForm.opponentC);
        Button exitwithoutsave = findViewById(R.id.exitbutton2);
        Button teleopButton = findViewById(R.id.teleop);
        Button autoButton = findViewById(R.id.auto);
        Button sendPageButton = findViewById(R.id.exitbutton);
        Button[] optionButtons = new Button[] {sendPageButton, autoButton, teleopButton};
        System.out.println(m_currentForm.team);
        if (m_currentForm == null) {
            m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
            (new Handler()).postDelayed(() -> {System.out.println(m_currentForm.team);}, 5000);
        }
        if (m_currentForm.team == Constants.Team.BLUE){
            teleopButton.setBackgroundColor(getResources().getColor(R.color.blueTeam));
            autoButton.setBackgroundColor(getResources().getColor(R.color.blueTeam));
            sendPageButton.setBackgroundColor(getResources().getColor(R.color.blueTeam));
        } else {
            teleopButton.setBackgroundColor(getResources().getColor(R.color.redTeam));
            autoButton.setBackgroundColor(getResources().getColor(R.color.redTeam));
            sendPageButton.setBackgroundColor(getResources().getColor(R.color.redTeam));
        }
        sendPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchActivity.this, MainActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                ActivityCompat.requestPermissions(MatchActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.WRITE_LOG_REQUEST);
                startActivity(intent);
            }
        });

        autoButton.setOnClickListener(v -> {
                Intent intent = new Intent(MatchActivity.this, Auto.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
        });

        teleopButton.setOnClickListener(v -> {
            Intent intent;
            if(m_currentForm.isInverted) {
                intent = new Intent(MatchActivity.this, InvertedTeleopActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            } else {
                intent = new Intent(MatchActivity.this, TeleopActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        exitwithoutsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MatchActivity.this, MainActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        // TODO: redo this one to actually work
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == Constants.WRITE_LOG_REQUEST) {

            Log.i("A", "Received response for write permission request.");

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //When write permission is granted

                Log.i("A", "Write permission has now been granted.");

                File home = new File(getApplicationInfo().dataDir + "/Logs"); //application directory, so: /data/data/com.whatever.otherstuff/Logs
                home.mkdirs();
                File csv = new File(home.getAbsolutePath() + "/" + m_currentForm.matchNumber + "-" + m_currentForm.teamNumber);

                try {


                    BufferedWriter writer = new BufferedWriter(new FileWriter(csv));
                    writer.write(m_currentForm.toString());
                    writer.close();


                    Log.i("A", "Finished writing to " + csv.getAbsolutePath() + "");
                    Utilities.showToast(this, "SAVED!", 2);

                    //IO crap for testing
                    //BufferedReader reader = new BufferedReader(new FileReader(csv));
                    //reader.close();
                } catch (IOException e) {
                    //IO didn't work, honestly   shouldn't happen, if it does its probably a device or permission error
                    Log.i("A", "File write to " + csv.getAbsolutePath() + " failed");
                }

            } else {
                Log.i("A", "Write permission was NOT granted.");
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

 */