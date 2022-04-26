package com.example.cookieclicker;

import android.net.wifi.p2p.WifiP2pManager;
import android.view.View;

import com.example.cookieclicker.MainActivity.*;

public class CookieHandler implements WifiP2pManager.ActionListener {

        long cookies = MainActivity.cookies;
        int cookiesPerClick = MainActivity.cookiesPerClick;
    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(int i) {

    }
    public void buttonPressed(View view){

        if(view.getId() == R.id.cookie_button) {

            cookies += cookiesPerClick;
            MainActivity.displayCookies();
        }
    }


}
