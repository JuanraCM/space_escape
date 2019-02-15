package com.example.juanra.gameproyect;

import android.widget.ImageView;

public class SpecialSpaceship extends Spaceship {

    private boolean active;
    private int wallWidthChanged;

    public SpecialSpaceship(ImageView image, int increment, int movementPoints) {
        super(image, increment, movementPoints);
        active = false;
    }

    public void toggleAction() {
        active = !active;
    }

    public int getWallWidthChanged() {
        return wallWidthChanged;
    }

    public void setWallWidthChanged(int width) {
        this.wallWidthChanged = width;
    }
}
