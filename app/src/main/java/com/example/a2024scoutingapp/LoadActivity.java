package com.example.a2024scoutingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class LoadActivity extends AppCompatActivity {
    private static final String TAG = "LoadActivity";
    private static final String DIRECTORY_NAME = "Logs";

    private RadioGroup filesGroup;
    private ArrayList<File> fileList = new ArrayList<>();
    private int selected = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        filesGroup = findViewById(R.id.Files);
        Button loadButton = findViewById(R.id.LoadButton);

        loadButton.setOnClickListener(v -> {
            if (selected >= 0 && selected < fileList.size()) {
                loadFile(fileList.get(selected));
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
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
        if (file.exists()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, file.getAbsolutePath());
            startActivity(intent);
        } else {
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        }
    }
}
