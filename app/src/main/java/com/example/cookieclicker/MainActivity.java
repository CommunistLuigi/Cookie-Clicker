package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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



    public static void displayCookies(){
        //code to display cookies
    }

    public void setTimer(){
        timer = new Timer(String.valueOf(1000));

    }

    //internal handler class;
    class CookieHandler implements Runnable{


        @Override
        public void run() {

        }

        public void buttonPressed(View view){
            cookies += cookiesPerClick;
            displayCookies();
        }



    }
}
