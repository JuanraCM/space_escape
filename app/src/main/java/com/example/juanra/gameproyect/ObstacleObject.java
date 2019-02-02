package com.example.juanra.gameproyect;

import android.widget.ImageView;

public class ObstacleObject extends GameObject {


    public ObstacleObject(ImageView image) {
        super(image, 3000.0f, image.getY());
    }

}
