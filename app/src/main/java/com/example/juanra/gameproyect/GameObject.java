package com.example.juanra.gameproyect;

import android.widget.ImageView;

/*
*
* Superclase que almacena los datos que tienen en comun tanto el objeto jugador
* como los obstaculos del juego
*
* */

public class GameObject {

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

    public void setObjectX(float objectX) {
        this.objectX = objectX;
    }

    public float getObjectY() {
        return objectY;
    }

    public void setObjectY(float objectY) {
        this.objectY = objectY;
    }

    public void setImageX(float x) {
        image.setX(x);
    }

    public void setImageVisibility(int visibility) {
        image.setVisibility(visibility);
    }
}
