package com.example.liveala.Utils.Models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class GeneralInspection implements Serializable {
    Date date;
    UserProfile inspector;
    Hall hall;
    List<Score> scores;
    int totalScore;
    

    public GeneralInspection() {
    }

    public GeneralInspection(Date date, UserProfile inspector, Hall hall, List<Score> scores, int totalScore) {
        this.date = date;
        this.inspector = inspector;
        this.hall = hall;
        this.scores = scores;
        this.totalScore = totalScore;
    }

    public Date getDate() {
        return date;
    }

    public UserProfile getInspector() {
        return inspector;
    }

    public Hall getHall() {
        return hall;
    }

    public List<Score> getScores() {
        return scores;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
