package com.ptumulty.notch;

import com.ptumulty.ceramic.models.ValueModel;
import com.ptumulty.notch.Checklist.Checklist;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import com.ptumulty.notch.Checklist.ChecklistCategoryManager;
import com.ptumulty.notch.Checklist.ChecklistCategoryManagerImpl;
import com.ptumulty.notch.ChecklistUI.ExtendedChecklistTableView;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import org.openide.util.Lookup;

import javax.swing.*;
import java.util.List;

public class MainPanel extends BorderPane
{
    private int MIN_WIDTH = 600;
    private int MIN_HEIGHT = 600;
    private int PREF_WIDTH = 1000;
    private int PREF_HEIGHT = 600;

    private final ExtendedChecklistTableView extendedChecklistTableView;


    MainPanel()
    {
        extendedChecklistTableView = new ExtendedChecklistTableView();
        extendedChecklistTableView.setPadding(new Insets(2));

        setPrefSize(PREF_WIDTH, PREF_HEIGHT);
        setMinSize(MIN_WIDTH, MIN_HEIGHT);

        configureLayout();

        DEV();
    }

    private void DEV()
    {
        ChecklistCategory category = new ChecklistCategory("Tasks Category");
        category.setCategoryTasks(List.of("A", "B", "C"));

        Checklist checklist1 = new Checklist("Task 1", category.getCategoryTasksSnapshot());
        Checklist checklist2 = new Checklist("Task 2", category.getCategoryTasksSnapshot());
        Checklist checklist3 = new Checklist("Task 3", category.getCategoryTasksSnapshot());

        category.getChecklists().addItems(List.of(checklist1, checklist2, checklist3));

        try
        {
            Lookup.getDefault().lookup(ChecklistCategoryManager.class).addChecklistCategory(category);
        } catch (ChecklistCategoryManagerImpl.CategoryAlreadyExistsException e)
        {
            /*
             * Do Nothing
             */
        }
    }

    private void configureLayout()
    {
        setCenter(extendedChecklistTableView);
    }
}
