package com.example.a2024scoutingapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.a2024scoutingapp.forms.ScoutingForm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.UUID;

public class SendMessageActivity extends AppCompatActivity {
    private static final String TAG = "SendMessageActivity";
    private static final String DIRECTORY_NAME = "Logs";
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 101;

    private EditText macInput;
    private TextView connectionStatus, connectionInfo;
    private Button sendButton, exitButton;
    private ScoutingForm form;
    String computerAddress ="00:24:D6:F3:F2:A9"; //"B8:1E:A4:CF:BA:54";
    private BluetoothSocket socket;
    private OutputStream outputStream;
    private BluetoothDevice hostDevice;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private static final UUID CONNECT_UUID = UUID.fromString("39675b0d-6dd8-4622-847f-3e5acc607e27");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send);
        form = (ScoutingForm) getIntent().getSerializableExtra("SCOUTING_FORM");
        if (form == null) {
            form = new ScoutingForm();
        }
        // Initialize UI components
        macInput = findViewById(R.id.MacText);
        connectionStatus = findViewById(R.id.ConnectionStatus);
        connectionInfo = findViewById(R.id.EsablishingConnection);
        sendButton = findViewById(R.id.SendButton);
        exitButton = findViewById(R.id.exitbutton);
        macInput = (EditText) findViewById(R.id.MacText);
        macInput.setText(computerAddress);
        // Button listeners
        sendButton.setOnClickListener(v -> {
            connectionStatus.setText("Sending initiatied");
            if (checkBluetoothPermissions()) {
                sendFiles();
                sendButton.setEnabled(false); // Disable button
                sendButton.postDelayed(() -> sendButton.setEnabled(true), 10000);
            }
        });

        exitButton.setOnClickListener(v -> {
            Intent intent = new Intent(SendMessageActivity.this, MatchActivity.class);
            intent.putExtra("SCOUTING_FORM", form);
            startActivity(intent);
        });
    }

    private boolean checkBluetoothPermissions() {
        // Check required permissions
        connectionStatus.setText("Checking Bluetooth...");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            // Request Bluetooth permission
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.BLUETOOTH},
                    BLUETOOTH_PERMISSION_REQUEST_CODE
            );
            return false;
        }
        return true;
    }

    private void sendFiles() {
        connectionStatus.setText("Validating mac address...");
        String macAddress = macInput.getText().toString().trim();
        if (macAddress.isEmpty()) {
            Toast.makeText(this, "Please enter a valid MAC address", Toast.LENGTH_SHORT).show();
            return;
        }
        if (connectToHost(macAddress)) {
                sendAllFiles();
        }
    }

    private boolean connectToHost(String macAddress) {
        connectionStatus.setText("Checking host connection...");
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            // Check for permissions on Android 12+
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.BLUETOOTH},
                        BLUETOOTH_PERMISSION_REQUEST_CODE
                );
                return false;
            }

            // Get the remote device
            hostDevice = bluetoothAdapter.getRemoteDevice(macAddress);
            if (hostDevice == null) {
                Toast.makeText(this, "Unable to find the host device", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Create and connect the Bluetooth socket
            socket = hostDevice.createRfcommSocketToServiceRecord(CONNECT_UUID);
            socket.connect();
            outputStream = socket.getOutputStream();
            connectionInfo.setText("Connected to: " + macAddress);
            return true;

        } catch (SecurityException e) {
            Log.e(TAG, "Missing Bluetooth permissions", e);
            Toast.makeText(this, "Missing Bluetooth permissions", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "Invalid MAC address", e);
            Toast.makeText(this, "Invalid MAC address", Toast.LENGTH_SHORT).show();
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Failed to connect to the host device", e);
            connectionInfo.setText("Connection failed");
            Toast.makeText(this, "Failed to connect to the host device", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    private void   sendAllFiles() {
        connectionStatus.setText("Sending files...");
        File logsDir = new File(getExternalFilesDir(null), DIRECTORY_NAME);
        if (!logsDir.exists() && !logsDir.mkdirs()) {
            Log.e(TAG, "Failed to create Logs directory.");
            return;
        }
        File logFile = new File(logsDir, "DeadInside.log");
        if (!logsDir.exists() || !logsDir.isDirectory()) {
            Toast.makeText(this, "No files available to send", Toast.LENGTH_SHORT).show();
            return;
        }

        File[] files = logsDir.listFiles();
        if (files != null) {
            try (FileWriter writer = new FileWriter(logFile)) {
                writer.write("THE SPANISH INQUISITION");
                System.out.println("THE SPANISH INQUISITION HAS ARRIVED");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Send DeadInside.log first if it exists
            if (logFile.exists()) {
                sendFile(logFile);
            }
            logFile.delete();
            // Send the rest of the files, excluding DeadInside.log
            for (File file : files) {
                if (!file.getName().equals("DeadInside.log")) {
                    sendFile(file);
                }
            }

            Toast.makeText(this, "All files sent successfully", Toast.LENGTH_SHORT).show();
            wipeFiles("Backups");
            moveBackup();
            connectionStatus.setText("Sending complete");
        } else {
            Toast.makeText(this, "No files available to send", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendFile(File file) {
        if (file == null || !file.exists()) {
            Toast.makeText(this, "File does not exist or is invalid", Toast.LENGTH_SHORT).show();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                write(line + "\n"); // Write each line to the Bluetooth connection
            }

            // Write the "end" indicator
            if (outputStream != null) {
                Toast.makeText(this, "File sent successfully: " + file.getName(), Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "Output stream is null, cannot send 'end' indicator");
                Toast.makeText(this, "Failed to send file, output stream not available", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e(TAG, "Error sending file: " + file.getName(), e);
            Toast.makeText(this, "Error sending file: " + file.getName(), Toast.LENGTH_SHORT).show();
        }
    }

    private void write(String data) throws IOException {
        if (outputStream != null) {
            outputStream.write(data.getBytes());
        }
    }
    private void wipeFiles(String directory){
        File backupDir = new File(getExternalFilesDir(null), directory);
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
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Retry sending files if permission was granted
                sendFiles();
            } else {
                Toast.makeText(this, "Bluetooth permission is required to send files", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void moveBackup() {
        try {
            File logsDir = new File(getExternalFilesDir(null), DIRECTORY_NAME);
            File backupDir = new File(getExternalFilesDir(null), "Backups");

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
                        Log.i(TAG, "Deleted " + backupFile.getName());
                    } catch (IOException e) {
                        Log.e(TAG, "Could not delete: " + file.getName(), e);
                    }
                }
                Toast.makeText(this, "All files deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No files to delete", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to delete all files", e);
            Toast.makeText(this, "Failed to delete all files, get Declan", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Failed to close Bluetooth socket", e);
            }
        }
    }
}
