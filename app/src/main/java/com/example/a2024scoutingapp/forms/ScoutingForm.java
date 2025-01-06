package com.example.a2024scoutingapp.forms;

import com.example.a2024scoutingapp.Constants;

import java.io.Serializable;
import java.util.ArrayList;

public class ScoutingForm implements Serializable {
    public Constants.Team team = Constants.Team.RED;

    public int teamNumber = 1;

    public int matchNumber = 0;

    public String scoutName = "";

    public Constants.GameMode currentMode = Constants.GameMode.AUTO;
    public Constants.GameMode teleopMode = Constants.GameMode.TELEOP;

    public boolean matchOver = false;
    public boolean matchStarted = false;
    public boolean disabled = false;
    public ScoutingForm() {

    }

    @Override
    public String toString() {
        return teamNumber + ","
                + team + ","
                + matchNumber + ","
                + scoutName + ","
                + (disabled ? "True" : "False") + ",";
    }

    public static ScoutingForm fromString(String s) {

        String[] arr = s.split(",");

        ScoutingForm ret = new ScoutingForm();

        ret.teamNumber = Integer.parseInt(arr[0]);
        ret.team = Constants.Team.fromString(arr[1]);
        ret.matchNumber = Integer.parseInt(arr[2]);
        ret.scoutName = arr[3];
        ret.matchStarted = true;
        ret.matchOver = true;
        return ret;
    }
}
