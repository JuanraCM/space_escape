package com.example.juanra.gameproyect;

import android.widget.ImageView;

/*
*
* Superclase que almacena los datos que tienen en comun tanto el objeto jugador
* como los obstaculos del juego
*
* */

public abstract class GameObject {

    private ImageView image;
    private float objectX;
    private float objectY;

    public GameObject() {

    }

    public GameObject(ImageView image, float objectX, float objectY) {
        this.image = image;
        this.objectX = objectX;
        this.objectY = objectY;
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

    public float getObjectY() {
        return objectY;
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    public int getImageHeight() {
        return image.getHeight();
    }

    public void setImageX(float x) {
        image.setX(x);
    }

    public void setImageY(float y) {
        image.setY(y);
    }

    public void setImageVisibility(int visibility) {
        image.setVisibility(visibility);
    }
}
