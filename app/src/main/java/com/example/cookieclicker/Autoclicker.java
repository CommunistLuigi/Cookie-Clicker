package com.example.cookieclicker;

import java.util.TimerTask;


public class Autoclicker extends TimerTask{

    int cookiesPerSecond, stacks;

    public Autoclicker(int speed){
        cookiesPerSecond = speed;
        stacks = 1;
    }

    public void addStacks(int stacks){
        this.stacks += stacks;
    }

    @Override
    public void run(){

    }
}
