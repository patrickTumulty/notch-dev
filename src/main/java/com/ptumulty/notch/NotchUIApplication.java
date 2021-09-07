package com.ptumulty.notch;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

public class NotchUIApplication extends Application
{
    private ConfigurableApplicationContext applicationContext;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void init() throws Exception
    {
        super.init();
        applicationContext = new SpringApplicationBuilder(NotchApplication.class).run();
    }

    @Override
    public void start(Stage primaryStage)
    {
        applicationContext.publishEvent(new StageReadyEvent(primaryStage));
    }

    static class StageReadyEvent extends ApplicationEvent
    {
        public StageReadyEvent(Stage stage)
        {
            super(stage);
        }

        public Stage getStage()
        {
            return ((Stage) getSource());
        }
    }
}
