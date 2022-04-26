package com.example.cookieclicker;

import static com.example.cookieclicker.MainActivity.cookiesPerClick;

import android.net.wifi.p2p.WifiP2pManager;
import android.view.View;
import static com.example.cookieclicker.MainActivity.*;

public class CookieHandler implements WifiP2pManager.ActionListener {


    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(int i) {

    }
    public static void buttonPressed(View view){

        if(view.getId() == R.id.cookie_button) {

            cookies += cookiesPerClick;
            MainActivity.displayCookies();
        }
    }


}
