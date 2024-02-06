package com.example.collegeschedulerappfinal;

public class AssignmentModel {
    private String title;
    private String dueDate;
    private String associatedClass;

    public AssignmentModel(String title, String dueDate, String associatedClass) {
        this.title = title;
        this.dueDate = dueDate;
        this.associatedClass = associatedClass;
    }

    public String getTitle() {
        return title;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getAssociatedClass() {
        return associatedClass;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public void setAssociatedClass(String associatedClass) {
        this.associatedClass = associatedClass;
    }
}
