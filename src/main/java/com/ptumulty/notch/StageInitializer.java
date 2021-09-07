package com.ptumulty.notch;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StageInitializer implements ApplicationListener<NotchUIApplication.StageReadyEvent>
{
    @Override
    public void onApplicationEvent(NotchUIApplication.StageReadyEvent event)
    {
        Stage stage = event.getStage();
        Scene scene = new Scene(new MainPanel());
        scene.getStylesheets().add("css/main-stylesheet.css");
        scene.getStylesheets().add("css/table-view-stylesheet.css");
        scene.getStylesheets().add("css/list-view-stylesheet.css");

        stage.setScene(scene);

        stage.setMinHeight(600);
        stage.setMinWidth(600);
        stage.show();
    }
}
