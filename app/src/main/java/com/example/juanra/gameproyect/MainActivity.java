package com.example.juanra.gameproyect;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.Visibility;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.annotation.Native;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Atributos del FrameLayout del juego
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialframeWidth;
    private LinearLayout gameLayout;

    // Elementos del juego
    private PlayerObject player;
    private ObstacleObject blueMarble;
    private SpecialObstacleObject redMarble;
    private SpecialObstacleObject blackMarble;
    private ScoreBoard scoreBoard;

    // Tamaño y posiciones auxiliares de los elementos
    private int playerSize;
    private float playerX, playerY;
    private float blueX, blueY;
    private float redX, redY;
    private float blackX, blackY;

    // Variables de estado de los objetos
    private boolean hasStarted = false;
    private boolean blackMarbleAction, redMarbleAction = false;
    private boolean assertMovingRight = false;

    // Sensor acelerometro
    private SensorManager sensorManager;

    // Variable random para la posicion X de las bolas
    private Random rand;

    // TimeCount
    int timeCount = 0;

    // Timer para el metodo updatePos
    private Timer timer;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameFrame = findViewById(R.id.gameFrame);
        gameLayout = findViewById(R.id.gameLayout);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        timer = new Timer();
    }

    private void registerListener() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    public void startGame(View view) {
        hasStarted = true;

        registerListener();
        initializeGameElements();
        setGameBounds();

        // Cambiamos la visibilidad de los elementos
        gameLayout.setVisibility(View.INVISIBLE);
        player.setImageVisibility(View.VISIBLE);
        blueMarble.setImageVisibility(View.VISIBLE);
        redMarble.setImageVisibility(View.VISIBLE);
        blackMarble.setImageVisibility(View.VISIBLE);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (hasStarted) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            updatePos(assertMovingRight);
                        }
                    });
                }
            }
        },0,  20);
    }

    private void initializeGameElements() {
        // Inicializa el objeto player
        ImageView playerImage = findViewById(R.id.player);
        playerImage.getHeight();
        Drawable playerLeft = getResources().getDrawable(R.drawable.balloon_left);
        Drawable playerRight = getResources().getDrawable(R.drawable.balloon_right);

        player = new PlayerObject(playerImage, playerLeft, playerRight);

        // Inicializa los elementos que van cayendo
        rand = new Random();

        ImageView blueImage = findViewById(R.id.blueMarble);
        ImageView redImage = findViewById(R.id.redMarble);
        ImageView blackImage = findViewById(R.id.blackMarble);

        blueMarble = new ObstacleObject(blueImage, 10, 10);
        redMarble = new SpecialObstacleObject(redImage, 30, 20, 30);
        blackMarble = new SpecialObstacleObject(blackImage, -10, 10, -60);

        // Inicializa el scoreboard
        TextView score = findViewById(R.id.scoreboard);
        TextView hiScoreLabel = findViewById(R.id.highScoreLabel);

        scoreBoard = new ScoreBoard(score, hiScoreLabel);
    }

    private void setGameBounds() {
        // Inicializa las variables que contienen el tamaño del frame, el jugador y los obstaculos
        if (frameHeight == 0) {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();
            initialframeWidth = frameWidth;

            // Obtenemos la posicion del jugador
            playerSize = player.getPlayerSize();
            playerX = player.getObjectX();
            playerY = player.getObjectY();

            // Obtenemos la posicion de los demas objetos
            blueX  = blueMarble.getObjectX();
            blueY  = blueMarble.getObjectY();
            redX   = redMarble.getObjectX();
            redY   = redMarble.getObjectY();
            blackX = blackMarble.getObjectX();
            blackY = blackMarble.getObjectY();
        }
    }

    public void quitGame(View view) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (event.values[0] < 0) {
                assertMovingRight = true;
            } else {
                assertMovingRight = false;
            }
            //updatePos(assertMovingRight);
        }
    }

    private void updatePos(boolean assertMovingRight) {
        // Variable que controla la caida de las bolas
        timeCount += 20;

        // Movimiento de la bola azul
        blueY += 10; // Unidades que se mueve en el eje vertical
        blueY = updateMarblePos(blueMarble, blueX, blueY);

        // Si la y se encuentra por debajo del frame la volvemos a poner arriba
        if (blueY > frameHeight) {
            blueY = -100;
            blueX = (float) Math.floor(rand.nextDouble() * (frameWidth - blueMarble.getImageWidth()));
        }

        blueMarble.setImageX(blueX);
        blueMarble.setImageY(blueY);

        // Movimiento de la bola negra
        if (!blackMarbleAction && timeCount % 10000 == 0) {
            blackMarbleAction = true;
            blackY = -20;
            blackX = (float) Math.floor(rand.nextDouble() * (frameWidth - blackMarble.getImageWidth()));
        }

        if (blackMarbleAction) {
            blackY += 20;
            blackY = updateMarblePos(blackMarble, blackX, blackY);
        }

        if (blackY > frameHeight) blackMarbleAction = false;

        blackMarble.setImageX(blackX);
        blackMarble.setImageY(blackY);

        // Movimiento de la bola roja
        if (!redMarbleAction && timeCount % 15000 == 0) {
            redMarbleAction = true;
            redY = -20;
            redX = (float) Math.floor(rand.nextDouble() * (frameWidth - redMarble.getImageWidth()));
        }

        if (redMarbleAction) {
            redY += 25;
            redY = updateMarblePos(redMarble, redX, redY);
        }

        if (redY > frameHeight) redMarbleAction = false;

        redMarble.setImageX(redX);
        redMarble.setImageY(redY);

        // Movimiento del jugador
        updatePlayerPos();
    }

    private float updateMarblePos(ObstacleObject marble, float marbleX, float marbleY) {
        float marbleCenterX = marbleX + marble.getImageWidth() / 2;
        float marbleCenterY = marbleY + marble.getImageHeight() / 2;

        if (hasCollided(marbleCenterX, marbleCenterY)) {
            marbleY = frameHeight + 100;
            scoreBoard.increment(marble.getIncrement());

            // Cambia el ancho del frame si es una bola especial
            if (marble instanceof SpecialObstacleObject) {
                frameWidth += ((SpecialObstacleObject) marble).getWallWidthChanged();
                ViewGroup.LayoutParams modified = gameFrame.getLayoutParams();
                modified.width = frameWidth;

                gameFrame.setLayoutParams(modified);
            }
        }

        return marbleY;
    }

    private void updatePlayerPos() {
        if (assertMovingRight) {
            playerX += 10;
            player.changeDrawable(PlayerDirection.RIGHT);
        } else {
            playerX -= 10;
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

    private boolean hasCollided(float x, float y) {
        // Comprobamos la colision por ambos lados y por arriba
        return (playerX <= x && x <= playerX + playerSize &&
                playerY <= y && y <= frameHeight);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (hasStarted) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                assertMovingRight = true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                assertMovingRight = false;
            }
        }
        return true;
    }*/
}
