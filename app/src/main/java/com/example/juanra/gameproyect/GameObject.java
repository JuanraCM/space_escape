package com.example.juanra.gameproyect;

import android.widget.ImageView;

public class GameObject {

    private ImageView image;
    private float objectX;
    private float objectY;

    public GameObject() {

    }

    public GameObject(ImageView image, float objectX) {
        this.image = image;
        this.objectX =objectX;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public float getObjectX() {
        return objectX;
    }

    public void setObjectX(float objectX) {
        this.objectX = objectX;
    }
}
