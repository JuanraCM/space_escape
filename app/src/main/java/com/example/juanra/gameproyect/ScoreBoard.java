package com.example.juanra.gameproyect;

import android.widget.TextView;

public class ScoreBoard {

    private TextView scoreBoard, highScoreLabel;
    private int currentScore;

    public ScoreBoard(TextView scoreBoard, TextView highScoreLabel) {
        this.scoreBoard = scoreBoard;
        this.highScoreLabel = highScoreLabel;

        currentScore = 0;
        scoreBoard.setText("Score: " + currentScore);
    }

    public void increment(int i) {
        currentScore += i;
        scoreBoard.setText("Score: " + currentScore);
    }
}
