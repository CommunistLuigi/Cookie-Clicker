package com.example.cookieclicker;

public class Autoclicker {

    int cookiesPerSecond, stacks;

    public Autoclicker(int speed){
        cookiesPerSecond = speed;
        stacks = 1;
    }

    public void addStacks(int stacks){
        this.stacks += stacks;
    }

    public int generateCookies(){
        return cookiesPerSecond*stacks;
    }
}
