package com.ptumulty.notch;

import com.ptumulty.notch.Checklist.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class NotchUIApplication extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        ChecklistCategory category = new ChecklistCategory("Completion Tasks");

        List<String> items = new ArrayList<>();
        items.add("Take out Trash");
        items.add("Feed the Cat");
        items.add("Forget I Fed The Cat");

        Checklist checklist1 = new Checklist("Todo1");
        checklist1.addNewChecklistItems(items);

        Checklist checklist2 = new Checklist("Todo2");
        items.add("Feed the dog");
        checklist2.addNewChecklistItems(items);

        category.getChecklists().addItem(checklist1);
        category.getChecklists().addItem(checklist2);

        Scene scene = new Scene(new MainPanel());
        scene.getStylesheets().add("css/main-panel.css");

        try
        {
            AppContext.get().getBean(ChecklistCategoryManager.class).addChecklistCategory(category);
        }
        catch (ChecklistCategoryManagerImpl.CategoryAlreadyExistsException e)
        {
            System.out.println("Print Stuff");
        }

        primaryStage.setScene(scene);

        primaryStage.setMinHeight(600);
        primaryStage.setMinWidth(600);
        primaryStage.show();
    }
}
