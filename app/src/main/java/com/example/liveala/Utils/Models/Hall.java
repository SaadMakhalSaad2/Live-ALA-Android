package com.example.liveala.Utils.Models;

import java.io.Serializable;
import java.util.List;

public class Hall implements Serializable {
    String hallName;
    List<UserProfile> students;
    String patron;

    public Hall() {
    }

    public Hall(String hallName, List<UserProfile> students, String patron) {
        this.hallName = hallName;
        this.students = students;
        this.patron = patron;
    }

    public String getHallName() {
        return hallName;
    }

    public List<UserProfile> getStudents() {
        return students;
    }

    public String getPatron() {
        return patron;
    }
}
