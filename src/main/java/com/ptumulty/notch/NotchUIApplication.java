package com.ptumulty.notch;

import com.ptumulty.ceramic.components.BooleanComponent;
import com.ptumulty.ceramic.models.BooleanModel;
import com.ptumulty.notch.Checklist.Checklist;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import com.ptumulty.notch.Checklist.ChecklistUtils;
import com.ptumulty.notch.ChecklistUI.ChecklistCategoryListItem;
import com.ptumulty.notch.ChecklistUI.ChecklistTableItem;
import com.ptumulty.notch.ChecklistUI.ExtendedChecklistTableView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        checklist2.addNewChecklistItems(items);

        category.getChecklists().addItem(checklist1);
        category.getChecklists().addItem(checklist2);

        ChecklistUtils.printChecklistFromCategory(category);

        ChecklistCategoryListItem tableItem = new ChecklistCategoryListItem(category);

        ExtendedChecklistTableView extendedChecklistTableView = new ExtendedChecklistTableView();
        extendedChecklistTableView.setChecklistCategoryListItem(tableItem);

        extendedChecklistTableView.setPrefSize(500, 500);

        Scene scene = new Scene(extendedChecklistTableView);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
