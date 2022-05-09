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

public class MainActivity extends AppCompatActivity {

    public long cookies = 0;
    public int cookiesPerClick = 1;
    public double cookiesPerSecond;
    public Timer timer;
    public TimerTask timerTask;
    TextView cookieCounterTV;
    TextView currentQuantityTV;
    public boolean timerOn;
    public TextView shopCookieCounterTV;
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

    public TextView quantityTV, cursorCounter, grandmaCounter, bakerCounter;

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

        cursorPrice = (int) (10 + cursors * PRICE_MULTIPLIER);
        grandmaPrice = (int) (100 + grandmas * PRICE_MULTIPLIER);
        bakerPrice = (int) (1000 + bakers * PRICE_MULTIPLIER);

        cookieCounterTV = findViewById(R.id.cookie_counter);
        cursorCounter = findViewById(R.id.Tier_1_AutoClicker);
        grandmaCounter = findViewById(R.id.Tier_2_AutoClicker);
        bakerCounter = findViewById(R.id.Tier_3_AutoClicker);

        cookiesPerSecond = cursors * CURSOR_MULTIPLIER
                + (int) (grandmas * GRANDMA_MULTIPLIER) + (int) (bakers * BAKER_MULTIPLIER);


        cookieLock = new Quack();

        timerOn = false;
        setTimer();
        timerTask = new TimerTask() {

            @Override

            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (cookieLock)

                        {
                            cookies += cookiesPerSecond;
                            displayCookies();
                        }
                    }


                });
            }
        };
        timer.schedule(timerTask, 0, 1000);


    }


    @SuppressLint("SetTextI18n")
    public void displayCookies() {

            if (!onShopScreen) {
                try {

                    cookieCounterTV.setText("" + cookies);
                } catch (Exception e) {
                    System.err.println("Theres errrrr");
                }
            } else {

                shopCookieCounterTV.setText("" + cookies);

            }

    }


    public void setTimer() {
        timer = new Timer();

    }

    @Override
    public void onBackPressed() {
        if (onShopScreen) {
            setContentView(R.layout.activity_main);
            onShopScreen = false;
        } else {
            super.onBackPressed();
        }

    }

    @SuppressLint("NonConstantResourceId")
    public void buttonPressed(View view) {

        switch (view.getId()) {

            case R.id.cookie_button:
                cookies += cookiesPerClick;
                displayCookies();
                break;
            case R.id.buy_screen_button:
                setContentView(R.layout.shop_activity);
                onShopScreen = true;
                setBuyScreenValues();
                displayCookies();

                break;
            case R.id.back_to_main_button:
                setContentView(R.layout.activity_main);
                onShopScreen = false;
                setCookieScreenValues();
                break;
            case R.id.buy_autoclicker_1:
                if (cookies >= (long) currentQuantity*cursorPrice) {
                    cookies -= (long) currentQuantity*cursorPrice;
                    displayCookies();
                    cursors += currentQuantity;
                    cookiesPerSecond = cursors * CURSOR_MULTIPLIER
                            + (int) (grandmas * GRANDMA_MULTIPLIER) + (int) (bakers * BAKER_MULTIPLIER);
                } else {

                    t.setText("You don't have enough cookies to purchase this!");
                    t.show();
                }
                break;
            case R.id.buy_autoclicker_2:
                if (cookies >= (long) currentQuantity*grandmaPrice) {
                    cookies -= (long) currentQuantity*grandmaPrice;
                    displayCookies();
                    grandmas += currentQuantity;
                    cookiesPerSecond = cursors * CURSOR_MULTIPLIER
                            + (int) (grandmas * GRANDMA_MULTIPLIER) + (int) (bakers * BAKER_MULTIPLIER);
                } else {

                    t.setText("You don't have enough cookies to purchase this!");
                    t.show();
                }
                break;
            case R.id.buy_autoclicker_3:
                if (cookies >= (long) currentQuantity *bakerPrice) {
                    cookies -= (long) currentQuantity*bakerPrice;
                    displayCookies();
                } else {
                    t.setText("You don't have enough cookies to purchase this!");
                    t.show();
                }
                bakers += currentQuantity;

                cookiesPerSecond = cursors * CURSOR_MULTIPLIER
                        + (int) (grandmas * GRANDMA_MULTIPLIER) + (int) (bakers * BAKER_MULTIPLIER);
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
                if (currentQuantity > 1) {
                    currentQuantity--;
                } else {

                    t.setText("You can't buy a negative quantity!");
                    t.show();
                }
                displayCurrentQuantity();
                break;
            case R.id.decrease_quantity_five:
                if (currentQuantity >= 5) {
                    currentQuantity -= 5;
                } else {

                    t.setText("You can't buy a negative quantity!");
                    t.show();
                }
                displayCurrentQuantity();

                break;
            case R.id.DaddyResetmyCookies:
                daddyResetMyCookies();
                break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onStart() {
        super.onStart();
        File file = new File(this.getFilesDir(), "save.txt");
        if (file.isFile()) {
            try (Scanner input = new Scanner(new FileInputStream(file))) {
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
    protected void onStop() {
        super.onStop();
        File file = new File(this.getFilesDir(), "save.txt");
        try (PrintWriter writer = new PrintWriter(file)) {

            writer.println(cookies + "," + cookiesPerClick + "," + cookiesPerSecond + "," + cursors + "," + grandmas + ","
                    + bakers + "," + cursorPrice + "," + grandmaPrice + "," + bakerPrice + "," + Instant.now().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    public void displayCursors() {

            cursorCounter.setText("You have " + cursors + "cursors");

    }

    @SuppressLint("SetTextI18n")
    public void displayGrandmas() {

    grandmaCounter.setText("You have " + grandmas + "grandmas");
    }

    @SuppressLint("SetTextI18n")
    public void displayBakers() {
    bakerCounter.setText("You have " + bakers + "bakers");
    }

    @SuppressLint("SetTextI18n")
    public void displayCurrentQuantity() {

            currentQuantityTV.setText("" + currentQuantity);
            if(currentQuantity == 69 || currentQuantity == 420){
                quantityTV.setText("nice");
            }
            else{
                quantityTV.setText("Quantity");
            }

    }

    @SuppressLint("SetTextI18n")
    public void setCookieScreenValues() {
        cookieCounterTV = findViewById(R.id.cookie_counter);
        cursorCounter = findViewById(R.id.Tier_1_AutoClicker);
        grandmaCounter = findViewById(R.id.Tier_2_AutoClicker);
        bakerCounter = findViewById(R.id.Tier_3_AutoClicker);
        try {
            cookieCounterTV.setText("" + cookies);
        } catch (Exception e) {
            e.printStackTrace();
        }
        displayCookies();
        displayCursors();
        displayGrandmas();
        displayBakers();

    }
    public void setBuyScreenValues(){
        shopCookieCounterTV = findViewById(R.id.shop_cookie_counter);
        currentQuantityTV = findViewById(R.id.quantity_text_view);
        currentQuantity = 1;

        quantityTV = findViewById(R.id.QuantityTv);


    }

    public void daddyResetMyCookies() {

        synchronized (cookieLock) {
            cursors = 0;
            grandmas = 0;
            bakers = 0;
            cookies = 0;


            currentQuantity = 1;

            displayCookies();
        }
    }
}

