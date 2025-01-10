package com.example.a2024scoutingapp;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
//import android.support.v4.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;



import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class SendMessageActivity extends AppCompatActivity {
    //yes
    BluetoothSocket socket;

    BluetoothDevice hostComputer;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//yes

    private OutputStream outputStream; //Stream data is written to
    private InputStream inStream; //Stream data is read from, currently unused

    String computerAddress = "A4:42:3B:4D:C9:2E"; //"00:C2:C6:C4:71:C3" //3C:F8:62:C5:8D:C4
    //"E0:2B:E9:A9:CB:57"
    final String stringUUID = "39675b0d-6dd8-4622-847f-3e5acc607e27"; //UUID of application DO NOT CHANGE
    UUID ConnectToUUID = UUID.fromString(stringUUID);

    boolean connected = false;

    String message;

    EditText connectionMAC;
    TextView connectionInfo;
    ToggleButton sendSavedButton;
    Button sendButton, deleteButton, exit;
    File[] existingFiles;

    boolean sending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: make the send screen
        // all of the errors here are from no send screen
        setContentView(R.layout.send);


        System.out.println(getApplicationInfo().dataDir);
        existingFiles = new File(getApplicationInfo().dataDir + "/Logs").listFiles();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        message = intent.getStringExtra(Intent.EXTRA_TEXT); //Gets message from previous activity, stores in message string

        connectionMAC = (EditText) findViewById(R.id.MacText);
        connectionMAC.setText(computerAddress);

        //TODO: add this + message info later
        connectionInfo = (TextView) findViewById(R.id.EsablishingConnection);
        connectionInfo.setText("Not Connected");

        final TextView messageInfo = (TextView) findViewById(R.id.ConnectionStatus);
        messageInfo.setText(message);


        sendSavedButton = (ToggleButton) findViewById(R.id.SendSaved);
        exit = findViewById(R.id.exitbutton);
        sendButton = (Button) findViewById(R.id.SendButton);
        deleteButton = (Button) findViewById(R.id.samWhyYouFallAsleep);

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendMessageActivity.this, MainActivity.class);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {

            /**
             * This method is called when the send button is pushed
             *
             * @param v ignore this, android handles this variable automatically
             */

            public void onClick(View v) {
                sendButton.setVisibility(View.INVISIBLE);
                connectionInfo.setText("Sending Message");
                sendMessage();
            }

        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(SendMessageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.DELETE_LOG_REQUEST);
                //onRequestPermissionsResult(Constants.DELETE_LOG_REQUEST, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new int[]{PackageManager.PERMISSION_GRANTED});
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SendMessageActivity.this, MainActivity.class);
        startActivity(intent);
    }

    void sendMessage() {
        if (!sending) {
            sending = true;
            new Thread(
                    new Runnable() {
                        public void run() {
                            //when the send button is pushed

                            connect();

                            try {
                                Thread.sleep(250);
                            } catch (Exception e) {
                                Log.i("A", "Error Waiting");
                            }

                            if (connected) {
                                try {
                                    if (!sendSavedButton.isChecked()) {
                                        write("" + message + "\n");
                                    }

                                    if (sendSavedButton.isChecked()) {

                                        //if the send saved option is selected

                                        StringBuilder toSend = new StringBuilder();
                                        File[] existingFiles = new File(getApplicationInfo().dataDir + "/Logs").listFiles(); //List of saved scouting logs
                                        for (File f : existingFiles) {

                                            BufferedReader reader = new BufferedReader(new FileReader(f));

                                            //write("" + reader.readLine() + "\n"); //Writes contents of loaded file t ooutput stream
                                            toSend.append("" + reader.readLine() + "\n");

                                            reader.close();

                                        }

                                        write(toSend.toString());

                                    }

                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            connectionInfo.setText("Sending Message");
                                        }
                                    });

                                    write("end"); //causes the computer to stop listening for messages and send its own end message

                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            connectionInfo.setText("Message Sent");
                                        }
                                    });

                                    try {
                                        Thread.sleep(500);
                                    } catch (Exception e) {
                                        Log.i("A", "Error Waiting");
                                    }
                                    boolean received = waitForEnd(); //waits until it times out or receives the end message

                                    if (!received) {
                                        Log.i("A", "Error Sending Message");
                                        runOnUiThread(new Runnable() {
                                            public void run() {
                                                connectionInfo.setText("Error Sending Message");
                                                AlertDialog.Builder builder = new AlertDialog.Builder(SendMessageActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                                builder.setMessage("The Message Failed To Send. Please Try Again.").setTitle("Not Sent").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                }).setCancelable(false).setIcon(R.mipmap.ic_launcher); //TODO: Change icon
                                                builder.create();
                                                builder.show();
                                            }
                                        });
                                    }

                                    socket.close();
                                } catch (IOException e) {
                                    Log.i("A", "Error Sending Message");
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            connectionInfo.setText("Error Sending Message");
                                            AlertDialog.Builder builder = new AlertDialog.Builder(SendMessageActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                                            builder.setMessage("The Message Failed To Send. Please Try Again.").setTitle("Not Sent").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            }).setCancelable(false).setIcon(R.mipmap.ic_launcher);
                                            builder.create();
                                            builder.show();
                                        }
                                    });

                                    e.printStackTrace();
                                }
                                connected = false;
                            }
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    sendButton.setVisibility(View.VISIBLE);
                                }
                            });
                            sending = false;
                        }
                    }).start();
        }
    }

    public void connect() {
        //When the connect button is pushed
        if (!connected) {
            computerAddress = connectionMAC.getText().toString();
            runOnUiThread(new Runnable() {
                public void run() {
                    connectionInfo.setText("Connecting...");
                }
            });
            try {
                init();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (connected) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        connectionInfo.setText("Connected");
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        connectionInfo.setText("Error: Connection Failed. Do You Have The Right MAC Address?");
                        AlertDialog.Builder builder = new AlertDialog.Builder(SendMessageActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        builder.setMessage("The Message Failed To Send. Please Try Again.").setTitle("Not Sent").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setCancelable(false).setIcon(R.mipmap.ic_launcher);
                        builder.create();
                        builder.show();
                    }
                });
            }
        }
    }

    /**
     *  @return true if end is recieved in the time limit, false otherwise
     *  Waits until it recieves end or times out
     */

    public boolean waitForEnd() {

        boolean result = false;

        Log.i("A", "Waiting for end");
        try {
            byte[] bytes = new byte[3];
            Log.i("A", "Checking For End");
            int bytesRead = inStream.read(bytes, 0, bytes.length);
            String recieved = new String(bytes, "UTF-8");

            if (recieved.equals("end")) { //check if the message is end
                result = true;
                Log.i("A", "Received End");
                //onRequestPermissionsResult(Constants.DELETE_LOG_REQUEST, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new int[]{PackageManager.PERMISSION_GRANTED});
            }
        } catch (Exception e) {
            Log.i("A", "Error While Waiting For End, Aborting");
            for (StackTraceElement s : e.getStackTrace()) {
                Log.i("A", s.toString());
            }
        }

        return result;

    }

    /**Creates the connection between the client and server apps, requires that the two devices are paired
     *
     * @throws IOException never gonna happen
     */

    private void init() throws IOException {

        int REQUEST_ENABLE_BT = 1;

        if (mBluetoothAdapter == null) {
            /* Device doesn't support Bluetooth
            app crashes if this is true, put error handling code in here


             */
            System.out.println("Crashed.");

        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    System.out.println("Got to this step, but didn't work.");
                    return;
                }
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); //Enables bluetooth, gets permissions
            } catch (Exception e) {
                Log.i("A", "Error Enabling Bluetooth");
            }
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceHardwareAddress = device.getAddress(); // MAC addresses of paired devices

                if (device.getAddress().toString().equals(computerAddress)) {
                    hostComputer = device;
                }


            }

        }

        try {
            if (!(hostComputer == null)) {
                socket = hostComputer.createRfcommSocketToServiceRecord(ConnectToUUID);
                socket.connect();
                connected = true;
                outputStream = socket.getOutputStream();
                inStream = socket.getInputStream();
                write(mBluetoothAdapter.getName().toString());
            } else {
                connected = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**Writes a string to the output stream between this and the paired device using the bluetooth connection
     *
     * @param s String to write to output stream
     * @throws IOException
     */

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == Constants.DELETE_LOG_REQUEST) {

            Log.i("A", "Received response for write permission request.");
            existingFiles = new File(getApplicationInfo().dataDir + "/Logs").listFiles();

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //When write permission is granted
                //Deletes file
                for (File existingFile : existingFiles) {
                    if (existingFile.delete()) {
                        Log.i("A", "File deleted");
                    } else {
                        Log.i("A", "Delete failed");
                    }
                }
            } else {
                Log.i("A", "Write permission was NOT granted.");
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}