package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class RestoreActivity extends AppCompatActivity {
    private static final String TAG = "RestoreActivity";
    private static final String BACKUP_DIRECTORY_NAME = "Backups";
    private static final String LOGS_DIRECTORY_NAME = "Logs";

    private RadioGroup backupsGroup;
    private ArrayList<File> backupList = new ArrayList<>();
    private int selected = -1;
    private Button restoreButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);

        // Initialize UI components
        backupsGroup = findViewById(R.id.BackupsGroup);
        restoreButton = findViewById(R.id.RestoreButton);
        backButton = findViewById(R.id.BackButton);

        // Button listeners
        restoreButton.setOnClickListener(v -> {
            if (selected >= 0 && selected < backupList.size()) {
                restoreFile(backupList.get(selected));
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(RestoreActivity.this, LoadActivity.class);
            startActivity(intent);
        });

        updateBackups();
    }

    private void updateBackups() {
        File backupDir = new File(getExternalFilesDir(null), BACKUP_DIRECTORY_NAME);

        if (!backupDir.exists() || !backupDir.isDirectory()) {
            Log.i(TAG, "No backup directory found.");
            Toast.makeText(this, "No backups available", Toast.LENGTH_SHORT).show();
            return;
        }

        backupList.clear();
        backupsGroup.removeAllViews();

        File[] backups = backupDir.listFiles();
        if (backups != null && backups.length > 0) {
            for (int i = 0; i < backups.length; i++) {
                File file = backups[i];
                backupList.add(file);

                RadioButton button = new RadioButton(this);
                button.setText(file.getName());
                int finalI = i;
                button.setOnClickListener(v -> selected = finalI);

                backupsGroup.addView(button);
            }
        } else {
            Toast.makeText(this, "No backups found", Toast.LENGTH_SHORT).show();
        }
    }

    private void restoreFile(File backupFile) {
        File logsDir = new File(getExternalFilesDir(null), LOGS_DIRECTORY_NAME);

        if (!logsDir.exists() && !logsDir.mkdirs()) {
            Log.e(TAG, "Failed to create Logs directory.");
            Toast.makeText(this, "Failed to restore file", Toast.LENGTH_SHORT).show();
            return;
        }

        File restoredFile = new File(logsDir, backupFile.getName());
        try {
            Files.move(backupFile.toPath(), restoredFile.toPath());
            Toast.makeText(this, "File restored: " + restoredFile.getName(), Toast.LENGTH_SHORT).show();
            updateBackups();
        } catch (IOException e) {
            Log.e(TAG, "Failed to restore file: " + backupFile.getName(), e);
            Toast.makeText(this, "Failed to restore file", Toast.LENGTH_SHORT).show();
        }
    }
}
