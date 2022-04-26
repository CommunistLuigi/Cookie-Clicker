package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity implements Runnable{

    public long cookies = 0;
    public int cookiesPerClick = 1;
    public Timer timer;


    ArrayList<Autoclicker> autoClickers;
    public MainActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        autoClickers = new ArrayList<>();

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
        timer = new Timer(String.valueOf(1000));

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

