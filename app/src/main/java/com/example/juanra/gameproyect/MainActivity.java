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
    private ImageView player;
    private Drawable playerRight, playerLeft;

    // Tamaño de player
    private int playerSize;

    // Posicion de los elementos
    private float playerX, playerY;

    // Scoreboard
    private TextView scoreBoard, highScoreLabel;
    private int score, highScore, timeCount;

    // Elementos de clase
    private Timer timer;
    private Handler handler = new Handler();

    // Variables de estado de los objetos
    private boolean hasStarted = false;
    private boolean doingSomething = false;
    private boolean tiltRight = false;

    // Sensor acelerometro
    private SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameFrame = findViewById(R.id.gameFrame);
        gameLayout = findViewById(R.id.gameLayout);
        player = findViewById(R.id.player);
        scoreBoard = findViewById(R.id.scoreboard);
        highScoreLabel = findViewById(R.id.highScoreLabel);

        playerLeft = getResources().getDrawable(R.drawable.balloon_left);
        playerRight = getResources().getDrawable(R.drawable.balloon_right);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    public void startGame(View view) {
        hasStarted = true;
        gameLayout.setVisibility(View.INVISIBLE);

        // Inicializa las variables que contienen el tamaño del frame si no lo estaban
        if (frameHeight == 0) {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();
            initialframeWidth = frameWidth;

            playerSize = player.getHeight();
            playerX = player.getX();
            playerY = player.getY();
        }

        // Seteamos las posiciones de los elementos (3000.0f es fuera del frame)
        player.setX(0.0f);

        // Hacemos visibles los elementos
        player.setVisibility(View.VISIBLE);

        timeCount = 0;
        score = 0;
        scoreBoard.setText("Score: 0");

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (hasStarted) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //changePos();
                        }
                    });
                }
            }
        }, 0, 20);
    }

    /*public void changePos() {  Metodo control por touchEvent
        // Mueve el jugador
        if (doingSomething) {
            playerX += 14;
            player.setImageDrawable(playerRight);
        } else {
            playerX -= 14;
            player.setImageDrawable(playerLeft);
        }

        // Controlamos que el jugados este en algun extremo
        if (playerX < 0) {
            playerX = 0;
            player.setImageDrawable(playerRight);
        }

        if (frameWidth - playerSize < playerX) {
            playerX = (frameWidth - playerSize);
            player.setImageDrawable(playerLeft);
        }

        // Actualizamos la posicion de player
        player.setX(playerX);
    }*/
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

    public void quitGame(View view) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (event.values[0] < 0) {
                System.out.println("You are moving to the right");
                updatePos(true);
            } else {
                System.out.println("You are moving to the left");
                updatePos(false);
            }
        }
    }

    private void updatePos(boolean b) {
        // Mueve el jugador
        if (b) {
            playerX += 14;
            player.setImageDrawable(playerRight);
        } else {
            playerX -= 14;
            player.setImageDrawable(playerLeft);
        }

        // Controlamos que el jugados este en algun extremo
        if (playerX < 0) {
            playerX = 0;
            player.setImageDrawable(playerRight);
        }

        if (frameWidth - playerSize < playerX) {
            playerX = (frameWidth - playerSize);
            player.setImageDrawable(playerLeft);
        }

        // Actualizamos la posicion de player
        player.setX(playerX);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onStart() {
        super.onStart();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }
}
