package com.example.juanra.gameproyect;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class PlayerObject extends GameObject {

    private Drawable imgLeft, imgRight;

    public PlayerObject() {

    }

    public PlayerObject(ImageView image, float objectX, Drawable imgLeft, Drawable imgRight) {
        super(image, objectX);
        this.imgLeft = imgLeft;
        this.imgRight = imgRight;
    }

    public Drawable getImgLeft() {
        return imgLeft;
    }

    public void setImgLeft(Drawable imgLeft) {
        this.imgLeft = imgLeft;
    }

    public Drawable getImgRight() {
        return imgRight;
    }

    public void setImgRight(Drawable imgRight) {
        this.imgRight = imgRight;
    }
}
