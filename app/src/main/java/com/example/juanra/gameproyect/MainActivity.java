package com.example.juanra.gameproyect;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    private Player player;
    private Spaceship currentShip;
    private SpaceshipObject playerShot;
    private WallLaser laserWall;
    private ImageView warningArrow;
    private ScoreBoard scoreBoard;
    private ImageView currentShipImage;

    // Diferentes naves
    private Spaceship[] spaceships;

    // Tamaño y posiciones auxiliares de los elementos
    private int playerSize;
    private float playerX, playerY;
    private float shipX, shipY;
    private float playerShotX, playerShotY;
    private float laserX;

    // Variables de estado de los objetos
    private boolean hasStarted = false;
    private boolean laserAction = false;
    private boolean movingRight = false;
    private boolean movingLeft = false;
    private boolean shooting = false;
    private boolean canShoot = true;

    // Sensor acelerometro
    private SensorManager sensorManager;

    // Variable random para la posicion X de los objetos
    private Random rand;

    // TimeCount
    private int timeCount = 0;

    // Timer para el metodo updatePos
    private Timer timer;
    private Handler handler = new Handler();

    // Variable para cargar el score
    private SharedPreferences settings;

    // Variable para reproducir sonidos
    private SoundPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameFrame = findViewById(R.id.gameFrame);
        gameLayout = findViewById(R.id.gameLayout);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        timer = new Timer();

        // Inicializa el scoreboard
        TextView score = findViewById(R.id.scoreboard);
        TextView hiScoreLabel = findViewById(R.id.highScoreLabel);
        scoreBoard = new ScoreBoard(score, hiScoreLabel);

        // Carga el score mas alto guardado en el dispositivo
        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        scoreBoard.setHighScore(settings.getInt("HIGH_SCORE", 0));

        soundPlayer = new SoundPlayer(this);
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
        currentShip.setImageVisibility(View.VISIBLE);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (hasStarted) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            updatePos();
                        }
                    });
                }
            }
        }, 0, 20);

        gameFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canShoot) {
                    shooting = true;
                    soundPlayer.playLaserShotSound();
                }
            }
        });
    }

    private void initializeGameElements() {
        // Inicializa el objeto player
        ImageView playerImage = findViewById(R.id.player);
        playerImage.getHeight();
        Drawable playerLeft = getResources().getDrawable(R.drawable.player_left);
        Drawable playerRight = getResources().getDrawable(R.drawable.player_right);
        Drawable playerQuiet = getResources().getDrawable(R.drawable.player_quiet);

        player = new Player(playerImage, playerLeft, playerRight, playerQuiet);

        // Inicializa los elementos que van cayendo
        rand = new Random();

        currentShipImage = findViewById(R.id.currentSpaceship);
        ImageView playerShotImage = findViewById(R.id.laserShot);
        ImageView laserImage = findViewById(R.id.laserBeam);
        warningArrow = findViewById(R.id.laserHint);

        spaceships = SpaceshipCreator.createSpaceships();
        setCurrentShip();

        playerShot = new SpaceshipObject(playerShotImage, player.getObjectX(), player.getObjectY());
        laserWall = new WallLaser(laserImage);
    }

    private void setCurrentShip() {
        int nShip = rand.nextInt(spaceships.length);
        currentShip = spaceships[nShip];

        switch (nShip) {
            case 0 : currentShipImage.setImageDrawable(getResources().getDrawable(R.drawable.spaceship_1));
                break;
            case 1 : currentShipImage.setImageDrawable(getResources().getDrawable(R.drawable.spaceship_2));
                break;
            case 2 : currentShipImage.setImageDrawable(getResources().getDrawable(R.drawable.spaceship_3));
                break;
            case 3 : currentShipImage.setImageDrawable(getResources().getDrawable(R.drawable.spaceship_4));
                break;
            case 4 : currentShipImage.setImageDrawable(getResources().getDrawable(R.drawable.spaceship_5));
                break;
            case 5 : currentShipImage.setImageDrawable(getResources().getDrawable(R.drawable.spaceship_6));
                break;
            case 6 : currentShipImage.setImageDrawable(getResources().getDrawable(R.drawable.spaceship_7));
                break;
        }
        currentShip.setImage(currentShipImage);
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
            shipX = spaceships[0].getObjectX();
            shipY = spaceships[0].getObjectY();
            laserX = laserWall.getObjectX();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if (event.values[0] < -1) {
                movingRight = true;
                movingLeft = false;
            } else if (event.values[0] > 1) {
                movingRight = false;
                movingLeft = true;
            } else {
                movingRight = false;
                movingLeft = false;
            }
        }
    }

    private void updatePos() {
        // Variable que controla la caida de las naves
        timeCount += 20;

        // Movimiento de la nave enemiga en pantalla
        shipY += currentShip.getMovementPoints(); // Unidades que se mueve en el eje vertical
        shipY = updateShipPos(currentShip, shipX, shipY);

        // Si la y se encuentra por debajo del frame la volvemos a poner arriba y cambiamos de nave
        if (shipY > frameHeight) {
            shipY = -500;
            shipX = (float) Math.floor(rand.nextDouble() * (frameWidth - currentShip.getImageWidth()));
            playerShotY = -100f;

            setCurrentShip();
        }

        if (shooting && hit(shipX, shipY, currentShip)) {
            playerShotY = -100f;

            if (currentShip.isDead()) {
                soundPlayer.playDestroyShipSound();
                shipY = -500;
                shipX = (float) Math.floor(rand.nextDouble() * (frameWidth - currentShip.getImageWidth()));
                scoreBoard.increment(currentShip.getIncrement());
                setCurrentShip();
            }
        }

        currentShip.setImageX(shipX);
        currentShip.setImageY(shipY);

        // Laser
        if (timeCount % 10000 == 0) {
            soundPlayer.playLaserWallSound();
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

    // Devuelve true si le hemos dado a una nave
    private boolean hit(float x, float y, SpaceshipObject o) {
        if (playerShotX >= x && playerShotX <= (x + o.getImageWidth()) &&
                playerShotY >= (y - o.getImageHeight()) && playerShotY <= (y + o.getImageHeight())) {
            currentShip.damage();
            return true;
        } else return false;
    }

    private float updateShipPos(Spaceship spaceship, float spaceshipX, float spaceshipY) {
        float spaceshipCenterX = spaceshipX + spaceship.getImageWidth() / 2;
        float spaceshipCenterY = spaceshipY + spaceship.getImageHeight() / 2;

        if (hasCollided(spaceshipCenterX, spaceshipCenterY)) {
            spaceshipY = frameHeight + 100;
            endGame();
        }
        return spaceshipY;
    }

    private void endGame() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) { }

        timer.cancel();
        timer = new Timer();

        hasStarted = false;

        // Cambia la visibilidad de los elementos
        player.setImageVisibility(View.INVISIBLE);
        currentShip.setImageVisibility(View.INVISIBLE);
        laserWall.setImageVisibility(View.INVISIBLE);
        laserX = -3000f;
        gameLayout.setVisibility(View.VISIBLE);
        playerShot.setImageVisibility(View.INVISIBLE);

        // Cambiamos el valor de High Score
        if (scoreBoard.getCurrentScore() > scoreBoard.getHighScore()) {
            scoreBoard.setHighScore(scoreBoard.getCurrentScore());
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", scoreBoard.getHighScore());
            editor.apply();
        }
    }

    private void updatePlayerPos() {
        if (movingRight) {
            playerX += 10;
            player.changeDrawable(PlayerDirection.RIGHT);
        } else if (movingLeft) {
            playerX -= 10;
            player.changeDrawable(PlayerDirection.LEFT);
        } else {
            player.changeDrawable(PlayerDirection.QUIET);
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

        updateShooting();

        // Actualizamos la posicion de player
        player.setImageX(playerX);
    }

    private void updateShooting() {
        // Shooting
        if (canShoot) {
            playerShot.setImageX(playerX);
            playerShotY = playerY;
            playerShotX = playerX;
        }

        if (shooting) {
            playerShot.setImageVisibility(View.VISIBLE);
            canShoot = false;
            playerShotY -= 40;
            playerShot.setImageY(playerShotY);
        }

        if (playerShotY < 0) {
            playerShot.setImageVisibility(View.INVISIBLE);
            canShoot = true;
            shooting = false;
        }
    }

    // Comprobamos la colision por ambos lados y por arriba
    private boolean hasCollided(float x, float y) {

        return (playerX <= x && x <= playerX + playerSize &&
                playerY <= y && y <= frameHeight);
    }

    // Comprobamos si el jugador ha choquado con la pared laser
    private boolean laserWallCollided(float x) {
        float playerXWidth = playerX + playerSize;
        return (playerX >= x && playerX <= (x + laserWall.getImageWidth()) ||
                playerXWidth >= x && playerXWidth <= (x + laserWall.getImageWidth()));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void quitGame(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }
}
