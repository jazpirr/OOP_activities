package com.example.demo;

import java.io.Serializable;

public class Vertex implements Serializable {
    String text;
    double X;
    double Y;

    public Vertex(String text, double x, double y) {
        this.text = text;
        X = x;
        Y = y;
    }

    public String getText() {
        return text;
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    @Override
    public String toString() {
        return text;
    }
}
