package com.example.juanra.gameproyect;

import android.widget.ImageView;

public class WallObject extends SpaceshipObject {

    public WallObject() {
    }

    public WallObject(ImageView image) {
        super(image, 3000f, image.getY());
    }
}
