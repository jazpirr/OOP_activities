package com.example.database;

public class Student {
    static int ID = 20250000;
    int id;
    String name;
    String course;

    public Student(String name, String course) {
        this.name = name;
        this.course = course;
        id = ++ID;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", course='" + course + '\'' +
                '}';
    }
}
