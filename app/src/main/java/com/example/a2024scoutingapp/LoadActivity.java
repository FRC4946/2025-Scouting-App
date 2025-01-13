package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.a2024scoutingapp.forms.ScoutingForm;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class LoadActivity extends AppCompatActivity {
    private static final String TAG = "LoadActivity";
    private static final String DIRECTORY_NAME = "Logs";
    private static final String BACKUP_DIRECTORY_NAME = "Backups";

    private RadioGroup filesGroup;
    private ArrayList<File> fileList = new ArrayList<>();
    private int selected = -1;
    private Button exit, deleteAll, delete, loadButton, restore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        // Initialize UI components
        exit = findViewById(R.id.exiiits);
        restore = findViewById(R.id.restore);
        delete = findViewById(R.id.delete);
        deleteAll = findViewById(R.id.notouchy);
        filesGroup = findViewById(R.id.Files);
        loadButton = findViewById(R.id.LoadButton);

        // Button listeners
        loadButton.setOnClickListener(v -> {
            if (selected >= 0 && selected < fileList.size()) {
                loadFile(fileList.get(selected));
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(v -> {
            if (selected >= 0 && selected < fileList.size()) {
                deleteFile(fileList.get(selected));
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        });

        deleteAll.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoadActivity.this);
            builder.setTitle("Confirm Delete All")
                    .setMessage("Are you sure you want to move all files to the backup directory?")
                    .setPositiveButton("Yes", (dialog, which) -> deleteAllFiles())
                    .setNegativeButton("No", (dialog, which) -> {
                        // Do nothing
                    });
            builder.show();
        });

        restore.setOnClickListener(v -> {
            final EditText pinInput = new EditText(LoadActivity.this);
            pinInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);

            AlertDialog.Builder builder = new AlertDialog.Builder(LoadActivity.this);
            builder.setTitle("Enter PIN")
                    .setMessage("Please enter your PIN to restore files.")
                    .setView(pinInput)
                    .setPositiveButton("OK", (dialog, which) -> {
                        String pin = pinInput.getText().toString();
                        if (pin.equals("0111")) {  // Replace with actual PIN logic
                            navigateToRestoreView();
                        } else {
                            Toast.makeText(LoadActivity.this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> {
                        // Do nothing
                    });
            builder.show();
        });

        exit.setOnClickListener(v -> {
            Intent intent = new Intent(LoadActivity.this, MainActivity.class);
            startActivity(intent);
        });

        updateFiles();
    }

    private void updateFiles() {
        File logsDir = new File(getExternalFilesDir(null), DIRECTORY_NAME);

        if (!logsDir.exists() && !logsDir.mkdirs()) {
            Log.e(TAG, "Failed to create Logs directory.");
            return;
        }

        fileList.clear();
        filesGroup.removeAllViews();

        File[] files = logsDir.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                fileList.add(file);

                RadioButton button = new RadioButton(this);
                button.setText(file.getName());
                int finalI = i;
                button.setOnClickListener(v -> selected = finalI);

                filesGroup.addView(button);
            }
        } else {
            Toast.makeText(this, "No files found", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFile(File file) {
        if (file == null || !file.exists()) {
            Toast.makeText(this, "File does not exist or is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder fileContent = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            // Assuming file content is serialized match data
            ScoutingForm loadedForm = parseMatchData(fileContent.toString());

            if (loadedForm != null) {
                Intent intent = new Intent(this, Auto.class); // Change to the appropriate activity
                intent.putExtra("SCOUTING_FORM", loadedForm);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to parse match data", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error loading file: " + file.getName(), e);
            Toast.makeText(this, "Error loading file", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteFile(File file) {
        File backupDir = new File(getExternalFilesDir(null), BACKUP_DIRECTORY_NAME);

        if (!backupDir.exists() && !backupDir.mkdirs()) {
            Log.e(TAG, "Failed to create backup directory.");
            return;
        }

        File backupFile = new File(backupDir, file.getName());
        try {
            Files.move(file.toPath(), backupFile.toPath());
            Toast.makeText(this, "File moved to backup: " + backupFile.getName(), Toast.LENGTH_SHORT).show();
            updateFiles();
        } catch (IOException e) {
            Log.e(TAG, "Failed to move file to backup", e);
            Toast.makeText(this, "Failed to move file to backup", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteAllFiles() {
        File logsDir = new File(getExternalFilesDir(null), DIRECTORY_NAME);
        File backupDir = new File(getExternalFilesDir(null), BACKUP_DIRECTORY_NAME);

        if (!backupDir.exists() && !backupDir.mkdirs()) {
            Log.e(TAG, "Failed to create backup directory.");
            return;
        }

        File[] files = logsDir.listFiles();
        if (files != null) {
            for (File file : files) {
                File backupFile = new File(backupDir, file.getName());
                try {
                    Files.move(file.toPath(), backupFile.toPath());
                    Log.i(TAG, "Moved file to backup: " + backupFile.getName());
                } catch (IOException e) {
                    Log.e(TAG, "Failed to move file to backup: " + file.getName(), e);
                }
            }
            Toast.makeText(this, "All files moved to backup", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No files to move", Toast.LENGTH_SHORT).show();
        }

        updateFiles();
    }
    private ScoutingForm parseMatchData(String fileContent) {
        try {
            // Example: Parsing JSON data (adapt to your file format)
            Gson gson = new Gson();
            return gson.fromJson(fileContent, ScoutingForm.class);
        } catch (Exception e) {
            Log.e(TAG, "Failed to parse match data", e);
            return null;
        }
    }

    private void navigateToRestoreView() {
        Intent intent = new Intent(LoadActivity.this, RestoreActivity.class);
        startActivity(intent);
    }
}
