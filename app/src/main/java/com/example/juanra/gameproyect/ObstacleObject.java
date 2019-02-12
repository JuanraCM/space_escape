package com.example.juanra.gameproyect;

import android.widget.ImageView;

public class ObstacleObject extends GameObject {

    private int increment, movementPoints;

    public ObstacleObject(ImageView image, int increment, int movementPoints) {
        super(image, 3000.0f, image.getY());
        this.increment = increment;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public int getMovementPoints() {
        return movementPoints;
    }

    public void setMovementPoints(int movementPoints) {
        this.movementPoints = movementPoints;
    }
}
