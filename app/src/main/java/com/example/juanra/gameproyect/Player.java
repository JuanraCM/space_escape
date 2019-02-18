package com.example.juanra.gameproyect;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class Player extends SpaceshipObject {

    private Drawable imgLeft, imgRight, imgQuiet;
    private int playerSize;

    public Player() {

    }

    public Player(ImageView image, Drawable imgLeft, Drawable imgRight, Drawable imgQuiet) {
        // 0.0f es la parte mas baja del frame
        super(image, 0.0f, image.getY());
        this.imgLeft = imgLeft;
        this.imgRight = imgRight;
        this.imgQuiet = imgQuiet;
        this.playerSize = image.getHeight();
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

    public Drawable getImgQuiet() {
        return imgQuiet;
    }

    public void setImgQuiet(Drawable imgQuiet) {
        this.imgQuiet = imgQuiet;
    }

    public int getPlayerSize() {
        return playerSize;
    }

    public void changeDrawable(PlayerDirection direction) {
        if (direction == PlayerDirection.LEFT)
            getImage().setImageDrawable(imgLeft);
        else if (direction == PlayerDirection.RIGHT)
            getImage().setImageDrawable(imgRight);
        else getImage().setImageDrawable(imgQuiet);
    }
}
