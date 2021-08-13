package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.components.BooleanComponent;
import com.ptumulty.ceramic.models.BooleanModel;
import com.ptumulty.ceramic.models.ValueModel;
import com.ptumulty.notch.Checklist.Checklist;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValueBase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChecklistTableItem extends ObservableValueBase<ChecklistTableItem>
{
    public Checklist checklist;
    private StringProperty title;
    private Map<String, BooleanComponent> checklistTasks;

    public ChecklistTableItem(Checklist checklist)
    {
        this.checklist = checklist;
        title = new SimpleStringProperty(checklist.getName().getValue());
        checklistTasks = new HashMap<>();

        populateChecklistTasks();
    }

    private void populateChecklistTasks()
    {
        for (String itemName : checklist.getChecklistItemNames())
        {
            Optional<BooleanModel> checkedState = checklist.getItemCheckedState(itemName);
            checkedState.ifPresent(booleanModel ->
            {
                booleanModel.addListener(this::propertyChange);
                checklistTasks.put(itemName, new BooleanComponent(booleanModel));
            });
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

    private void propertyChange()
    {
        fireValueChangedEvent();
    }

    @Override
    public ChecklistTableItem getValue()
    {
        return this;
    }
}
