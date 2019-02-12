package com.example.juanra.gameproyect;

import android.widget.ImageView;

public class SpecialObstacleObject extends ObstacleObject {

    private boolean active;
    private int wallWidthChanged;

    public SpecialObstacleObject(ImageView image, int increment, int movementPoints, int wallWidthChanged) {
        super(image, increment, movementPoints);
        active = false;
        this.wallWidthChanged = wallWidthChanged;
    }

    public void toggleAction() {
        active = !active;
    }

    public int getWallWidthChanged() {
        return wallWidthChanged;
    }
}
