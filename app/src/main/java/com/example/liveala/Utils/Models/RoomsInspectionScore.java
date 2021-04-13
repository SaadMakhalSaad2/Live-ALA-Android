package com.example.liveala.Utils.Models;

import java.io.Serializable;
import java.util.List;

public class RoomsInspectionScore implements Serializable {
    UserProfile student;
    List<Score> scores;
    int totalScore;
}
