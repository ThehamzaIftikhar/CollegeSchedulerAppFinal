package com.example.collegeschedulerappfinal;
public class ClassModel {
    private String className;
    private String time;
    private String instructor;

    public ClassModel(String className, String time, String instructor) {
        this.className = className;
        this.time = time;
        this.instructor = instructor;
    }

    public String getClassName() {
        return className;
    }

    public String getTime() {
        return time;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}