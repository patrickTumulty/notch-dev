package com.ptumulty.notch;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NotchApplication extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        Scene scene = new Scene(new MainPanel());
//        Scene scene = new Scene(new CheckableRegion());
        scene.getStylesheets().add("css/main-stylesheet.css");
        scene.getStylesheets().add("css/table-view-stylesheet.css");
        scene.getStylesheets().add("css/list-view-stylesheet.css");

        primaryStage.setScene(scene);

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.show();
    }
}
