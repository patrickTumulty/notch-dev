package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.ceramicfx.CancelableActionPopupWindow;
import com.ptumulty.ceramic.utility.StringUtils;
import com.ptumulty.notch.AppContext;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import com.ptumulty.notch.Checklist.ChecklistCategoryManager;
import com.ptumulty.notch.Checklist.ChecklistCategoryManagerImpl;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class ChecklistCategoryManagerView extends BorderPane
{
    private final HBox topBar;
    private final ChecklistCategoryListView checklistCategoryListView;

    public ChecklistCategoryManagerView()
    {
        getStyleClass().add(".manager-view-pane");

        checklistCategoryListView = new ChecklistCategoryListView();
        topBar = new HBox();
        topBar.setAlignment(Pos.CENTER);

        Button button = new Button("+");
        topBar.getChildren().add(button);
        button.setOnAction(event -> new NameCategoryPopup());

        configureVisuals();
    }

    public ChecklistCategoryListView getListView()
    {
        return checklistCategoryListView;
    }

    private void configureVisuals()
    {
        setTop(topBar);
        setCenter(checklistCategoryListView);
    }

    private static class NameCategoryPopup
    {
        private final TextArea defaultChecklistItems;
        private final TextField categoryNameField;

        NameCategoryPopup()
        {
            VBox vBox = new VBox();
            vBox.setSpacing(10);
            vBox.setPadding(new Insets(10,10,0,10));
            vBox.setAlignment(Pos.CENTER);

            categoryNameField = new TextField();
            categoryNameField.setPromptText("Category Title...");
            vBox.getChildren().add(categoryNameField);

            defaultChecklistItems = new TextArea();
            vBox.getChildren().add(defaultChecklistItems);

            EventHandler<ActionEvent> actionEvent = event -> createCategory();

            CancelableActionPopupWindow popupWindow = new CancelableActionPopupWindow(vBox, "Create", actionEvent);
            popupWindow.setStylesheet("css/main-stylesheet.css");
            popupWindow.setWindowTitle("Checklist Category Creation");
            popupWindow.setActionButtonID("action-button");
            popupWindow.show(300, 300);
        }

        private void createCategory()
        {
            String categoryName = categoryNameField.getText().isBlank() ?
                                  "Untitled" :
                                  categoryNameField.getText();

            try
            {
                ChecklistCategory checklistCategory = new ChecklistCategory(categoryName);

                List<String> defaults = StringUtils.parseMultilineStringToList(defaultChecklistItems.getText());
                checklistCategory.setDefaultChecklistTasks(defaults);
                AppContext.get().getBean(ChecklistCategoryManager.class).addChecklistCategory(checklistCategory);
            }
            catch (ChecklistCategoryManagerImpl.CategoryAlreadyExistsException e)
            {
                /*
                TODO: Check here for how many of this already existing name shows up and append a number at the end.
                 */
            }
        }

    }
}
