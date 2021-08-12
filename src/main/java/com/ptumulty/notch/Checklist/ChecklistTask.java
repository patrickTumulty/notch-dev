package com.ptumulty.notch.Checklist;

import com.ptumulty.ceramic.models.BooleanModel;
import com.ptumulty.ceramic.models.StringModel;

public class ChecklistTask
{
    private final StringModel itemName;
    private final BooleanModel completionStatus;

    public ChecklistTask(String itemName)
    {
        this.itemName = new StringModel(itemName);
        completionStatus = new BooleanModel(false);
    }

    public StringModel getTaskName()
    {
        return itemName;
    }

    public BooleanModel getCompletionStatus()
    {
        return completionStatus;
    }

    @Override
    public String toString()
    {
        return itemName + " [" + completionStatus + "]";
    }
}
