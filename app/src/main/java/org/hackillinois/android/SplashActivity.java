package org.hackillinois.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends DrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}