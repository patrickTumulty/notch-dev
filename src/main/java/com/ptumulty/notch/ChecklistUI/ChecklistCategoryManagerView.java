package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.ceramicfx.Action;
import com.ptumulty.ceramic.ceramicfx.CancelableActionPopupWindow;
import com.ptumulty.ceramic.utility.StringUtils;
import com.ptumulty.notch.AppContext;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import com.ptumulty.notch.Checklist.ChecklistCategoryManager;
import com.ptumulty.notch.Checklist.ChecklistCategoryManagerImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

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
        createButton.setOnAction(event -> new CreateCategoryPopupWindow());

        setTop(createButton);
        setCenter(checklistCategoryListView);
    }

    private static class CreateCategoryPopupWindow
    {
        private final TextArea defaultChecklistItems;
        private final TextField categoryNameField;

        CreateCategoryPopupWindow()
        {
            VBox vBox = new VBox();
            vBox.setSpacing(10);
            vBox.setPadding(new Insets(10));
            vBox.setAlignment(Pos.CENTER);

            categoryNameField = new TextField();
            categoryNameField.setPromptText("Category Title...");
            vBox.getChildren().add(categoryNameField);

            defaultChecklistItems = new TextArea();
            vBox.getChildren().add(defaultChecklistItems);

            Action<CancelableActionPopupWindow.ActionCanceledException> actionEvent = this::createCategory;

            CancelableActionPopupWindow popupWindow = new CancelableActionPopupWindow(vBox, "Create", actionEvent);
            popupWindow.setStylesheet("css/main-stylesheet.css");
            popupWindow.setWindowTitle("Checklist Category Creation");
            popupWindow.setActionButtonID("action-button");
            popupWindow.show(300, 300);
        }

        private void createCategory() throws CancelableActionPopupWindow.ActionCanceledException
        {
            String categoryName = categoryNameField.getText().isBlank() ?
                                  "Untitled" :
                                  categoryNameField.getText();

            try
            {
                ChecklistCategory checklistCategory = new ChecklistCategory(categoryName);

                List<String> defaults = StringUtils.parseMultilineStringToList(defaultChecklistItems.getText());
                StringUtils.removeBlankStringsFromList(defaults);
                if (defaults.size() == 0)
                {
                    throw new CancelableActionPopupWindow.ActionCanceledException("Task list is empty.");
                }
                checklistCategory.setCategoryTasks(defaults);
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
