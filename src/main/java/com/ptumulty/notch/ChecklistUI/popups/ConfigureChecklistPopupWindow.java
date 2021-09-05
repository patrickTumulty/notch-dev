package com.ptumulty.notch.ChecklistUI.popups;

import com.ptumulty.ceramic.ceramicfx.Action;
import com.ptumulty.ceramic.ceramicfx.CancelableActionPopupWindow;
import com.ptumulty.notch.Checklist.Checklist;
import com.ptumulty.notch.ChecklistUI.ChecklistCategoryListItem;
import com.ptumulty.notch.ChecklistUI.ChecklistTableItem;
import com.ptumulty.notch.CustomComponents.CheckableLabel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConfigureChecklistPopupWindow
{
    private TextField textField;
    private List<CheckableLabel> tasks;
    private final ChecklistCategoryListItem checklistCategoryListItem;
    private final Optional<ChecklistTableItem> checklistTableItem;
    private VBox vBox;

    public ConfigureChecklistPopupWindow(ChecklistCategoryListItem checklistCategoryListItem, Optional<ChecklistTableItem> checklistTableItem)
    {
        this.checklistCategoryListItem = checklistCategoryListItem;
        this.checklistTableItem = checklistTableItem;

        configureVisuals();

        createAndShowPopup();
    }

    private VBox configureVisuals()
    {
        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(10));

        textField = new TextField(checklistTableItem.isPresent() ?
                checklistTableItem.get().getChecklist().getName().get() : "");
        vBox.getChildren().add(textField);

        tasks = new ArrayList<>();

        for (String task : checklistCategoryListItem.getCategory().getCategoryTasksSnapshot())
        {
            CheckableLabel checkableLabel = new CheckableLabel(task,checklistTableItem.isPresent() &&
                                            checklistTableItem.get().getChecklist().getTaskState(task).isPresent());

            tasks.add(checkableLabel);
            vBox.getChildren().add(checkableLabel);
        }
        return vBox;
    }

    private void createAndShowPopup()
    {
        Action<CancelableActionPopupWindow.ActionCanceledException> createChecklist =
                checklistTableItem.isPresent() ?
                this::configureExistingChecklist : this::createNewChecklist;

        CancelableActionPopupWindow popupWindow = new CancelableActionPopupWindow(vBox,
                checklistTableItem.isPresent() ? "Configure" : "Create", createChecklist);
        popupWindow.setStylesheet("css/main-stylesheet.css");
        popupWindow.setActionButtonID("action-button");
        popupWindow.setWindowTitle(checklistTableItem.isPresent() ? "Configure Checklist" : "Create Checklist");
        popupWindow.show();
    }

    private void createNewChecklist() throws CancelableActionPopupWindow.ActionCanceledException
    {
        checkIfTextFieldIsEmpty();

        Checklist checklist = new Checklist(textField.getText());
        for (CheckableLabel task : tasks)
        {
            if (task.isSelected())
            {
                checklist.addTask(task.getLabelText());
            }
        }

        checkIfChecklistContainsTasks(checklist);

        checklistCategoryListItem.getCategory().getChecklists().addItem(checklist);
    }

    private void configureExistingChecklist() throws CancelableActionPopupWindow.ActionCanceledException
    {
        checkIfTextFieldIsEmpty();

        if (checklistTableItem.isPresent())
        {
            checklistTableItem.get().getChecklist().getName().setValue(textField.getText());

            for (CheckableLabel task : tasks)
            {
                if (task.isSelected() &&
                        checklistTableItem.get().getChecklist().getTaskState(task.getLabelText()).isEmpty())
                {
                    checklistTableItem.get().getChecklist().addTask(task.getLabelText());
                }
                else if (!task.isSelected() &&
                        checklistTableItem.get().getChecklist().getTaskState(task.getLabelText()).isPresent())
                {
                    checklistTableItem.get().getChecklist().removeTask(task.getLabelText());
                }
            }

            checkIfChecklistContainsTasks(checklistTableItem.get().getChecklist());
        }
        else
        {
            throw new CancelableActionPopupWindow.ActionCanceledException("There was an error...");
        }
    }

    private void checkIfTextFieldIsEmpty() throws CancelableActionPopupWindow.ActionCanceledException
    {
        if (textField.getText().isBlank())
        {
            throw new CancelableActionPopupWindow.ActionCanceledException("Title field is empty");
        }
    }

    private void checkIfChecklistContainsTasks(Checklist checklist) throws CancelableActionPopupWindow.ActionCanceledException
    {
        if (checklist.getTaskNamesSnapshot().size() == 0)
        {
            throw new CancelableActionPopupWindow.ActionCanceledException("No Tasks Selected");
        }
    }
}
