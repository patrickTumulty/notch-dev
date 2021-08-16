package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.notch.AppContext;
import com.ptumulty.notch.Checklist.ChecklistCategoryManager;
import com.ptumulty.notch.Checklist.ChecklistCategoryManagerImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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

    private void configureVisuals()
    {
        setTop(topBar);
        setCenter(checklistCategoryListView);
    }

    private class NameCategoryPopup
    {
        NameCategoryPopup()
        {
            Stage stage = new Stage();
            stage.setTitle("Create Category");
            stage.initModality(Modality.APPLICATION_MODAL);

            VBox vBox = new VBox();
            vBox.setSpacing(10);
            vBox.setPadding(new Insets(10));
            vBox.setAlignment(Pos.CENTER);

            TextField categoryNameField = new TextField();
            categoryNameField.setPromptText("Title...");

            Button createButton = new Button("Create");
            createButton.setOnAction(event ->
            {
                String categoryName = categoryNameField.getText().isBlank() ?
                                      "Untitled" :
                                      categoryNameField.getText();

                try
                {
                    AppContext.get()
                              .getBean(ChecklistCategoryManager.class)
                              .createAndAddChecklistCategory(categoryName);
                }
                catch (ChecklistCategoryManagerImpl.CategoryAlreadyExistsException e)
                {
                    System.out.println("Already Exists");
                }
                stage.close();
            });

            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(event -> stage.close());

            vBox.getChildren().add(categoryNameField);

            HBox hBox = new HBox(cancelButton, createButton);
            hBox.setAlignment(Pos.CENTER);
            hBox.setSpacing(10);

            vBox.getChildren().add(hBox);

            stage.setScene(new Scene(vBox, 300, 100));
            stage.show();
        }

    }
}
