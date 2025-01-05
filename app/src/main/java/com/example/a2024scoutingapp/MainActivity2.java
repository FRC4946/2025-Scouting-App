package com.example.a2024scoutingapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {
    private ScoutingForm m_currentForm;
    private Button m_blue1button, m_blue2Button, m_blue3Button,
            m_red1Button, m_red2Button, m_red3Button;
    private ImageButton m_downArrowButton, m_upArrowButton;
    private String m_station;
//yes
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        m_red1Button = findViewById(R.id.red1Button);
        m_blue1button = findViewById(R.id.blue1button);
        m_downArrowButton = findViewById(R.id.downArrow);
        m_upArrowButton = findViewById(R.id.upArrow);
/*        updateTeamNumber(m_red1Button);
        updateTeamNumber(m_blue1button);*/



        //TODO:refactor the ui again
        m_red1Button.setOnClickListener(view -> {
            m_currentForm.team = Constants.Team.RED;
            m_station = "Red 1";
            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.READ_SCHEDULE_REQUEST);
            //System.out.println(m_currentForm.teamNumber);
        });

        m_blue1button.setOnClickListener(view -> {
            m_currentForm.team = Constants.Team.BLUE;
            m_station = "Blue 1";
            ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.READ_SCHEDULE_REQUEST);
            //System.out.println(m_currentForm.teamNumber);
        });
        m_downArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_currentForm.isInverted = false;
                System.out.println(m_currentForm.team);
                Intent intent = new Intent(MainActivity2.this, MatchActivity.class);
                intent.putExtra("SCOUTING_FORM", m_currentForm);
                startActivity(intent);
            }
        });
        m_upArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               m_currentForm.isInverted = true;
               System.out.println(m_currentForm.team);
               Intent intent  = new Intent(MainActivity2.this, MatchActivity.class);
               intent.putExtra("SCOUTING_FORM", m_currentForm);
               startActivity(intent);
            }
        });


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

        } else if (requestCode == Constants.READ_SCHEDULE_REQUEST) {

            Log.i("A", "Received response for read permission request.");

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //When read permission is granted

                Log.i("A", "Read permission has now been granted.");

                try {

                    // Read the JSON File in the Documents Folder
                    // *Technically a txt file
                    BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getPath() + "/Documents/schedule.txt"));
                    JSONArray schedule = new JSONObject(reader.readLine()).getJSONArray("Schedule");

                    for (int i = 0; i < schedule.length(); i++) {
                        try {
                            JSONObject match = schedule.getJSONObject(i);

                            // Pull match number
                            int matchNumber = match.getInt("matchNumber");
                            if (matchNumber == m_currentForm.matchNumber) {
                                JSONArray teams = match.getJSONArray("teams");

                                if (m_currentForm.team == Constants.Team.RED) {
                                    m_currentForm.opponentA = teams.getJSONObject(3).getInt("teamNumber");
                                    System.out.println(m_currentForm.opponentA);
                                    m_currentForm.opponentB = teams.getJSONObject(4).getInt("teamNumber");
                                    System.out.println(m_currentForm.opponentB);
                                    m_currentForm.opponentC = teams.getJSONObject(5).getInt("teamNumber");
                                    System.out.println(m_currentForm.opponentC);
                                } else {
                                    m_currentForm.opponentA = teams.getJSONObject(0).getInt("teamNumber");
                                    System.out.println(m_currentForm.opponentA);
                                    m_currentForm.opponentB = teams.getJSONObject(1).getInt("teamNumber");
                                    System.out.println(m_currentForm.opponentB);
                                    m_currentForm.opponentC = teams.getJSONObject(2).getInt("teamNumber");
                                    System.out.println(m_currentForm.opponentC);
                                }

                                break;
                            }
                        } catch (JSONException e) {
                            // Oops
                            Log.i("A", e.toString());
                        }
                    }


                    reader.close();

                } catch (IOException e) {
                    //IO didn't work, honestly this shouldn't happen, if it does its probably a device or permission error
                    Log.i("A", "File read failed");
                } catch (JSONException e) {
                    Log.i("A", "JSON Parse failed");
                }

            } else {
                Log.i("A", "Read permission was NOT granted.");
            }

        } else if (requestCode == Constants.READ_TEAM_NUMBER_REQUEST) {

            Log.i("A", "Received response for read permission request.");

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //When read permission is granted

                Log.i("A", "Read permission has now been granted.");

                try {

                    // Read the JSON File in the Documents Folder
                    // *Technically a txt file
                    BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getPath() + "/Documents/schedule.txt"));
                    JSONArray schedule = new JSONObject(reader.readLine()).getJSONArray("Schedule");

                    for (int i = 0; i < schedule.length(); i++) {
                        try {
                            JSONObject match = schedule.getJSONObject(i);

                            // Pull match number
                            int matchNumber = match.getInt("matchNumber");
                            if (matchNumber == m_currentForm.matchNumber) {
                                JSONArray teams = match.getJSONArray("teams");

//                                switch (m_station) {
//                                    case "Red 1":
//                                        m_currentForm.teamNumber = teams.getJSONObject(0).getInt("teamNumber");
//                                        break;
//                                    case "Red 2":
//                                        m_currentForm.teamNumber = teams.getJSONObject(1).getInt("teamNumber");
//                                        break;
//                                    case "Red 3":
//                                        m_currentForm.teamNumber = teams.getJSONObject(2).getInt("teamNumber");
//                                        break;
//                                    case "Blue 1":
//                                        m_currentForm.teamNumber = teams.getJSONObject(3).getInt("teamNumber");
//                                        break;
//                                    case "Blue 2":
//                                        m_currentForm.teamNumber = teams.getJSONObject(4).getInt("teamNumber");
//                                        break;
//                                    case "Blue 3":
//                                        m_currentForm.teamNumber = teams.getJSONObject(5).getInt("teamNumber");
//                                        break;
//                                }

                                //TODO: fix this and make the text
                                //this will be part of refactoring the ui again
                                //m_teamNumber.setText("" + m_currentForm.teamNumber);
                            }
                        } catch (JSONException e) {
                            // Oops
                            Log.i("A", e.toString());
                        }
                    }


                    reader.close();

                } catch (IOException e) {
                    //IO didn't work, honestly this shouldn't happen, if it does its probably a device or permission error
                    Log.i("A", "File read failed");
                } catch (JSONException e) {
                    Log.i("A", "JSON Parse failed");
                }

            } else {
                Log.i("A", "Read permission was NOT granted.");
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void updateTeamNumber(Button b) {
//        b.setText(b.getText().toString().substring(!b.getText().toString().contains("\n") ? 0 : b.getText().toString().indexOf("\n")));
        m_station = b.getText().toString();
        ActivityCompat.requestPermissions(MainActivity2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                Constants.READ_TEAM_NUMBER_REQUEST);
        b.setText("\n" + m_currentForm.teamNumber);
    }
}