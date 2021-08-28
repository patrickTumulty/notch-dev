package com.ptumulty.notch.ChecklistUI.popups;

import com.ptumulty.ceramic.ceramicfx.Action;
import com.ptumulty.ceramic.ceramicfx.CancelableActionPopupWindow;
import com.ptumulty.notch.Checklist.Checklist;
import com.ptumulty.notch.ChecklistUI.ChecklistCategoryListItem;
import com.ptumulty.notch.CustomComponents.CheckableLabel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class CreateChecklistPopupWindow
{
    public CreateChecklistPopupWindow(ChecklistCategoryListItem checklistCategoryListItem)
    {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));

        TextField textField = new TextField();
        vBox.getChildren().add(textField);

        List<CheckableLabel> tasks = new ArrayList<>();

        for (String task : checklistCategoryListItem.getCategory().getCategoryTasksSnapshot())
        {
            CheckableLabel checkableLabel = new CheckableLabel(task);
            tasks.add(checkableLabel);
            vBox.getChildren().add(checkableLabel);
        }

        Action<CancelableActionPopupWindow.ActionCanceledException> createChecklist = () ->
        {
            checkIfTextFieldIsEmpty(textField);

            Checklist checklist = new Checklist(textField.getText());
            for (CheckableLabel task : tasks)
            {
                if (task.isSelected())
                {
                    checklist.addTask(task.getLabelText());
                }
            }

            checkIfAnyTasksWereAdded(checklist);

            checklistCategoryListItem.getCategory().getChecklists().addItem(checklist);
        };

        CancelableActionPopupWindow popupWindow = new CancelableActionPopupWindow(vBox, "Create", createChecklist);
        popupWindow.setStylesheet("css/main-stylesheet.css");
        popupWindow.setActionButtonID("action-button");
        popupWindow.setWindowTitle("Create Checklist");
        popupWindow.show();
    }

    private void checkIfTextFieldIsEmpty(TextField textField) throws CancelableActionPopupWindow.ActionCanceledException
    {
        if (textField.getText().isBlank())
        {
            throw new CancelableActionPopupWindow.ActionCanceledException("Title field is empty");
        }
    }

    private void checkIfAnyTasksWereAdded(Checklist checklist) throws CancelableActionPopupWindow.ActionCanceledException
    {
        if (checklist.getTaskNamesSnapshot().size() == 0)
        {
            throw new CancelableActionPopupWindow.ActionCanceledException("No Tasks Selected");
        }
    }
}
