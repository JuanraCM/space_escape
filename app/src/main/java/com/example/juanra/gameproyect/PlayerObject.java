package com.example.juanra.gameproyect;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class PlayerObject extends SpaceshipObject {

    private Drawable imgLeft, imgRight;
    private int playerSize;

    public PlayerObject() {

    }

    public PlayerObject(ImageView image, Drawable imgLeft, Drawable imgRight) {
        // 0.0f es la parte mas baja del frame
        super(image, 0.0f, image.getY());
        this.imgLeft = imgLeft;
        this.imgRight = imgRight;
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

    public int getPlayerSize() {
        return playerSize;
    }

    public void changeDrawable(PlayerDirection direction) {
        if (direction == PlayerDirection.LEFT)
            getImage().setImageDrawable(imgLeft);
        else
            getImage().setImageDrawable(imgRight);
    }
}
