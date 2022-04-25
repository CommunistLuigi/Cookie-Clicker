package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    int cookies = 0;

    ArrayList<Autoclicker> autoClickers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void setTimer(){
        Timer timer = new Timer();
    }
}