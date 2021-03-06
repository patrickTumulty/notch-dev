package com.ptumulty.notch.ChecklistUI.popups;

import com.ptumulty.ceramic.ceramicfx.Action;
import com.ptumulty.ceramic.ceramicfx.CancelableActionPopupWindow;
import com.ptumulty.ceramic.utility.SetUtils;
import com.ptumulty.ceramic.utility.StringUtils;
import com.ptumulty.notch.Checklist.Checklist;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import com.ptumulty.notch.Checklist.ChecklistCategoryManager;
import com.ptumulty.notch.Checklist.ChecklistCategoryManagerImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.openide.util.Lookup;

import java.util.*;

public class ConfigureCategoryPopupWindow
{
    private TextArea defaultChecklistTasks;
    private TextField categoryNameField;
    private final Optional<ChecklistCategory> checklistCategory;
    private final VBox uiComponentContainer;

    public ConfigureCategoryPopupWindow()
    {
        this(Optional.empty());
    }

    public ConfigureCategoryPopupWindow(Optional<ChecklistCategory> checklistCategory)
    {
        this.checklistCategory = checklistCategory;

        uiComponentContainer = new VBox();
        uiComponentContainer.setSpacing(10);
        uiComponentContainer.setPadding(new Insets(10));
        uiComponentContainer.setAlignment(Pos.CENTER);

        configureNameField();

        configureTasksField();

        Action<CancelableActionPopupWindow.ActionCanceledException> actionEvent =
                this.checklistCategory.isPresent() ? this::configureCategory : this::createCategory;

        CancelableActionPopupWindow popupWindow =
                new CancelableActionPopupWindow(
                        uiComponentContainer, checklistCategory.isPresent() ? "Configure" : "Create", actionEvent);
        popupWindow.setStylesheet("css/main-stylesheet.css");
        popupWindow.setWindowTitle((checklistCategory.isPresent() ? "Configure" : "Create") + " Checklist Category");
        popupWindow.setActionButtonID("action-button");
        popupWindow.show(300, 300);
    }

    private void configureTasksField()
    {
        defaultChecklistTasks = new TextArea();

        if (checklistCategory.isPresent())
        {
            StringBuilder stringBuilder = new StringBuilder();
            for (String task : checklistCategory.get().getCategoryTasksSnapshot())
            {
                stringBuilder.append(task).append("\n");
            }
            defaultChecklistTasks.setText(stringBuilder.toString());
        }

        uiComponentContainer.getChildren().add(defaultChecklistTasks);
    }

    private void configureNameField()
    {
        categoryNameField = new TextField(this.checklistCategory.isPresent() ?
                                          this.checklistCategory.get().getCategoryTitle() : "");
        categoryNameField.setPromptText("Category Title...");
        uiComponentContainer.getChildren().add(categoryNameField);
    }

    private void createCategory() throws CancelableActionPopupWindow.ActionCanceledException
    {
        try
        {
            ChecklistCategory checklistCategory = new ChecklistCategory(getCategoryName());

            List<String> defaults = getDefaultTasks();
            if (defaults.size() == 0)
            {
                throw new CancelableActionPopupWindow.ActionCanceledException("Task list is empty.");
            }
            checklistCategory.setCategoryTasks(defaults);
            Lookup.getDefault().lookup(ChecklistCategoryManager.class).addChecklistCategory(checklistCategory);
        }
        catch (ChecklistCategoryManagerImpl.CategoryAlreadyExistsException e)
        {
            /*
             * TODO: Check here for how many of this already existing name shows up and append a number at the end.
             */
        }
    }

    private List<String> getDefaultTasks()
    {
        List<String> defaults = StringUtils.parseMultilineStringToList(defaultChecklistTasks.getText());
        StringUtils.removeBlankStringsFromList(defaults);
        return defaults;
    }

    private String getCategoryName()
    {
        return categoryNameField.getText().isEmpty() ? "Untitled" : categoryNameField.getText();
    }

    private void configureCategory() throws CancelableActionPopupWindow.ActionCanceledException
    {
        if (checklistCategory.isPresent())
        {
            checklistCategory.get().getCategoryTitleModel().setValue(getCategoryName());
            checklistCategory.get().setCategoryTasks(getDefaultTasks());

            removeTasksFromChecklists(checklistCategory.get());
        }
        else
        {
            throw new CancelableActionPopupWindow.ActionCanceledException("Something went wrong...");
        }
    }

    /**
     * If the category has been configured to include fewer tasks than it originally had, this method will go into
     * each checklist and remove those tasks.
     *
     * Note: Each checklist can have a subset of the categories tasks
     */
    private void removeTasksFromChecklists(ChecklistCategory category)
    {
        Set<String> taskSet = new HashSet<>();
        Set<String> defaultTaskSet = new HashSet<>(category.getCategoryTasksSnapshot());
        for (Checklist checklist : category.getChecklists().get())
        {
            taskSet.clear();
            taskSet.addAll(checklist.getTaskNamesSnapshot());
            taskSet = SetUtils.difference(taskSet, defaultTaskSet);
            if (taskSet.size() > 0)
            {
                checklist.removeTasks(new ArrayList<>(taskSet));
            }
        }
    }
}