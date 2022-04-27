package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements Runnable{

    public long cookies = 0;
    public int cookiesPerClick = 1;
    public double cookiesPerSecond;
    public Timer timer;
    public TimerTask timerTask;
    public boolean timerOn;
    public int cursors, grandmas, bakers;
    //how many cookies/sec each autoclicker gives
    public final double CURSOR_MULTIPLIER = 0.1;
    public final double GRANDMA_MULTIPLIER = 0.5;
    public final double BAKER_MULTIPLIER = 1;
    //what the price of each autoclicker is multiplied by each time you buy one
    public final double PRICE_MULTIPLIER = 1.1;

    ArrayList<Autoclicker> autoClickers;
    public MainActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoClickers = new ArrayList<>();
        timerOn = false;
        timerTask = new TimerTask() {

            @Override
            public void run() {
                cookies += cookiesPerSecond;
                displayCookies();
            }
        };
        timer.schedule(timerTask, 0, 1000);


    }

    @Override
    public void run(){
        for(Autoclicker autoclicker : autoClickers){
            autoclicker.run();

        }
    }

    public void displayCookies(){
        TextView textView = (TextView)findViewById(R.id.cookie_counter);
        textView.setText(String.valueOf(cookies));
    }

    public void setTimer(){
        timer = new Timer();

    }


    public void buttonPressed(View view){

            switch(view.getId()) {

                case R.id.cookie_button:
                cookies += cookiesPerClick;
                displayCookies();
                break;


            }
        }


    }

