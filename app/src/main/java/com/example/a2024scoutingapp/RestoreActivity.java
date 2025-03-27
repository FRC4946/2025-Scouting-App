package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    private ScoutingForm m_currentForm;

    private Button restoreButton, deleteButton, deleteAllButton, backButton, restoreAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
        m_currentForm = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        if (m_currentForm == null) {
            m_currentForm = new ScoutingForm();
        }
        // Initialize UI components
        backupsGroup = findViewById(R.id.BackupsGroup);
        restoreButton = findViewById(R.id.RestoreButton);
        deleteButton = findViewById(R.id.DeleteButton);
        deleteAllButton = findViewById(R.id.DeleteAllButton);
        backButton = findViewById(R.id.BackButton);
        restoreAll = findViewById(R.id.RestoreAll);

        // Populate backup list
        updateBackups();

        backupsGroup.setOnCheckedChangeListener((group, checkedId) -> {
            selected = backupsGroup.indexOfChild(findViewById(checkedId));
        });
        restoreAll.setOnClickListener(v -> {restoreAllFiles();});
        restoreButton.setOnClickListener(v -> {
            if (selected >= 0 && selected < backupList.size()) {
                restoreFile(backupList.get(selected));
            } else {
                Toast.makeText(this, "No file selected to restore", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (selected >= 0 && selected < backupList.size()) {
                permanentlyDeleteFile(backupList.get(selected));
            } else {
                Toast.makeText(this, "No file selected to delete", Toast.LENGTH_SHORT).show();
            }
        });

        deleteAllButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(RestoreActivity.this);
            builder.setTitle("Delete All Backups?")
                    .setMessage("Are you sure you want to permanently delete all backup files?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteAllBackups();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                    })
                    .show();
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(RestoreActivity.this, MatchActivity.class);
            intent.putExtra("SCOUTING_FORM", m_currentForm);
            startActivity(intent);
        });
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
            for (File file : backups) {
                backupList.add(file);
                RadioButton button = new RadioButton(this);
                button.setText(file.getName());
                backupsGroup.addView(button);
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    StringBuilder fileContent = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        fileContent.append(line);
                    }
                    System.out.println(fileContent.toString());
                } catch (IOException e) {

                }
            }
        } else {
            Toast.makeText(this, "No backup files found", Toast.LENGTH_SHORT).show();
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
        if (backupFile.renameTo(restoredFile)) {
            Toast.makeText(this, "File restored: " + restoredFile.getName(), Toast.LENGTH_SHORT).show();
            updateBackups(); // Refresh list after restoration
        } else {
            Log.e(TAG, "Failed to restore file: " + backupFile.getName());
            Toast.makeText(this, "Failed to restore file", Toast.LENGTH_SHORT).show();
        }
    }

    private void permanentlyDeleteFile(File file) {
        if (file.delete()) {
            Toast.makeText(this, "File permanently deleted: " + file.getName(), Toast.LENGTH_SHORT).show();
            updateBackups(); // Refresh list after deletion
        } else {
            Log.e(TAG, "Failed to permanently delete file: " + file.getName());
            Toast.makeText(this, "Failed to delete file", Toast.LENGTH_SHORT).show();
        }
    }
    private void restoreAllFiles() {
        try {
            File logsDir = new File(getExternalFilesDir(null), LOGS_DIRECTORY_NAME);
            File backupDir = new File(getExternalFilesDir(null), BACKUP_DIRECTORY_NAME);

            if (!logsDir.exists() && !logsDir.mkdirs()) {
                Log.e(TAG, "Failed to create backup directory.");
                return;
            }

            File[] files = backupDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    File backupFile = new File(backupDir, file.getName());
                    restoreFile(backupFile);
                }
                updateBackups();
                Toast.makeText(this, "All files restored", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No files to restore", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to delete all files", e);
            Toast.makeText(this, "Failed to delete all files, get Declan", Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteAllBackups() {
        File backupDir = new File(getExternalFilesDir(null), BACKUP_DIRECTORY_NAME);
        if (backupDir.exists() && backupDir.isDirectory()) {
            File[] files = backupDir.listFiles();
            if (files != null) {
                boolean allDeleted = true;
                for (File file : files) {
                    if (!file.delete()) {
                        Log.e(TAG, "Failed to delete file: " + file.getName());
                        allDeleted = false;
                    }
                }

                if (allDeleted) {
                    Toast.makeText(this, "All backup files deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Some files could not be deleted", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "No backup files to delete", Toast.LENGTH_SHORT).show();
        }

        updateBackups(); // Refresh list after deletion
    }
}
