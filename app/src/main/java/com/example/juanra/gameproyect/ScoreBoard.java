package com.example.juanra.gameproyect;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ScoreBoard {

    private TextView scoreBoard, highScoreLabel;
    private int currentScore, highScore;

    public ScoreBoard(TextView scoreBoard, TextView highScoreLabel) {
        this.scoreBoard = scoreBoard;
        this.highScoreLabel = highScoreLabel;

        currentScore = 0;
        setDefaultTitle();
    }

    public void increment(int i) {
        currentScore += i;
        scoreBoard.setText("Score: " + currentScore);
    }

    public TextView getScoreBoard() {
        return scoreBoard;
    }

    public void setScoreBoard(TextView scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    public TextView getHighScoreLabel() {
        return highScoreLabel;
    }

    public void setHighScore(int score) {
        this.highScore = score;
        this.highScoreLabel.setText("HIGH SCORE: " + score);
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
        scoreBoard.setText("Score: " + currentScore);
    }

    public int getHighScore() {
        return highScore;
    }

    public void setDefaultTitle() {
        scoreBoard.setText("SCOREBOARD");
    }
}
