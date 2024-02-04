package com.example.collegeschedulerappfinal;

public class CollegeEntry {
    private String courseName;
    private String time;
    private String instructor;

    public CollegeEntry(String courseName, String time, String instructor) {
        this.courseName = courseName;
        this.time = time;
        this.instructor = instructor;
    }

    @Override
    public String toString() {
        return "Course: " + courseName + "\nTime: " + time + "\nInstructor: " + instructor;
    }
}
