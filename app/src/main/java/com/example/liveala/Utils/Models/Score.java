package com.example.liveala.Utils.Models;

import java.io.Serializable;

public class Score implements Serializable {
    String criterion;
    int points;

    public Score() {
    }

    public Score(String criterion, int points) {
        this.criterion = criterion;
        this.points = points;
    }

    public String getCriterion() {
        return criterion;
    }

    public int getPoints() {
        return points;
    }
}
