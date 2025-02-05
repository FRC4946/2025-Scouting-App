package com.example.a2024scoutingapp.forms;

import com.example.a2024scoutingapp.Constants;

import java.io.Serializable;
import java.util.ArrayList;

public class ScoutingForm implements Serializable {
    public Constants.Team team = Constants.Team.RED;

    public int teamNumber = 0;

    public int matchNumber = 0;
    public int teleopL4Coral = 0;
    public int defencePercent = 0;
    public int teleopL3Coral = 0;
    public int teleopL2Coral = 0;
    public int teleopL1Coral = 0;
    public int autoL4Coral = 0;
    public int autoL3Coral = 0;
    public int autoL2Coral = 0;
    public int autoL1Coral = 0;
    public int autoProcessor = 0;
    public int autoNet = 0;
    public String notes = "depression";
    public int teleopProcessor = 0;
    public int teleopNet = 0;
    public boolean loaded = false;
    public String scoutName = "";
    public int climbSpeed = 0;

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
                + (disabled ? "True" : "False") + ","
                + autoL1Coral + ","
                + autoL2Coral + ","
                + autoL3Coral + ","
                + autoL4Coral + ","
                + autoProcessor + ","
                + autoNet + ","
                + teleopL1Coral + ","
                + teleopL2Coral + ","
                + teleopL3Coral + ","
                +teleopL4Coral + ","
                + teleopProcessor + ","
                + teleopNet + ","
                + defencePercent + ","
                + climbSpeed + ","
                + notes + ",";
    }

    public static ScoutingForm fromString(String s) {

        String[] arr = s.split(",");

        ScoutingForm ret = new ScoutingForm();
        System.out.println(s);
        ret.teamNumber = Integer.parseInt(arr[0]);
        ret.team = Constants.Team.fromString(arr[1]);
        ret.matchNumber = Integer.parseInt(arr[2]);
        ret.scoutName = arr[3];
        ret.disabled = Boolean.parseBoolean(arr[4]);
        ret.autoL1Coral = Integer.parseInt(arr[5]);
        ret.autoL2Coral = Integer.parseInt(arr[6]);
        ret.autoL3Coral = Integer.parseInt(arr[7]);
        ret.autoL4Coral = Integer.parseInt(arr[8]);
        ret.autoProcessor = Integer.parseInt(arr[9]);
        ret.autoNet = Integer.parseInt(arr[10]);
        ret.teleopL1Coral = Integer.parseInt(arr[11]);
        ret.teleopL2Coral = Integer.parseInt(arr[12]);
        ret.teleopL3Coral = Integer.parseInt(arr[13]);
        ret.teleopL4Coral = Integer.parseInt(arr[14]);
        ret.teleopProcessor = Integer.parseInt(arr[15]);
        ret.teleopNet = Integer.parseInt(arr[16]);
        ret.defencePercent = Integer.parseInt(arr[17]);
        ret.climbSpeed = Integer.parseInt(arr[18]);
        ret.notes = arr[19];
        ret.matchStarted = true;
        ret.matchOver = true;
        return ret;
    }
}
