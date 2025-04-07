package com.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;

public class HelloController {
    public VBox vbMain;
    private StackPane selected = null;
    public AnchorPane apMain;
    List<Vertex> test1 = new ArrayList<>();

    public void initialize(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("vertex.txt"))) {
            List<Vertex> temp = (ArrayList<Vertex>)ois.readObject();
            test1.clear();
            apMain.getChildren().clear();

            for(Vertex v: temp){
                addVertex(v.getX(),v.getY(),v.getText());
            }
        } catch (FileNotFoundException e) {
            System.out.println("SERIALIZED "+test1.size());
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getClass());
        }
    }

    private void addVertex(double x, double y, String name) {
        StackPane sp = new StackPane();
        Circle c = new Circle(30);
        c.setFill(new LinearGradient(
                0.0948, 0.0, 0.237, 0.9336, true, CycleMethod.NO_CYCLE,
                new Stop(0.0, new Color(0.1806, 0.75, 0.712, 1.0)),
                new Stop(1.0, new Color(1.0, 1.0, 1.0, 1.0))));
        c.setStroke(Color.BLACK);

       Text text = new Text(name);
       text.setFont(new Font("System",20));

       sp.getChildren().addAll(c,text);
       sp.setLayoutX(x);
       sp.setLayoutY(y);

        sp.setOnMousePressed(this::onMousePressed);
        sp.setOnMouseDragged(this::onMouseDragged);
        sp.setOnMouseClicked(this::onVertexClicked);

        apMain.getChildren().addAll(sp);
        test1.add(new Vertex(name,x,y));
    }

    public void onVertexClicked(MouseEvent mouseEvent) {
        StackPane sp = (StackPane) mouseEvent.getSource();

        if(mouseEvent.getClickCount() == 2){
            TextInputDialog dialog = new TextInputDialog("What name do you want?");
            dialog.showAndWait().ifPresent(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    StackPane sp = (StackPane) mouseEvent.getSource();
                    Text text = (Text) sp.getChildren().get(1);
                    text.setText(s);
                    System.out.println(s);
                }
            });
        } else {
            if (selected != null){
                selected.setStyle("");
            }

            if (selected == sp)
                    selected = null;
            else {
                selected = sp;
                selected.setStyle("-fx-border-color: red; -fx-border-width: 2; -fx-border-radius: 100;");
            }
        }
    }

    public void onSaveClicked(ActionEvent actionEvent) {
        test1.clear();
        for (var node : apMain.getChildren()) {
            if(node instanceof StackPane sp){
                Text textNode = (Text) sp.getChildren().get(1);
                String text = textNode.getText();
                test1.add(new Vertex(text,sp.getLayoutX(),sp.getLayoutY()));
            }
        }

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("vertex.txt"))){
            oos.writeObject(test1);
        } catch (IOException e) {
            System.err.println(e.getClass());
        }
    }

    public void onAddClicked(ActionEvent actionEvent) {
        addVertex(100,100, "?");
    }

    public void onRemoveClicked(ActionEvent actionEvent) {
        if(selected != null){
            apMain.getChildren().remove(selected);
            double x = selected.getLayoutX();
            double y = selected.getLayoutY();
            test1.removeIf(v -> Math.abs(v.getX() - x) < 1 && Math.abs(v.getY() - y) < 1);
            selected = null;
        }
    }
    @FXML
    private void onMousePressed(MouseEvent event) {
        StackPane sp = (StackPane) event.getSource();
        sp.setUserData(new double[]{event.getSceneX() - sp.getLayoutX(), event.getSceneY() - sp.getLayoutY()});
    }

    @FXML
    private void onMouseDragged(MouseEvent event) {
        StackPane sp = (StackPane) event.getSource();
        double[] offset = (double[]) sp.getUserData();
        sp.setLayoutX(event.getSceneX() - offset[0]);
        sp.setLayoutY(event.getSceneY() - offset[1]);
    }
}