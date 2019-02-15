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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Atributos del FrameLayout del juego
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialframeWidth, minFrameWidth;
    private LinearLayout gameLayout;

    // Elementos del juego
    private PlayerObject player;
    private Spaceship firstSpaceship;
    private SpaceshipObject misil;
    private WallObject laserWall;
    private SpecialSpaceship redMarble;
    private SpecialSpaceship blackMarble;
    private ScoreBoard scoreBoard;

    private ImageView warningArrow;

    // Tamaño y posiciones auxiliares de los elementos
    private int playerSize;
    private float playerX, playerY;
    private float blueX, blueY;
    private float misilX, misilY;
    private float laserX;
    private float arrowX;

    // Variables de estado de los objetos
    private boolean hasStarted = false;
    private boolean blackMarbleAction, redMarbleAction, laserAction = false;
    private boolean assertMovingRight = false;
    private boolean shooting = false;
    private boolean canShoot = true;

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
        firstSpaceship.setImageVisibility(View.VISIBLE);

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

        gameFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canShoot)shooting = true;
            }
        });
    }

    private void initializeGameElements() {
        // Inicializa el objeto player
        ImageView playerImage = findViewById(R.id.player);
        playerImage.getHeight();
        Drawable playerLeft = getResources().getDrawable(R.drawable.player_left);
        Drawable playerRight = getResources().getDrawable(R.drawable.player_right);

        player = new PlayerObject(playerImage, playerLeft, playerRight);

        // Inicializa los elementos que van cayendo
        rand = new Random();

        ImageView blueImage = findViewById(R.id.spaceship1);
        ImageView misilImage = findViewById(R.id.shoot);
        ImageView laserImage = findViewById(R.id.laserBeam);
        ImageView redImage = findViewById(R.id.redMarble);
        ImageView blackImage = findViewById(R.id.blackMarble);
        warningArrow = findViewById(R.id.laserHint);

        firstSpaceship = new Spaceship(blueImage, 10, 12);
        misil = new SpaceshipObject(misilImage, player.getObjectX(),player.getObjectY());
        laserWall = new WallObject(laserImage);

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
            minFrameWidth = 100;

            // Obtenemos la posicion del jugador
            playerSize = player.getPlayerSize();
            playerX = player.getObjectX();
            playerY = player.getObjectY();

            // Obtenemos la posicion de los demas objetos
            blueX  = firstSpaceship.getObjectX();
            blueY  = firstSpaceship.getObjectY();
            laserX = laserWall.getObjectX();
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
        }
    }

    private void updatePos(boolean assertMovingRight) {
        // Variable que controla la caida de las bolas
        timeCount += 20;

        // Movimiento de la bola azul
        blueY += 5; // Unidades que se mueve en el eje vertical
        blueY = updateMarblePos(firstSpaceship, blueX, blueY);

        // Si la y se encuentra por debajo del frame la volvemos a poner arriba
        if (blueY > frameHeight) {
            blueY = -500;
            blueX = (float) Math.floor(rand.nextDouble() * (frameWidth - firstSpaceship.getImageWidth()));
            misilY = -100f;
        }

        if (shooting && hit(blueX, blueY, firstSpaceship)) {
            blueY = -500;
            blueX = (float) Math.floor(rand.nextDouble() * (frameWidth - firstSpaceship.getImageWidth()));
            misilY = -100f;
            scoreBoard.increment(firstSpaceship.getIncrement());
        }

        firstSpaceship.setImageX(blueX);
        firstSpaceship.setImageY(blueY);

        // Laser
        if (timeCount % 10000 == 0) {
            laserX = (float) Math.floor(rand.nextDouble() * (frameWidth));
            warningArrow.setX(laserX);
            warningArrow.setVisibility(View.VISIBLE);
        }

        if (timeCount % 11000 == 0) {
            laserAction = true;
            warningArrow.setVisibility(View.INVISIBLE);
            laserWall.setImageX(laserX);
            laserWall.setImageVisibility(View.VISIBLE);
        }

        if (laserAction) {
            if (laserWallCollided(laserX)) {
                endGame();
            }
        }

        if (timeCount % 13000 == 0) {
            laserWall.setImageVisibility(View.INVISIBLE);
            laserWall.setImageX(3000f);
            laserAction = false;
            timeCount = 0;
        }

        // Movimiento del jugador
        updatePlayerPos();
    }

    private boolean hit(float x, float y, SpaceshipObject o) {
        Log.d("POSMISIL","Y: " + misil.getObjectY());
        Log.d("POSBOLA","Y: " + y);
        Log.d("POSBOLAW","Y: " + (y - o.getImageHeight()));
        if (misilX >= x && misilX <= (x + o.getImageWidth()) &&
                misilY >= (y - o.getImageHeight()) && misilY <= (y + o.getImageHeight())) {
            Log.d("asd", o.getImageHeight() + "");
            return true;
        }
        else return false;
    }

    private float updateMarblePos(Spaceship marble, float marbleX, float marbleY) {
        float marbleCenterX = marbleX + marble.getImageWidth() / 2;
        float marbleCenterY = marbleY + marble.getImageHeight() / 2;

        if (hasCollided(marbleCenterX, marbleCenterY)) {
            marbleY = frameHeight + 100;
            scoreBoard.increment(marble.getIncrement());

            // Cambia el ancho del frame si es una bola especial
            if (marble instanceof SpecialSpaceship) {
                frameWidth += ((SpecialSpaceship) marble).getWallWidthChanged();
                if (frameWidth > initialframeWidth) {
                    frameWidth = initialframeWidth;
                    scoreBoard.increment(50); // Bonus score
                }
                ViewGroup.LayoutParams modified = gameFrame.getLayoutParams();
                modified.width = frameWidth;

                gameFrame.setLayoutParams(modified);
                if (minFrameWidth > frameWidth) {
                    endGame();
                }
            }
        }
        return marbleY;
    }

    private void endGame() {
        timer.cancel();
        timer = new Timer();

        hasStarted = false;

        // Cambia la visibilidad de los elementos
        player.setImageVisibility(View.INVISIBLE);
        firstSpaceship.setImageVisibility(View.INVISIBLE);
        laserWall.setImageVisibility(View.INVISIBLE);
        gameLayout.setVisibility(View.VISIBLE);

        ViewGroup.LayoutParams modified = gameFrame.getLayoutParams();
        modified.width = initialframeWidth;

        gameFrame.setLayoutParams(modified);
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

        // Shooting
        if (canShoot) {
            misil.setImageX(playerX);
            misilY = playerY;
            misilX = playerX;
        }

        if (shooting) {
            misil.setImageVisibility(View.VISIBLE);
            canShoot = false;
            misilY -= 30;
            misil.setImageY(misilY);
        }

        if (misilY < 0) {
            misil.setImageVisibility(View.INVISIBLE);
            canShoot = true;
            shooting = false;
        }

        // Actualizamos la posicion de player
        player.setImageX(playerX);
    }

    private boolean hasCollided(float x, float y) {
        // Comprobamos la colision por ambos lados y por arriba
        return (playerX <= x && x <= playerX + playerSize &&
                playerY <= y && y <= frameHeight);
    }

    private boolean laserWallCollided(float x) {
        return (playerX >= x && playerX <= (x + laserWall.getImageWidth()));
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
