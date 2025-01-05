package com.example.a2024scoutingapp;

import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Constants {

    public static final int SCOUT_NAME_MAX_UI_LENGTH = 20;

    public static final String START_MATCH_ERROR = "Please Start The Match";
    public static final String MATCH_OVER_ERROR = "Cannot Edit After The Match Has Been Ended";

    public static final int WRITE_LOG_REQUEST = 1;
    public static final int DELETE_LOG_REQUEST = 2;
    public static final int READ_SCHEDULE_REQUEST = 3;
    public static final int READ_TEAM_NUMBER_REQUEST = 4;


    public enum GameMode {
        AUTO, TELEOP, ENDGAME,
    }

    public enum GameAction {
        OFFENCE, DEFENCE
    }

    public enum DefenceType {
        DEFENDING, IDLE
    }

    public enum Team {
        RED, BLUE;

        @Override
        public String toString() {
            switch (this) {
                case BLUE:
                    return "Blue";
                case RED:
                default:
                    return "Red";
            }
        }

        public static Team fromString(String s) {
            if ("Blue".equals(s)) {
                return BLUE;
            } else {
                return RED;
            }
        }
    }

    public enum Climb {
        CLIMB, PARK, NONE;

        @Override
        public String toString() {
            switch (this) {
                case CLIMB:
                    return "Climb";
                case PARK:
                    return "Park";
                case NONE:
                default:
                    return "None";
            }
        }

        public static Climb fromString(String s) {
            if ("Climb".equals(s)) {
                return CLIMB;
            } else if ("Park".equals(s)) {
                return PARK;
            } else {
                return NONE;
            }
        }
    }

}
