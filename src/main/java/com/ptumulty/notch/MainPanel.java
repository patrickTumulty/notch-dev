package com.ptumulty.notch;

import com.ptumulty.notch.ChecklistUI.ChecklistCategoryManagerView;
import com.ptumulty.notch.ChecklistUI.ExtendedChecklistTableView;
import javafx.scene.layout.BorderPane;

public class MainPanel extends BorderPane
{
    private int MIN_WIDTH = 600;
    private int MIN_HEIGHT = 600;
    private int PREF_WIDTH = 1000;
    private int PREF_HEIGHT = 600;

    private final ExtendedChecklistTableView extendedChecklistTableView;
    private final ChecklistCategoryManagerView categoryManagerView;

    MainPanel()
    {
        extendedChecklistTableView = new ExtendedChecklistTableView();
        categoryManagerView = new ChecklistCategoryManagerView();

        setPrefSize(PREF_WIDTH, PREF_HEIGHT);
        setMinSize(MIN_WIDTH, MIN_HEIGHT);

        configureLayout();
    }

    private void configureLayout()
    {
        setLeft(categoryManagerView);
        setCenter(extendedChecklistTableView);
    }


}
