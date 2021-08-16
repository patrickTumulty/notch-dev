package com.ptumulty.notch;

import com.ptumulty.notch.Checklist.*;
import com.ptumulty.notch.ChecklistUI.ChecklistCategoryListItem;
import com.ptumulty.notch.ChecklistUI.ExtendedChecklistTableView;
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

        ChecklistUtils.printChecklistFromCategory(category);

        Scene scene = new Scene(new MainPanel());

        try
        {
            AppContext.get().getBean(ChecklistManager.class).addChecklistCategory(category);
        }
        catch (ChecklistManagerImpl.CategoryAlreadyExistsException e)
        {
            System.out.println("Print Stuff");
        }

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
