package com.example.juanra.gameproyect;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Native;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Atributos del FrameLayout del juego
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialframeWidth;
    private LinearLayout gameLayout;

    // Elementos del juego
    private PlayerObject player;
    private GameObject obstacleBlue; // Implementar

    // Tamaño y posiciones auxiliares de los elementos
    private int playerSize;
    private float playerX, playerY;

    // Scoreboard
    private TextView scoreBoard, highScoreLabel;
    private int score, highScore, timeCount;

    // Variables de estado de los objetos
    private boolean hasStarted = false;

    // Sensor acelerometro
    private SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameFrame = findViewById(R.id.gameFrame);
        gameLayout = findViewById(R.id.gameLayout);

        scoreBoard = findViewById(R.id.scoreboard);
        highScoreLabel = findViewById(R.id.highScoreLabel);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        initializeElements();
        setGameBounds();
    }

    private void initializeElements() {
        // Inicializa el objeto player
        ImageView playerImage = findViewById(R.id.player);
        Drawable playerLeft = getResources().getDrawable(R.drawable.balloon_left);
        Drawable playerRight = getResources().getDrawable(R.drawable.balloon_right);

        player = new PlayerObject(playerImage, playerLeft, playerRight);
    }

    private void setGameBounds() {
        // Inicializa las variables que contienen el tamaño del frame, el jugador y los obstaculos
        if (frameHeight == 0) {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();
            initialframeWidth = frameWidth;

            playerSize = player.getPlayerSize();
            playerX = player.getObjectX();
            playerY = player.getObjectY();
        }
    }

    private void registerListener() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    public void startGame(View view) {
        registerListener();

        hasStarted = true;
        gameLayout.setVisibility(View.INVISIBLE);

        // Hacemos visibles los elementos
        player.setImageVisibility(View.VISIBLE);

        timeCount = 0;
        score = 0;
        scoreBoard.setText("Score: 0");
    }

    public void quitGame(View view) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (event.values[0] < 0) {
                updatePos(true);
            } else {
                updatePos(false);
            }
        }
    }

    private void updatePos(boolean assertMoving) {
        // Mueve el jugador
        if (assertMoving) {
            playerX += 14;
            player.changeDrawable(PlayerDirection.RIGHT);
        } else {
            playerX -= 14;
            player.changeDrawable(PlayerDirection.LEFT);
        }

        // Controlamos que el jugados este en algun extremo
        if (playerX < 0) {
            playerX = 0;
            player.changeDrawable(PlayerDirection.RIGHT);
        }

        if (frameWidth - playerSize < playerX) {
            playerX = (frameWidth - playerSize);
            player.changeDrawable(PlayerDirection.LEFT);
        }

        // Actualizamos la posicion de player
        player.setImageX(playerX);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (hasStarted) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                doingSomething = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                doingSomething = false;
            }
        }
        return true;
    }*/
}
