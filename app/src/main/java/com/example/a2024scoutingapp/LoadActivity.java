package com.example.a2024scoutingapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class LoadActivity extends AppCompatActivity {

    private static final String TAG = "LoadActivity";
    private static final int DELETE_PERMISSION_REQUEST = 100;

    int selected = -1; // Default to no selection
    File[] existingFiles = new File[0];
    RadioGroup filesGroup;
    ArrayList<RadioButton> fileButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        updateFiles();

        Button loadButton = findViewById(R.id.LoadButton);
        Button deleteButton = findViewById(R.id.DeleteButton);

        loadButton.setOnClickListener(v -> {
            Log.i(TAG, "Commencing load");
            if (existingFiles.length > 0 && selected >= 0) {
                loadFile(existingFiles[selected]);
            } else {
                Toast.makeText(this, "No file selected to load.", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "No files found, returning to main activity");
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        deleteButton.setOnClickListener(v -> {
            Log.i(TAG, "Commencing delete");
            if (existingFiles.length > 0 && selected >= 0) {
                // Check for required permissions
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            DELETE_PERMISSION_REQUEST);
                } else {
                    deleteFile(existingFiles[selected]);
                }
            } else {
                Toast.makeText(this, "No file selected to delete.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFile(File file) {
        if (file != null && file.exists()) {
            Intent intent = new Intent(this, MainActivity.class);
            String fileName = file.getAbsolutePath();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.putExtra(Intent.EXTRA_TEXT, fileName);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Selected file does not exist.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteFile(File file) {
        if (file != null && file.exists() && file.delete()) {
            Toast.makeText(this, "File deleted successfully.", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "File deleted successfully.");
            updateFiles();
        } else {
            Toast.makeText(this, "Failed to delete file.", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "Failed to delete file.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == DELETE_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (selected >= 0 && selected < existingFiles.length) {
                    deleteFile(existingFiles[selected]);
                }
            } else {
                Toast.makeText(this, "Permission denied to delete files.", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Write permission was NOT granted.");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void updateFiles() {
        filesGroup = findViewById(R.id.Files);
        filesGroup.removeAllViews(); // Clear existing RadioButtons
        fileButtons = new ArrayList<>();

        File logsDir = new File(getApplicationInfo().dataDir, "Logs");
        Log.i(TAG, "Logs directory path: " + logsDir.getAbsolutePath());
        System.out.println("Logs directory path: " + logsDir.getAbsolutePath());
        if (!logsDir.exists()) {
            if (!logsDir.mkdirs()) {
                Log.e(TAG, "Failed to create Logs directory.");
                System.out.println("Logs not able to make");
                Toast.makeText(this, "Logs directory is missing.", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        System.out.println("Logs directory path: " + logsDir.getAbsolutePath());
        existingFiles = logsDir.listFiles();
        if (existingFiles != null && existingFiles.length > 0) {
            for (int i = 0; i < existingFiles.length; i++) {
                Log.i(TAG, "Found file: " + existingFiles[i].getName());
                System.out.println("Found file: " + existingFiles[i].getName());
                RadioButton fileButton = new RadioButton(this);
                fileButton.setLayoutParams(new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.WRAP_CONTENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT));
                String fileName = existingFiles[i].getName();

                // Extract details from file name
                String[] arr = fileName.split("-");
                if (arr.length >= 2) {
                    fileButton.setText((i + 1) + " - Match " + arr[0] + ", Team " + arr[1]);
                } else {
                    fileButton.setText((i + 1) + " - " + fileName);
                }

                fileButton.setOnClickListener(v -> selected = filesGroup.indexOfChild(v));
                fileButtons.add(fileButton);
                filesGroup.addView(fileButton);
            }

            // Select the first file by default
            filesGroup.check(fileButtons.get(0).getId());
        } else {
            Toast.makeText(this, "No files found.", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "No files found in Logs directory.");
        }
    }

    // Example method to save a file
    private void saveFile(String fileName, String fileContent) {
        File logsDir = new File(getApplicationInfo().dataDir, "Logs");
        if (!logsDir.exists()) {
            logsDir.mkdirs();
        }

        File newFile = new File(logsDir, fileName);
        try (FileWriter writer = new FileWriter(newFile)) {
            writer.write(fileContent);
            writer.flush();
            Log.i(TAG, "File saved: " + newFile.getAbsolutePath());
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile))); // Ensure file system visibility
            updateFiles(); // Refresh the list
        } catch (IOException e) {
            Log.e(TAG, "Error saving file", e);
            Toast.makeText(this, "Failed to save file.", Toast.LENGTH_SHORT).show();
        }
    }
}
