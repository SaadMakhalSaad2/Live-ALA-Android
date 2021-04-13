package com.example.liveala.Utils.Models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RoomsInspection  implements Serializable {
    Date date;
    String inspector;
    Hall hall;

    List<RoomsInspectionScore> scoresList;

    public RoomsInspection() {
    }

    public RoomsInspection(Date date, String inspector, Hall hall, List<RoomsInspectionScore> scoresList) {
        this.date = date;
        this.inspector = inspector;
        this.hall = hall;
        this.scoresList = scoresList;
    }

    public Date getDate() {
        return date;
    }

    public String getInspector() {
        return inspector;
    }

    public Hall getHall() {
        return hall;
    }

    public List<RoomsInspectionScore> getScoresList() {
        return scoresList;
    }
}
