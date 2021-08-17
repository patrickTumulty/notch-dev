package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.utility.StringUtils;
import com.ptumulty.notch.AppContext;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import com.ptumulty.notch.Checklist.ChecklistCategoryManager;
import com.ptumulty.notch.Checklist.ChecklistCategoryManagerImpl;
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

    private class NameCategoryPopup
    {
        private Stage stage;
        private VBox vBox;
        private TextArea defaultChecklistItems;
        private TextField categoryNameField;

        NameCategoryPopup()
        {
            configureStage();

            configureVBox();

            categoryNameField = new TextField();
            categoryNameField.setPromptText("Title...");
            vBox.getChildren().add(categoryNameField);

            defaultChecklistItems = new TextArea();
            vBox.getChildren().add(defaultChecklistItems);

            configureButtons();

            stage.setScene(new Scene(vBox, 300, 300));
            stage.show();
        }

        private void configureVBox()
        {
            vBox = new VBox();
            vBox.setSpacing(10);
            vBox.setPadding(new Insets(10));
            vBox.setAlignment(Pos.CENTER);
        }

        private void configureButtons()
        {
            Button createButton = new Button("Create");
            createButton.setOnAction(event -> createCategory());

            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(event -> stage.close());

            HBox hBox = new HBox(cancelButton, createButton);
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);

            vBox.getChildren().add(hBox);
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
            stage.close();
        }

        private void configureStage()
        {
            stage = new Stage();
            stage.setTitle("Create Category");
            stage.initModality(Modality.APPLICATION_MODAL);
        }

    }
}
