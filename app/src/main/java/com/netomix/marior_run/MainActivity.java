package com.netomix.marior_run;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameView vista = new GameView(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // setContentView(R.layout.activity_main);
        setContentView(vista);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }
}
