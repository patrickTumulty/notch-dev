package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.notch.ChecklistUI.popups.ConfigureCategoryPopupWindow;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class ChecklistCategoryManagerView extends BorderPane
{
    private final ChecklistCategoryListView checklistCategoryListView;

    public ChecklistCategoryManagerView()
    {
        getStyleClass().add(".manager-view-pane");

        checklistCategoryListView = new ChecklistCategoryListView();

        configureVisuals();
    }

    public ChecklistCategoryListView getListView()
    {
        return checklistCategoryListView;
    }

    private void configureVisuals()
    {
        Button createButton = new Button("New Category");
        FontIcon plusIcon = new FontIcon(FontAwesomeSolid.PLUS);
        plusIcon.setIconColor(Color.WHITE);
        createButton.setGraphic(plusIcon);
        widthProperty().addListener((observable, oldValue, newValue) -> createButton.setPrefWidth(newValue.doubleValue()));
        createButton.setOnAction(event -> new ConfigureCategoryPopupWindow());

        setTop(createButton);
        setCenter(checklistCategoryListView);
    }
}
