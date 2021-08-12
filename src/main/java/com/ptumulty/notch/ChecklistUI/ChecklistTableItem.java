package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.notch.Checklist.ChecklistItem;
import com.ptumulty.notch.Checklist.ChecklistTask;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChecklistTableItem
{
    public ChecklistItem checklist;
    private StringProperty title;
    private Map<String, BooleanProperty> checklistTasks;

    ChecklistTableItem(ChecklistItem checklist)
    {
        title = new SimpleStringProperty(checklist.getName().getValue());
        checklistTasks = new HashMap<>();
        for (ChecklistTask task : checklist.getChecklistTasksSnapshot())
        {
            checklistTasks.put(task.getTaskName().getValue(),
                               new SimpleBooleanProperty(task.getCompletionStatus().getValue()));
        }
    }

    public StringProperty titleProperty()
    {
        return title;
    }

    public ChecklistItem getChecklistItem()
    {
        return checklist;
    }

    public Optional<BooleanProperty> getTaskBooleanProperty(String taskName)
    {
        return Optional.ofNullable(checklistTasks.get(taskName));
    }
}
