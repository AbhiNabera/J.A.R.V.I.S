package com.example.abhinabera.jarvis;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.com.bloder.magic.Magic;
import br.com.bloder.magic.view.MagicButton;

public class Graphing extends Activity {

    private String android_package_name = "com.example.avinash.task2d";
    private String unity_package_name = "com.abhinabera.ips";

    MagicButton android_icon, unity_icon, mainmenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphing);

        android_icon = (MagicButton) findViewById(R.id.android_icon);
        unity_icon = (MagicButton) findViewById(R.id.unity_icon);
        mainmenu = (MagicButton) findViewById(R.id.exit);

        android_icon.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage(android_package_name);
                if(intent != null){
                    startActivity(intent);
                }
            }
        });

        unity_icon.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getPackageManager().getLaunchIntentForPackage(unity_package_name);
                if(intent != null){
                    startActivity(intent);
                }
            }
        });

        mainmenu.setMagicButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Graphing.this, MainMenu.class);
                startActivity(i);
            }
        });
    }
}
