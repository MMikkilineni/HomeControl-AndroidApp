package com.example.mmikkilineni.homecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class HomeControl extends AppCompatActivity {
    Button Discnt, Themes, fan;
    ToggleButton light1, light2, light3;
    TextView Sensordata;
    Handler bluetoothIn;
    final int handlerState = 0;          //used to identify handler message
    private BluetoothAdapter myBluetooth = null;
    public static BluetoothSocket btSocket = null;
    private StringBuilder recDataString = new StringBuilder();
    private ConnectedThread mConnectedThread;
    private static String address;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_control);

        //call the widgets
        light1 = (ToggleButton) findViewById(R.id.toggleButton);
        light2 = (ToggleButton) findViewById(R.id.toggleButton2);
        light3 = (ToggleButton) findViewById(R.id.toggleButton3);
        fan = (Button) findViewById(R.id.btn_fan);
        Discnt = (Button) findViewById(R.id.btn_disconnect);
        Themes = (Button) findViewById(R.id.btn_themes);
        Sensordata = (TextView)findViewById(R.id.txt_sensor);

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);
                    int endOfLineIndex = recDataString.indexOf("\n");
                    if (endOfLineIndex > 0) {
                        if (recDataString.charAt(0) == 'C')
                        {
                            String sensor = recDataString.substring(0, 47);
                            //String temperature = recDataString.substring(41, 46);
                            //String humidity = recDataString.substring(19, 24);
                            Sensordata.setText("" +sensor);
                        }
                        recDataString.delete(0, recDataString.length());                    //clear all string data
                    }
                }
            }
        };

        myBluetooth = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();

        //commands to be sent to bluetooth
        light1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (light1.isChecked()) {
                    if (btSocket != null) {
                        try {
                            btSocket.getOutputStream().write("1".toString().getBytes());
                        } catch (IOException e) {
                            Toast.makeText(getBaseContext(), "Error Connecting Light1", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (btSocket != null) {
                        try {
                            btSocket.getOutputStream().write("0".toString().getBytes());
                        } catch (IOException e) {
                            Toast.makeText(getBaseContext(), "Error Connecting Light1", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        light2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (light2.isChecked()) {
                    if (btSocket != null) {
                        try {
                            btSocket.getOutputStream().write("3".toString().getBytes());
                        } catch (IOException e) {
                            Toast.makeText(getBaseContext(), "Error Connecting Light2", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (btSocket != null) {
                        try {
                            btSocket.getOutputStream().write("2".toString().getBytes());
                        } catch (IOException e) {
                            Toast.makeText(getBaseContext(), "Error Connecting Light2", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        light3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (light3.isChecked()) {
                    if (btSocket != null) {
                        try {
                            btSocket.getOutputStream().write("5".toString().getBytes());
                        } catch (IOException e) {
                            Toast.makeText(getBaseContext(), "Error Connecting Light3", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    if (btSocket != null) {
                        try {
                            btSocket.getOutputStream().write("4".toString().getBytes());
                        } catch (IOException e) {
                            Toast.makeText(getBaseContext(), "Error Connecting Light3", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        Discnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Disconnect(); //close connection
            }
        });

        Themes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeControl.this, Themes.class);
                startActivity(intent);
            }
        });
    }

    private void Disconnect() {
        if (btSocket != null) //If the btSocket is busy
        {
            try {
                btSocket.close(); //close connection
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(myUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }

    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(DeviceList.EXTRA_ADDRESS);

        if(address != null) {
            //create device and set the MAC address
            BluetoothDevice device = myBluetooth.getRemoteDevice(address);

            try {
                btSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
            }
            // Establish the Bluetooth socket connection.
            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                }
            }
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();
        }
    }

    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if(myBluetooth==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (myBluetooth.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
    }
}

