package com.victorio.society;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView logo;
    Animation app_splash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        logo = findViewById(R.id.logo);
        logo.startAnimation(app_splash);

        //Timer perpindahan logo ke homepage
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Menuju halaman lain
                Intent a = new Intent(MainActivity.this, SignAct.class);
                startActivity(a);
                finish();
            }
        }, 2500);



    }
}
