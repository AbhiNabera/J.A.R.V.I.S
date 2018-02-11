package com.example.abhinabera.jarvis;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import br.com.bloder.magic.view.MagicButton;

public class MainMenu extends Activity {

    MagicButton ips,body_sensing,activity_recognition,exit;

    String name,message;
    public static BluetoothAdapter myBluetooth = null;
    public Set<BluetoothDevice> pairedDevices;
    public static BluetoothSocket btSocket = null;
    private boolean isBtConnected = false, ConnectSuccess = true;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static OutputStream tmpOut = null;
    public static BluetoothSocket bsocket;
    public static String btstatus="disconnected";
    public InputStream mmInStream=null;
    public OutputStream mmOutStream=null;
    BufferedReader in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ips = (MagicButton) findViewById(R.id.ips);
        body_sensing = (MagicButton) findViewById(R.id.bodysensing);
        activity_recognition = (MagicButton) findViewById(R.id.activity_recognition);
        exit = (MagicButton) findViewById(R.id.exit);

        ips.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainMenu.this, Graphing.class);
                startActivity(i);
            }
        });
        registerbroadcast();

        body_sensing.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setup();
                pairedDevices = myBluetooth.getBondedDevices();
                ArrayList list = new ArrayList();
                if (pairedDevices.size() > 0) {
                    for (BluetoothDevice bt : pairedDevices) {
                        list.add(bt.getName() + "\n" + bt.getAddress());
                    }
                }
                final CharSequence[] dialogtext = new CharSequence[list.size()];
                int i = 0;
                while (i < list.size()) {
                    dialogtext[i] = list.get(i).toString();
                    i++;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainMenu.this);
                builder.setTitle("Available Bluetooth Devices");
                builder.setItems(dialogtext, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Log.d("pos", "" + item);
                        String info = dialogtext[item].toString();
                        name=info.split("\n")[0];
                        String add = info.substring(info.length() - 17);

                        if (btstatus.matches("disconnected")) {
                            BtConnect con = new BtConnect();
                            con.execute(add);
                        } else{
                            Toast.makeText(MainMenu.this, "Device Connected", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(MainMenu.this, BodySensing.class);
                        startActivity(i);
                    }
                }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        exit.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });

        activity_recognition.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenu.this, IPAddress.class);
                startActivity(i);
            }
        });
    }

    public void setup() {
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!myBluetooth.isEnabled()) {
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon, 1);
        }
    }

    public void registerbroadcast(){
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(mReceiver, filter1);
        this.registerReceiver(mReceiver, filter2);
        this.registerReceiver(mReceiver, filter3);
    }

    public class BtConnect extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            try {
                if (btSocket == null || !isBtConnected) {
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(params[0]);
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                    myBluetooth.cancelDiscovery();
                    btSocket.connect();
                }
            } catch (IOException e) {
                btstatus="disconnected";
                ConnectSuccess = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_SHORT).show();
            } else {
                isBtConnected = true;
                btstatus = "connected";
                Intent i=new Intent(MainMenu.this, BodySensing.class);
                startActivity(i);
            }
        }
    }

    private class ConnectedThread extends Thread {

        public ConnectedThread(BluetoothSocket socket) throws IOException {
            InputStream tmpIn = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
                bsocket = socket;

            } catch (IOException e) {
            }


            mmInStream = tmpIn;
            mmOutStream = tmpOut;
            in = new BufferedReader(new InputStreamReader(mmInStream));
        }

        public void run() {

            byte[] buffer=new byte[6000000];
            int bytes = 0;

            try {
                bytes = mmInStream.read(buffer,0,buffer.length);
                Log.d("bytesoutside",""+bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true&&mmInStream!=null&&mmOutStream!=null) {
                try {
                    message=in.readLine();
                    Log.d("message",message);
                } catch (IOException e) {
                    break;
                }

            }
        }
    }

    public final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //Device found
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                Toast.makeText(MainMenu.this,"Device Connected",Toast.LENGTH_SHORT).show();
                btstatus="connected";

            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //Done searching
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                //Device is about to disconnect
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected
                    btstatus = "disconnected";
                    btSocket=null;
                    ConnectSuccess = true;
                    isBtConnected = false;
                    Toast.makeText(MainMenu.this, "Device Disconnected", Toast.LENGTH_SHORT).show();
            }
    }};
}
