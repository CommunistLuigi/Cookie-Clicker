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
    public boolean onShopScreen = false;
    public int cursorPrice, grandmaPrice, bakerPrice;

    Object cookieLock;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.cookie_counter);


        cookies = 0;


        cursors = 0;
        grandmas = 0;
        bakers = 0;

        cursorPrice = (int) (10 + cursors*PRICE_MULTIPLIER);
        grandmaPrice = (int) (100 + grandmas*PRICE_MULTIPLIER);
        bakerPrice = (int) (1000 + bakers*PRICE_MULTIPLIER);

        cookiesPerSecond = cursors * CURSOR_MULTIPLIER
                + (int) grandmas * GRANDMA_MULTIPLIER + (int) bakers * BAKER_MULTIPLIER;


        cookieLock = new Object();

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
        try {
            textView.setText("" + cookies);
        }
        catch(Exception e){

        }
    }

    public void setTimer(){
        timer = new Timer();

    }

    @Override
    public void onBackPressed() {
        if(onShopScreen){
            setContentView(R.layout.activity_main);
            onShopScreen = false;
        }
        else{
            super.onBackPressed();
        }

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
                    onShopScreen = true;
                break;
                case R.id.back_to_main_button:
                    setContentView(R.layout.activity_main);
                    onShopScreen = false;

                case R.id.buy_autoclicker_1:
                    cookies -= cursorPrice;
                    displayCookies();
                    cursors++;
                    break;
                case R.id.buy_autoclicker_2:
                    cookies -= grandmaPrice;
                    displayCookies();
                    grandmas++;
                    break;
                case R.id.buy_autoclicker_3:
                    cookies -= bakerPrice;
                    displayCookies();
                    bakers++;
                    break;

            }
        }


    }

