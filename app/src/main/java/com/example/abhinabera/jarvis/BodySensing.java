package com.example.abhinabera.jarvis;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import br.com.bloder.magic.view.MagicButton;

import static com.example.abhinabera.jarvis.MainMenu.bsocket;
import static com.example.abhinabera.jarvis.MainMenu.btSocket;

public class BodySensing extends Activity {


    TextView heart_rate, temperature;
    MagicButton main_menu;
    public InputStream mmInStream=null;
    public OutputStream mmOutStream=null;
    static BufferedReader in;
    String message;
    ConnectedThread mConnectedThread;
    Handler bluetoothIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_sensing);

        heart_rate = (TextView) findViewById(R.id.heart_rate);
        temperature = (TextView) findViewById(R.id.temperature);
        main_menu = (MagicButton) findViewById(R.id.main_menu);

        main_menu.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BodySensing.this, MainMenu.class);
                startActivity(i);
            }
        });
        try {
            mConnectedThread = new ConnectedThread(btSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mConnectedThread.start();

        bluetoothIn = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                if ( message.contains(",")) {
                    String heartrate = message.split(",")[0];
                    float heart = Float.parseFloat(heartrate);
                    if(heart <  60)
                    {
                        heart = 0;
                        heart_rate.setText(Float.toString(heart));
                    }
                    if(heart >= 60 && heart <= 100)
                    {
                        heart_rate.setText(Float.toString(heart));
                    }
                    else if (heart > 100 && heart <= 120)
                    {
                        heart = heart - 15;
                        heart_rate.setText(Float.toString(heart));
                    }
                    else if(heart > 120 && heart <= 140)
                    {
                        heart = heart - 35;
                        heart_rate.setText(Float.toString(heart));
                    }
                    else if(heart > 140 && heart <= 160)
                    {
                        heart = heart - 48;
                        heart_rate.setText(Float.toString(heart));
                    }
                    else if(heart > 160 && heart <= 180)
                    {
                        heart = heart - 60;
                        heart_rate.setText(Float.toString(heart));
                    }
                    else if (heart > 180 && heart <= 200)
                    {
                        heart = heart - 70;
                        heart_rate.setText(Float.toString(heart));
                    }
                    else if (heart > 180 && heart <= 200)
                    {
                        heart = heart - 85;
                        heart_rate.setText(Float.toString(heart));
                    }
                    else if (heart > 200 && heart <= 220)
                    {
                        heart = heart - 110;
                        heart_rate.setText(Float.toString(heart));
                    }
                    else if (heart > 220 && heart <= 240)
                    {
                        heart = heart - 135;
                        heart_rate.setText(Float.toString(heart));
                    }
                    temperature.setText(message.split(",")[1]);
                }
            }
        };
    }

    private class ConnectedThread extends Thread {

        public ConnectedThread(BluetoothSocket socket) throws IOException {
            InputStream tmpIn = null;
            OutputStream tmpOut=null;
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
            int bytes = 0;
            while (true&&mmInStream!=null&&mmOutStream!=null) {
                try {
                    message=in.readLine();
                    Log.d("message",message);
                    bluetoothIn.obtainMessage(0, bytes, -1, message).sendToTarget();
                } catch (IOException e) {
                    break;
                }

            }
        }
    }

}
