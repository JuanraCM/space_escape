package com.example.juanra.gameproyect;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class SpaceshipCreator {

    public static Spaceship[] createSpaceships() {
        Spaceship[] spaceships = {
                new Spaceship(10, 8, 2),
                new Spaceship(15, 10, 1),
                new Spaceship(20, 6, 3),
                new Spaceship(5, 7, 1),
                new Spaceship(5, 6, 2),
                new Spaceship(30, 5, 5),
                new Spaceship(20, 7, 2),
        };
        return spaceships;
    }
}
