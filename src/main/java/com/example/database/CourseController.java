package com.example.database;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class CourseController {
    public VBox vbCourse;
    public ListView<Course> lvList;

    public static final String URL = "jdbc:mysql://localhost:3306/csit228f3";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    public TextField tf_id;
    public TextField tf_course;
    public TextField tf_name;

    public void initialize(){
        try(Connection con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            Statement stm = con.createStatement(); ){

            String query = "Select * from course";
            ResultSet resultSet = stm.executeQuery(query);

            while(resultSet.next()){
                Course s = new Course(resultSet.getString(3), resultSet.getString(2));
                lvList.getItems().add(s);
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }


        lvList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Course>() {
            @Override
            public void changed(ObservableValue<? extends Course> observable, Course s, Course t1) {
                if (t1 != null) {
                    tf_id.setText(String.valueOf(t1.id));
                    tf_name.setText(String.valueOf(t1.courseName));
                    tf_course.setText(t1.course);
                }
            }
        });

    }

    public void onHomeClicked(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) vbCourse.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("test1.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 318);
        stage.setMinHeight(318);
        stage.setMinWidth(600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
    }

    public void onUpdateClicked(ActionEvent actionEvent) {
        Course s = lvList.getSelectionModel().getSelectedItem();

        if(s != null) {
            String newCourse = tf_course.getText();
            String newCourseName = tf_name.getText();


            try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "UPDATE course SET course = ?, course_name = ? WHERE course = ? AND course_name = ?"
                 )
            ) {
                preparedStatement.setString(2, newCourseName);
                preparedStatement.setString(1, newCourse);
                preparedStatement.setString(4, s.courseName);
                preparedStatement.setString(3, s.course);
                System.out.println("SUCESS");


                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected > 0 ){
                    System.out.println("UPDATED SUCCESSFULLY");
                    s.courseName = newCourseName;
                    s.course = newCourse;
                    lvList.refresh();

                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void onAddClicked(ActionEvent actionEvent) {
        try (Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO course(course, course_name) VALUES(?,?)"
             )
        ){
            System.out.println("Database Connection Successfull");
            String name = tf_name.getText();
            String course = tf_course.getText();
            preparedStatement.setString(1,course);
            preparedStatement.setString(2,name);
            System.out.println(name);

            Course c = new Course(course, name);
            lvList.getItems().add(c);
            tf_name.setText("");
            tf_course.setText("");


            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0 ){
                System.out.println("INSERTED SUCCESSFULLY");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void onRemoveClicked(ActionEvent actionEvent) {
        Course c = lvList.getSelectionModel().getSelectedItem();

        if(c != null){
            try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                 PreparedStatement preparedStatement = con.prepareStatement(
                         "DELETE FROM course WHERE course = ? AND course_name = ?"
                 )) {
                preparedStatement.setString(2, c.course);
                preparedStatement.setString(1, c.courseName);

                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected > 0 ){
                    System.out.println("DELETED SUCCESSFULLY");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        lvList.getItems().remove(c);
    }
}
