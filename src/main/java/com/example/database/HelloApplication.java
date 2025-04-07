package com.example.database;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HelloApplication extends Application {
    public static final String URL = "jdbc:mysql://localhost:3306/csit228f3";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";




    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("test1.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Testing!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try(Connection con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            Statement stm = con.createStatement()){

            String queryEnroll = "CREATE TABLE IF NOT EXISTS enrolled ("+
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL, " +
                    "course VARCHAR(100) NOT NULL" +
                    ");";

            String queryCourse = "CREATE TABLE IF NOT EXISTS course ("+
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "course VARCHAR(100) NOT NULL, " +
                    "course_name VARCHAR(100) NOT NULL" +
                    ");";

            stm.execute(queryCourse);
            stm.execute(queryEnroll);
            System.out.println("Successfully created tables!");
        } catch (SQLException e){
            throw new RuntimeException(e);
        }

        launch();
    }
}