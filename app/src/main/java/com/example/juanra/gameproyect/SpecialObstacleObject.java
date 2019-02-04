package com.example.juanra.gameproyect;

import android.widget.ImageView;

public class SpecialObstacleObject extends ObstacleObject {

    private boolean active;

    public SpecialObstacleObject(ImageView image) {
        super(image);
        active = false;
    }

    public void toggleAction() {
        active = !active;
    }
}
