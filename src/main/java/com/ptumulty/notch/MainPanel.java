package com.ptumulty.notch;

import com.ptumulty.notch.Checklist.ChecklistManager;
import com.ptumulty.notch.Checklist.ChecklistManagerImpl;
import com.ptumulty.notch.ChecklistUI.ChecklistCategoryListView;
import com.ptumulty.notch.ChecklistUI.ExtendedChecklistTableView;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class MainPanel extends BorderPane
{
    private final ExtendedChecklistTableView extendedChecklistTableView;
    private final ChecklistCategoryListView categoryListView;
    private static int count = 0;

    MainPanel()
    {
        extendedChecklistTableView = new ExtendedChecklistTableView();
        categoryListView = new ChecklistCategoryListView();

        configureLayout();
    }

    private void configureLayout()
    {
        Button button = new Button("DEBUG");
        button.setOnAction(event ->
        {
            try {
                AppContext.get().getBean(ChecklistManager.class).createAndAddChecklistCategory("Test " + count);
                count++;
            } catch (ChecklistManagerImpl.CategoryAlreadyExistsException e) {
                System.out.println("Already Exists");
            }
        });

        setTop(button);
        setCenter(extendedChecklistTableView);
        setLeft(categoryListView);
    }


}
