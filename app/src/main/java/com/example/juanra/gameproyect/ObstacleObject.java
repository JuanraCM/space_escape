package com.example.juanra.gameproyect;

import android.widget.ImageView;

public class ObstacleObject extends GameObject {

    private float objectY;

    public ObstacleObject(ImageView image, float objectX, float objectY) {
        super(image, objectX);
        this.objectY = objectY;
    }

    public float getObjectY() {
        return objectY;
    }

    public void setObjectY(float objectY) {
        this.objectY = objectY;
    }
}
