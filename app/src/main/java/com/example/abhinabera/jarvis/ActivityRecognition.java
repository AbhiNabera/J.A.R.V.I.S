package com.example.abhinabera.jarvis;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import br.com.bloder.magic.view.MagicButton;

public class ActivityRecognition extends Activity {

    TextView textView1, textView2, textView3, textView4, textView5, textView6, textView7;
    CardView card1, card2, card3, card4, card5, card6, card7;
    MagicButton refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);
        textView1 = (TextView) findViewById(R.id.walking_time);
        textView2 = (TextView) findViewById(R.id.upstairs_time);
        textView3 = (TextView) findViewById(R.id.downstairs_time);
        textView4 = (TextView) findViewById(R.id.siiting_time);
        textView5 = (TextView) findViewById(R.id.standing_time);
        textView6 = (TextView) findViewById(R.id.sleeping_time);
        textView7 = (TextView) findViewById(R.id.running_time);
        card1 = (CardView) findViewById(R.id.card1);
        card2 = (CardView) findViewById(R.id.card2);
        card3 = (CardView) findViewById(R.id.card3);
        card4 = (CardView) findViewById(R.id.card4);
        card5 = (CardView) findViewById(R.id.card5);
        card6 = (CardView) findViewById(R.id.card6);
        card7 = (CardView) findViewById(R.id.card7);

        String ipAddress = getIntent().getExtras().getString("IPAddress");
        refresh = (MagicButton) findViewById(R.id.refresh);

        String totalAddress = "http://" + ipAddress;
        new JSONTask().execute(totalAddress);

        refresh.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ipAdddress = getIntent().getExtras().getString("IPAddress");
                new JSONTask().execute("http://" + ipAdddress);
            }
        });

    }

    public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";
                while((line = reader.readLine()) != null){

                    buffer.append(line);

                }

                String finalJson = buffer.toString();
                JSONObject parentObject = new JSONObject(finalJson);

                final String walking_time = parentObject.getString("walking").split(",")[0];
                final String walking_upstairs_time = parentObject.getString("walking_upstairs").split(",")[0];
                final String walking_downstairs_time = parentObject.getString("walking_downstairs").split(",")[0];
                final String sitting_time = parentObject.getString("sitting").split(",")[0];
                final String standing_time = parentObject.getString("standing").split(",")[0];
                final String sleeping_time = parentObject.getString("sleeping").split(",")[0];
                final String running_time = parentObject.getString("running").split(",")[0];

                final String walking_status = parentObject.getString("walking").split(",")[1];
                final String walking_upstairs_status = parentObject.getString("walking_upstairs").split(",")[1];
                final String walking_downstairs_status = parentObject.getString("walking_downstairs").split(",")[1];
                final String sitting_status = parentObject.getString("sitting").split(",")[1];
                final String standing_status = parentObject.getString("standing").split(",")[1];
                final String sleeping_status = parentObject.getString("sleeping").split(",")[1];
                final String running_status = parentObject.getString("running").split(",")[1];

                final float walking_time1 = Float.parseFloat(walking_time);
                final float walking_upstairs_time1 = Float.parseFloat(walking_upstairs_time);
                final float walking_downstairs_time1 = Float.parseFloat(walking_downstairs_time);
                final float sitting_time1 = Float.parseFloat(sitting_time);
                final float standing_time1 = Float.parseFloat(standing_time);
                final float sleeping_time1 = Float.parseFloat(sleeping_time);
                final float running_time1 = Float.parseFloat(running_time);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(walking_status.equals("1")){
                            card1.setCardBackgroundColor(Color.rgb(76, 175, 80));
                        }
                        else card1.setCardBackgroundColor(Color.rgb(244, 67, 54));
                        if(walking_upstairs_status.equals("1")){
                            card2.setCardBackgroundColor(Color.rgb(76, 175, 80));
                        }
                        else card2.setCardBackgroundColor(Color.rgb(244, 67, 54));
                        if(walking_downstairs_status.equals("1")){
                            card3.setCardBackgroundColor(Color.rgb(76, 175, 80));
                        }
                        else card3.setCardBackgroundColor(Color.rgb(244, 67, 54));
                        if(sitting_status.equals("1")){
                            card4.setCardBackgroundColor(Color.rgb(76, 175, 80));
                        }
                        else card4.setCardBackgroundColor(Color.rgb(244, 67, 54));
                        if(standing_status.equals("1")){
                            card5.setCardBackgroundColor(Color.rgb(76, 175, 80));
                        }
                        else card5.setCardBackgroundColor(Color.rgb(244, 67, 54));
                        if(sleeping_status.equals("1")){
                            card6.setCardBackgroundColor(Color.rgb(76, 175, 80));
                        }
                        else card6.setCardBackgroundColor(Color.rgb(244, 67, 54));
                        if(running_status.equals("1")){
                            card7.setCardBackgroundColor(Color.rgb(76, 175, 80));
                        }
                        else card7.setCardBackgroundColor(Color.rgb(244, 67, 54));


                        textView1.setText(walking_time1 + "s");
                        textView2.setText(walking_upstairs_time1 + "s");
                        textView3.setText(walking_downstairs_time1 + "s");
                        textView4.setText(sitting_time1 + "s");
                        textView5.setText(standing_time1 + "s");
                        textView6.setText(sleeping_time1 + "s");
                        textView7.setText(running_time1 + "s");
                    }
                });

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();}
            finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(ActivityRecognition.this, "Refreshed information from server", Toast.LENGTH_SHORT).show();
        }
    }

}
