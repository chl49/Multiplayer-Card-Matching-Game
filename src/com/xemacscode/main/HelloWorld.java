package com.xemacscode.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class HelloWorld extends Application{

    @Override
    public void start(Stage stage) throws Exception {
        Button btn1 = new Button("Hello World");
        StackPane root = new StackPane(); 
        root.getChildren().add(btn1);
           Scene scene = new Scene (root, 600, 400);
           stage.setScene(scene);
           stage.setTitle("First JavaFX Application");
           stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
