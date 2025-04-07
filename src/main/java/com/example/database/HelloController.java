package com.example.database;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    public VBox vbMain;

    public void onEnrollClicked(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) vbMain.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("add_student.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 533, 538);
        stage.setMinHeight(533);
        stage.setMinWidth(538);
        stage.setTitle("Hello!");
        stage.setScene(scene);

    }

    public void onCourseClicked(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) vbMain.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("modify_course.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setMinHeight(533);
        stage.setMinWidth(538);
        stage.setTitle("Hello!");
        stage.setScene(scene);
    }
}