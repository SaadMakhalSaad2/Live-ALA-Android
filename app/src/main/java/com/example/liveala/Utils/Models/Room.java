package com.example.liveala.Utils.Models;

public class Room {
    String name;
    String hall;
    UserProfile student_1;
    UserProfile student_2;

    public Room() {
    }

    public Room(String name, String hall, UserProfile student_1, UserProfile student_2) {
        this.name = name;
        this.hall = hall;
        this.student_1 = student_1;
        this.student_2 = student_2;
    }

    public String getName() {
        return name;
    }

    public String getHall() {
        return hall;
    }

    public UserProfile getStudent_1() {
        return student_1;
    }

    public UserProfile getStudent_2() {
        return student_2;
    }
}
