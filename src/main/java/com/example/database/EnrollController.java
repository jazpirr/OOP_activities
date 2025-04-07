package com.example.database;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class EnrollController {
    public TextField tfID;
    public ComboBox cbCourse;
    public TextField tfName;
    public VBox vbMain;

    public static final String URL = "jdbc:mysql://localhost:3306/csit228f3";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "";
    public ListView<Student> lvList;


    public void initialize(){
        try(Connection con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            Statement stm = con.createStatement(); ){

            String query = "Select * from enrolled";
            ResultSet resultSet = stm.executeQuery(query);

            while(resultSet.next()){
                Student s = new Student(resultSet.getString(2), resultSet.getString(3));
                lvList.getItems().add(s);
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }


        try(Connection con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            Statement stm = con.createStatement(); ){

            String query = "Select * from course";
            ResultSet resultSet = stm.executeQuery(query);

            while(resultSet.next()){
                cbCourse.getItems().add(resultSet.getString(2));
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }



        lvList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Student>() {
            @Override
            public void changed(ObservableValue<? extends Student> observable, Student s, Student t1) {
                if (t1 != null) {
                    tfID.setText(String.valueOf(t1.id));
                    tfName.setText(t1.name);
                    cbCourse.getSelectionModel().select(t1.course);
                }
            }
        });


    }

    public void onHomeClicked(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) vbMain.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("test1.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 318);
        stage.setMinHeight(318);
        stage.setMinWidth(600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
    }

    public void onUpdateClicked(ActionEvent actionEvent) {
        Student s = lvList.getSelectionModel().getSelectedItem();

        if(s != null){
            String newName = tfName.getText();
            String newCourse = cbCourse.getSelectionModel().getSelectedItem().toString();

            try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "UPDATE enrolled SET name = ?, course = ? WHERE name = ? AND course = ?"
                 )
            ) {
                preparedStatement.setString(1, newName);
                preparedStatement.setString(2, newCourse);
                preparedStatement.setString(3, s.name);
                preparedStatement.setString(4, s.course);


                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected > 0 ){
                    System.out.println("UPDATED SUCCESSFULLY");
                    s.name = newName;
                    s.course = newCourse;
                    lvList.refresh();

                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void onEnrollClicked(ActionEvent actionEvent) {
        try(Connection con = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement preparedStatement = con.prepareStatement(
                    "INSERT INTO enrolled(name, course) VALUES(?,?)"
            )){
            System.out.println("Successfully connected to database!");
            String name = tfName.getText();
            String course = cbCourse.getSelectionModel().getSelectedItem().toString();

            Student s = new Student(name,course);

            lvList.getItems().add(s);
            tfName.setText("");
            cbCourse.getSelectionModel().clearSelection();
            cbCourse.setPromptText("fasdf");
            tfID.setText("");

            preparedStatement.setString(1,name);
            preparedStatement.setString(2,course);

            int rowsAffected = preparedStatement.executeUpdate();
            if(rowsAffected > 0 ){
                System.out.println("INSERTED SUCCESSFULLY");
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void onRemoveClicked(ActionEvent actionEvent) {
        Student s = lvList.getSelectionModel().getSelectedItem();

        if (s != null) {
            try (Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                 PreparedStatement preparedStatement = con.prepareStatement(
                         "DELETE FROM enrolled WHERE name = ? AND course = ?"
                 )) {
                preparedStatement.setString(1, s.name);
                preparedStatement.setString(2, s.course);

                int rowsAffected = preparedStatement.executeUpdate();
                if(rowsAffected > 0 ){
                    System.out.println("DELETED SUCCESSFULLY");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        lvList.getItems().remove(s);
    }
}
