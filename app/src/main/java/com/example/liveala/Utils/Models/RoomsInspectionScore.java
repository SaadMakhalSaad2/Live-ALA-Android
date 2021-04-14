package com.example.liveala.Utils.Models;

import java.io.Serializable;
import java.util.List;

public class RoomsInspectionScore implements Serializable {
    List<Score> scores;
    int totalScore;

    public RoomsInspectionScore(List<Score> scores, int totalScore) {
        this.scores = scores;
        this.totalScore = totalScore;
    }

    public RoomsInspectionScore() {
    }

    public List<Score> getScores() {
        return scores;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
