package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.components.BooleanComponent;
import com.ptumulty.ceramic.models.BooleanModel;
import com.ptumulty.notch.Checklist.Checklist;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValueBase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChecklistTableItem extends ObservableValueBase<ChecklistTableItem>
{
    private final StringProperty title;
    private final Map<String, BooleanComponent> checklistTasks;
    public Checklist checklist;

    public ChecklistTableItem(Checklist checklist)
    {
        this.checklist = checklist;
        title = new SimpleStringProperty(checklist.getName().get());
        checklistTasks = new HashMap<>();

        populateChecklistTasks();
    }

    private void populateChecklistTasks()
    {
        for (String itemName : checklist.getTaskNamesSnapshot())
        {
            Optional<BooleanModel> checkedState = checklist.getTaskState(itemName);
            checkedState.ifPresent(booleanModel ->
                    checklistTasks.put(itemName, new BooleanComponent(booleanModel)));
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

    public Optional<BooleanComponent> getTaskBooleanComponent(String taskName)
    {
        return Optional.ofNullable(checklistTasks.get(taskName));
    }

//    private void propertyChange()
//    {
//        fireValueChangedEvent();
//    }

    @Override
    public ChecklistTableItem getValue()
    {
        return this;
    }
}
