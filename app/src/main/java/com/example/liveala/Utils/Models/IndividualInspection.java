package com.example.liveala.Utils.Models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class IndividualInspection implements Serializable {

    UserProfile student;
    UserProfile inspector;
    Date date;
    int totalScore;

    List<Score> scores;

    public IndividualInspection() {
    }

    public IndividualInspection(UserProfile student, UserProfile inspector, Date date, int totalScore, List<Score> scores) {
        this.student = student;
        this.inspector = inspector;
        this.date = date;
        this.totalScore = totalScore;
        this.scores = scores;
    }

    public UserProfile getStudent() {
        return student;
    }

    public UserProfile getInspector() {
        return inspector;
    }

    public Date getDate() {
        return date;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public List<Score> getScores() {
        return scores;
    }
}
