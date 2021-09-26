package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.components.BooleanComponent;
import com.ptumulty.ceramic.models.BooleanModel;
import com.ptumulty.notch.Checklist.Checklist;
import com.ptumulty.notch.CustomComponents.CheckableRegion;
import com.ptumulty.notch.CustomComponents.CheckableRegionComponent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValueBase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ChecklistTableItem extends ObservableValueBase<ChecklistTableItem> implements Checklist.ChecklistListener
{
    private final StringProperty title;
    private final Map<String, CheckableRegionComponent> checklistTasks;
    public Checklist checklist;

    public ChecklistTableItem(Checklist checklist)
    {
        this.checklist = checklist;
        title = new SimpleStringProperty(checklist.getName().get());
        checklistTasks = new HashMap<>();

        checklist.getName().addListener(this::propertyChange);
        checklist.addListener(this);

        populateChecklistTasks();
    }

    private void populateChecklistTasks()
    {
        for (String itemName : checklist.getTaskNamesSnapshot())
        {
            createAndAddTaskComponent(itemName);
        }
    }

    private void createAndAddTaskComponent(String itemName)
    {
        Optional<BooleanModel> checkedState = checklist.getTaskState(itemName);
        checkedState.ifPresent(booleanModel -> checklistTasks.put(itemName, new CheckableRegionComponent(booleanModel)));
    }

    public StringProperty titleProperty()
    {
        return title;
    }

    public Checklist getChecklist()
    {
        return checklist;
    }

    public Optional<CheckableRegionComponent> getTaskBooleanComponent(String taskName)
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

    @Override
    public void taskAdded(String task)
    {
        createAndAddTaskComponent(task);
    }

    @Override
    public void taskRemoved(String task)
    {
        checklistTasks.remove(task);
    }

    @Override
    public void checklistTasksChanged()
    {
        propertyChange();
    }
}
