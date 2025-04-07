package com.example.database;

public class Course {
    static int ID = 202500;
    int id;
    String course;
    String courseName;

    public Course(String course, String courseName) {
        this.course = course;
        this.courseName = courseName;
        id = ++ID;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseName='" + courseName + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}
