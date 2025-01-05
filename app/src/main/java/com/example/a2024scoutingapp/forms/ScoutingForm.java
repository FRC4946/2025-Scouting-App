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
    public Constants.GameMode endgameMode = Constants.GameMode.ENDGAME;
    public Constants.GameAction currentAction = Constants.GameAction.OFFENCE;
    public Constants.DefenceType currentDefenceType = Constants.DefenceType.IDLE;

    public boolean matchOver = false;
    public boolean matchStarted = false;

    // public TimePeriod climbPeriod = new TimePeriod();
    // public ArrayList<TimePeriod> defenceTimes = new ArrayList<TimePeriod>();
    // public ArrayList<TimePeriod> activeDefenceTimes = new ArrayList<TimePeriod>();

    public int autoAmp = 0;
    public int autoSpeaker = 0;
    public int autoMissed = 0;
    public int amp = 0;
    public int speaker = 0;
    public int missed = 0;
    public boolean doubleClimb = false;
    public double endgameTime = 0.0;
    public int opponentA = 0;
    public double opponentADefenceTime = 0;
    public int opponentB = 0;
    public double opponentBDefenceTime = 0;
    public int opponentC = 0;
    public double opponentCDefenceTime = 0;
    public String tempCoords = "";
    public String allCoords = "";
    public int passed = 0;
    public boolean disabled = false;
    public int currentCycle = 0; // First cycle will be 0 in the code, but will display 1 to the user

    // The double[] is formatted such that
    // Index 0: Transport Timer 1
    // Index 1: Loading Timer
    // Index 2: Transport Timer 2
    // Index 3: Community Timer
    public ArrayList<double[]> cycleTimes = new ArrayList<>();

    private boolean m_finalized = false;
    public boolean isInverted = false;

    public ScoutingForm() {

    }

    @Override
    public String toString() {
        return teamNumber + ","
                + team + ","
                + matchNumber + ","
                + scoutName + ","
                + autoAmp + ","
                + autoSpeaker + ","
                + autoMissed + ","
                + amp + ","
                + speaker + ","
                + missed + ","
                + passed + ","
                + (isInverted ? "True" : "False") + ","
                + allCoords + ","
                + (disabled ? "True" : "False") + ","
                + opponentA + ","
                + opponentADefenceTime + ","
                + opponentB + ","
                + opponentBDefenceTime + ","
                + opponentC + ","
                + opponentCDefenceTime;
    }

    public static ScoutingForm fromString(String s) {

        String[] arr = s.split(",");

        ScoutingForm ret = new ScoutingForm();

        ret.teamNumber = Integer.parseInt(arr[0]);
        ret.team = Constants.Team.fromString(arr[1]);
        ret.matchNumber = Integer.parseInt(arr[2]);
        ret.scoutName = arr[3];
        ret.autoAmp = Integer.parseInt(arr[4]);
        ret.autoSpeaker = Integer.parseInt(arr[5]);
        ret.amp = Integer.parseInt(arr[6]);
        ret.speaker = Integer.parseInt(arr[7]);
        ret.doubleClimb = "True".equals(arr[8]);
        ret.endgameTime = Double.parseDouble(arr[9]);
        ret.opponentA = Integer.parseInt(arr[10]);
        ret.opponentADefenceTime = Double.parseDouble(arr[11]);
        ret.opponentB = Integer.parseInt(arr[12]);
        ret.opponentBDefenceTime = Double.parseDouble(arr[13]);
        ret.opponentC = Integer.parseInt(arr[14]);
        ret.opponentCDefenceTime = Double.parseDouble(arr[15]);
        ret.matchStarted = true;
        ret.matchOver = true;
        return ret;
    }
}
