package com.example.busapp;

import android.graphics.drawable.Drawable;

import org.w3c.dom.Text;

public class BusStation {
    private String text;
    private Drawable d;

    public void setD(Drawable d){
        this.d = d;
    }

    public void setText(String t){
        this.text = t;
    }

    public String getText(){
        return text;
    }

    public Drawable getD(){
        return d;
    }
}




