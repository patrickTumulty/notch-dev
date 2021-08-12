package com.ptumulty.notch.Checklist;

import com.ptumulty.ceramic.models.ListModel;
import com.ptumulty.ceramic.models.StringModel;
import com.sun.javafx.collections.ImmutableObservableList;

public class ChecklistItem
{
    private final StringModel name;
    private final ListModel<ChecklistTask> checklistItems;

    public ChecklistItem(String name)
    {
        this.name = new StringModel(name);
        checklistItems = new ListModel<>();
    }

    public StringModel getName()
    {
        return name;
    }

    public ImmutableObservableList<ChecklistTask> getChecklistTasksSnapshot()
    {
        return new ImmutableObservableList<>(checklistItems.getListItems().toArray(new ChecklistTask[0]));
    }

    public ListModel<ChecklistTask> getChecklistTasks()
    {
        return checklistItems;
    }

    @Override
    public String toString()
    {
        return name.toString();
    }
}
