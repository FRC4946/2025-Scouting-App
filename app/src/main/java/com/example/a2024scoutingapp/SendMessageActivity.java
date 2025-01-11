package com.example.a2024scoutingapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class SendMessageActivity extends AppCompatActivity {
    private static final String TAG = "SendMessageActivity";
    private static final String DIRECTORY_NAME = "Logs";

    private BluetoothSocket socket;
    private OutputStream outputStream;
    private BluetoothDevice hostComputer;

    private boolean connected = false;

    private EditText connectionMAC;
    private TextView connectionInfo;
    private ToggleButton sendSavedButton;
    private Button sendButton, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send);

        connectionMAC = findViewById(R.id.MacText);
        connectionInfo = findViewById(R.id.EsablishingConnection);
        sendSavedButton = findViewById(R.id.SendSaved);
        sendButton = findViewById(R.id.SendButton);
        exit = findViewById(R.id.exitbutton);

        sendButton.setOnClickListener(v -> sendMessage());

        // Default connection MAC (for demo purposes)
        connectionMAC.setText("A4:42:3B:4D:C9:2E");
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendMessageActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void sendMessage() {
        new Thread(() -> {
            connectBluetooth();

            if (connected) {
                try {
                    if (sendSavedButton.isChecked()) {
                        sendSavedLogs();
                    } else {
                        write("Hello World!\n");
                    }
                    write("end");
                    runOnUiThread(() -> connectionInfo.setText("Message Sent"));
                } catch (IOException e) {
                    Log.e(TAG, "Error sending message", e);
                    runOnUiThread(() -> connectionInfo.setText("Error Sending Message"));
                }
            }
        }).start();
    }

    private void sendSavedLogs() throws IOException {
        File logsDir = new File(getExternalFilesDir(null), DIRECTORY_NAME);
        File[] logFiles = logsDir.listFiles();

        if (logFiles != null && logFiles.length > 0) {
            StringBuilder logs = new StringBuilder();
            for (File log : logFiles) {
                try (FileReader reader = new FileReader(log)) {
                    char[] buffer = new char[1024];
                    int read;
                    while ((read = reader.read(buffer)) != -1) {
                        logs.append(buffer, 0, read);
                    }
                }
            }
            write(logs.toString());
        } else {
            Log.i(TAG, "No logs to send.");
        }
    }

    private void connectBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            runOnUiThread(() -> connectionInfo.setText("Bluetooth not enabled"));
            return;
        }

        // Assume the paired device is identified by MAC address
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        for (BluetoothDevice device : bluetoothAdapter.getBondedDevices()) {
            if (device.getAddress().equals(connectionMAC.getText().toString())) {
                hostComputer = device;
                break;
            }
        }

        try {
            if (hostComputer != null) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                socket = hostComputer.createRfcommSocketToServiceRecord(
                        UUID.fromString("39675b0d-6dd8-4622-847f-3e5acc607e27"));
                socket.connect();
                outputStream = socket.getOutputStream();
                connected = true;
                runOnUiThread(() -> connectionInfo.setText("Connected"));
            } else {
                runOnUiThread(() -> connectionInfo.setText("Device not found"));
            }
        } catch (IOException e) {
            Log.e(TAG, "Error connecting to Bluetooth", e);
        }
    }

    private void write(String message) throws IOException {
        if (outputStream != null) {
            outputStream.write(message.getBytes());
        }
    }
}
