package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.models.BooleanModel;
import com.ptumulty.notch.Checklist.Checklist;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChecklistTableItem
{
    public Checklist checklist;
    private StringProperty title;
    private Map<String, BooleanProperty> checklistTasks;

    public ChecklistTableItem(Checklist checklist)
    {
        this.checklist = checklist;
        title = new SimpleStringProperty(checklist.getName().getValue());
        checklistTasks = new HashMap<>();
        for (String itemName : checklist.getChecklistItemNames())
        {
            Optional<BooleanModel> checkedState = checklist.getItemCheckedState(itemName);
            checkedState.ifPresent(booleanModel ->
                    checklistTasks.put(itemName, new SimpleBooleanProperty(booleanModel.getValue())));
        }
    }

    public StringProperty titleProperty()
    {
        return title;
    }

    public Checklist getChecklist()
    {
        return checklist;
    }

    public Optional<BooleanProperty> getTaskBooleanProperty(String taskName)
    {
        return Optional.ofNullable(checklistTasks.get(taskName));
    }
}
