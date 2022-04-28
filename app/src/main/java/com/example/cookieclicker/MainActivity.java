package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{

    public long cookies = 0;
    public int cookiesPerClick = 1;
    public double cookiesPerSecond;
    public Timer timer;
    public TimerTask timerTask;
    TextView textView;
    public boolean timerOn;
    public int cursors, grandmas, bakers;
    //how many cookies/sec each autoclicker gives
    public final double CURSOR_MULTIPLIER = 0.1;
    public final double GRANDMA_MULTIPLIER = 0.5;
    public final double BAKER_MULTIPLIER = 1;
    //what the price of each autoclicker is multiplied by each time you buy one
    public final double PRICE_MULTIPLIER = 1.1;

    ArrayList<Autoclicker> autoClickers;
//    public MainActivity(){
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.cookie_counter);
        cookies = 0;
        cookiesPerSecond = 0;
        Object cookieLock = new Object();
        autoClickers = new ArrayList<>();
        timerOn = false;
        setTimer();
        timerTask = new TimerTask() {

            @Override
            public void run() {
                synchronized (cookieLock) {
                    cookies += cookiesPerSecond;
                    displayCookies();
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);


    }



    @SuppressLint("SetTextI18n")
    public void displayCookies(){

        textView.setText("" + cookies);
    }

    public void setTimer(){
        timer = new Timer();

    }


    @SuppressLint("NonConstantResourceId")
    public void buttonPressed(View view){

            switch(view.getId()) {

                case R.id.cookie_button:
                cookies += cookiesPerClick;
                displayCookies();
                break;
                case R.id.buy_screen_button:
                    setContentView(R.layout.shop_activity);
                break;
                case R.id.shop_cookie_counter:
                    setContentView(R.layout.activity_main);

            }
        }


    }

