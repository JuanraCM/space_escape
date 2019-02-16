package com.example.juanra.gameproyect;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public class SoundPlayer {

    private AudioAttributes audioAttributes;
    final int SOUND_POOL_MAX = 3;

    private static SoundPool soundPool;
    private static int laserShotSound;
    private static int destroyShipSound;
    private static int laserWallSound;

    public SoundPlayer(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setMaxStreams(SOUND_POOL_MAX)
                    .build();
        } else {
            soundPool = new SoundPool(SOUND_POOL_MAX, AudioManager.STREAM_MUSIC, 0);
        }

        laserShotSound = soundPool.load(context, R.raw.laser_gun, 1);
        destroyShipSound = soundPool.load(context, R.raw.destroy_ship, 1);
        laserWallSound = soundPool.load(context, R.raw.laser_wall, 1);
    }

    public void playLaserShotSound() {
        soundPool.play(laserShotSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playDestroyShipSound() {
        soundPool.play(destroyShipSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playLaserWallSound() {
        soundPool.play(laserWallSound, 0.2f, 0.2f, 1, 0, 1.0f);
    }

}
