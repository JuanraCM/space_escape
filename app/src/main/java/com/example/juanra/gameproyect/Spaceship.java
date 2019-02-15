package com.example.juanra.gameproyect;

import android.widget.ImageView;

public class Spaceship extends SpaceshipObject {

    private int increment, movementPoints;

    public Spaceship(ImageView image, int increment, int movementPoints) {
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
