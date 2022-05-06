package com.example.cookieclicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{

    public long cookies = 0;
    public int cookiesPerClick = 1;
    public double cookiesPerSecond;
    public Timer timer;
    public TimerTask timerTask;
    TextView cookieCounterTV;
    TextView currentQuantityTV;
    public boolean timerOn;
    public int cursors, grandmas, bakers;
    //how many cookies/sec each autoclicker gives
    public final double CURSOR_MULTIPLIER = 0.1;
    public final double GRANDMA_MULTIPLIER = 0.5;
    public final double BAKER_MULTIPLIER = 1;
    //what the price of each autoclicker is multiplied by each time you buy one
    public final double PRICE_MULTIPLIER = 1.1;
    public int currentQuantity;
    public boolean onShopScreen = false;
    public int cursorPrice, grandmaPrice, bakerPrice;
    public Toast t;

    Object cookieLock;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cookieCounterTV = findViewById(R.id.cookie_counter);
        t = new Toast(this);

        cookies = 0;

        currentQuantity = 1;
        currentQuantityTV = findViewById(R.id.quantity_text_view);

        cursors = 0;
        grandmas = 0;
        bakers = 0;

        cursorPrice = (int) (10 + cursors*PRICE_MULTIPLIER);
        grandmaPrice = (int) (100 + grandmas*PRICE_MULTIPLIER);
        bakerPrice = (int) (1000 + bakers*PRICE_MULTIPLIER);

        cookiesPerSecond = cursors * CURSOR_MULTIPLIER
                + (int)( grandmas * GRANDMA_MULTIPLIER) + (int)( bakers * BAKER_MULTIPLIER);


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
           cookieCounterTV.setText("" + cookies);
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
                   resetValues();

                    onShopScreen = false;
                    break;
                case R.id.buy_autoclicker_1:
                    if(cookies >= cursorPrice) {
                        cookies -= cursorPrice;
                        displayCookies();
                        cursors += currentQuantity;
                    }
                    else{

                        t.setText("You don't have enough cookies to purchase this!");
                        t.show();
                    }
                    break;
                case R.id.buy_autoclicker_2:
                    if(cookies >= grandmaPrice) {
                        cookies -= grandmaPrice;
                        displayCookies();
                        grandmas += currentQuantity;
                    }
                    else{

                        t.setText("You don't have enough cookies to purchase this!");
                        t.show();
                    }
                    break;
                case R.id.buy_autoclicker_3:
                    if(cookies >= bakerPrice) {
                        cookies -= bakerPrice;
                        displayCookies();
                    }
                    else{
                        t.setText("You don't have enough cookies to purchase this!");
                        t.show();
                    }
                    bakers += currentQuantity;
                    currentQuantity = 1;
                    break;

                case R.id.increase_quantity:
                    currentQuantity++;
                    displayCurrentQuantity();
                    break;
                case R.id.increase_quantity_five:
                    currentQuantity += 5;
                    displayCurrentQuantity();
                    break;
                case R.id.decrease_quantity:
                    if(currentQuantity != 0) {
                        currentQuantity--;
                    }
                    else{

                        t.setText("You can't buy a negative quantity!");
                        t.show();
                    }
                    break;
                case R.id.decrease_quantity_five:
                    if(currentQuantity <= 5){
                        currentQuantity -= 5;
                    }
                    else{

                        t.setText("You can't buy a negative quantity!");
                        t.show();
                    }

                    break;
                case R.id.DaddyResetmyCookies:
                    daddyResetMyCookies();
                    break;
            }
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onStart(){
        super.onStart();
        File file = new File(this.getFilesDir(), "save.txt");
        if(file.isFile()){
            try(Scanner input = new Scanner(new FileInputStream(file))){
                String[] data = input.nextLine().split(",");
                cookies = Long.parseLong(data[0]);
                cookiesPerClick = Integer.parseInt(data[1]);
                cookiesPerSecond = Double.parseDouble(data[2]);
                cursors = Integer.parseInt(data[3]);
                grandmas = Integer.parseInt(data[4]);
                bakers = Integer.parseInt(data[5]);
                cursorPrice = Integer.parseInt(data[6]);
                grandmaPrice = Integer.parseInt(data[7]);
                bakerPrice = Integer.parseInt(data[8]);
                cookies += Duration.between(Instant.parse(data[9]), Instant.now()).getSeconds() * cookiesPerSecond;
                t.setText("Loaded from save file and updated you with an additional " +
                        Duration.between(Instant.parse(data[9]), Instant.now()).getSeconds() * (int) cookiesPerSecond + " cookies!");
                t.show();
                displayCookies();
                //needs more to update buttons in shop :3
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                t.setText("Failed to load your save file! :(");
            }
        }
        }
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onStop(){
        super.onStop();
        File file = new File(this.getFilesDir(), "save.txt");
        try(PrintWriter writer = new PrintWriter(file)){

            writer.println(cookies + "," + cookiesPerClick + "," + cookiesPerClick + "," + cursors + "," + grandmas + ","
            + bakers + "," + cursorPrice + "," + grandmaPrice + "," + bakerPrice + "," + Instant.now().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        }

        public void displayCursors(){

        }

        public void displayGrandmas(){

        }

        public void displayBakers(){

        }

        @SuppressLint("SetTextI18n")
        public void displayCurrentQuantity(){
        try{
            currentQuantityTV.setText(""+currentQuantity);
        } catch(Exception e){

            }
        }

        @SuppressLint("SetTextI18n")
        public void resetValues(){
        cookieCounterTV = findViewById(R.id.cookie_counter);
        try {
            cookieCounterTV.setText("" + cookies);
        }catch (Exception e){
            e.printStackTrace();
        }
        displayCookies();

        }

        public void daddyResetMyCookies(){
            cursors = 0;
            grandmas = 0;
            bakers = 0;
            cookies = 0;

            currentQuantity = 1;
            displayCookies();
        }

    }

