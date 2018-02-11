package com.example.abhinabera.jarvis;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class IPAddress extends Activity {

    Button submit;
    EditText ipAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipaddress);

        submit = (Button) findViewById(R.id.submit);
        ipAdd = (EditText) findViewById(R.id.ip_address);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address = ipAdd.getText().toString();
                if(address.equals(""))
                {
                    Toast.makeText(IPAddress.this, "Please enter a valid IP.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(IPAddress.this, address, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(IPAddress.this, ActivityRecognition.class);
                    intent.putExtra("IPAddress", address);
                    startActivity(intent);
                }
            }
        });
    }
}
