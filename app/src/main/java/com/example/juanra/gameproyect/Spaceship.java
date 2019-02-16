package com.example.juanra.gameproyect;

import android.widget.ImageView;

public class Spaceship extends SpaceshipObject {

    private int increment, movementPoints, lifePoints, currentLife;

    public Spaceship(int increment, int movementPoints, int lifePoints) {
        super(3000.0f, 0);
        this.movementPoints = movementPoints;
        this.increment = increment;
        this.lifePoints = lifePoints;
        this.currentLife = lifePoints;
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

    public boolean isDead() {
        if (currentLife == 0) {
            currentLife = lifePoints;
            return true;
        }
        return false;
    }

    public void damage() {
        currentLife--;
    }
}
