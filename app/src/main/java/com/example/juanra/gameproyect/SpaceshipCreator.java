package com.example.juanra.gameproyect;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class SpaceshipCreator {

    public static Spaceship[] createSpaceships() {
        Spaceship[] spaceships = {
                new Spaceship(10, 5, 2),
                new Spaceship(15, 7, 1),
                new Spaceship(20, 3, 3),
                new Spaceship(5, 4, 1),
                new Spaceship(5, 3, 2),
                new Spaceship(30, 2, 5),
                new Spaceship(20, 4, 2),
        };
        return spaceships;
    }
}
