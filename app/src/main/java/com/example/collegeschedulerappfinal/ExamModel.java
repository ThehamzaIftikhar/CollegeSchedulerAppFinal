package com.example.collegeschedulerappfinal;

public class ExamModel {
    private String examDate;
    private String examTime;
    private String examLocation;

    public ExamModel(String examDate, String examTime, String examLocation) {
        this.examDate = examDate;
        this.examTime = examTime;
        this.examLocation = examLocation;
    }

    public String getExamDate() {
        return examDate;
    }

    public String getExamTime() {
        return examTime;
    }

    public String getExamLocation() {
        return examLocation;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public void setExamTime(String examTime) {
        this.examTime = examTime;
    }

    public void setExamLocation(String examLocation) {
        this.examLocation = examLocation;
    }
}
