package com.example.juanra.gameproyect;

import android.widget.ImageView;

public class WallLaser extends SpaceshipObject {

    public WallLaser() {
    }

    public WallLaser(ImageView image) {
        super(image, 3000f, image.getY());
    }
}
