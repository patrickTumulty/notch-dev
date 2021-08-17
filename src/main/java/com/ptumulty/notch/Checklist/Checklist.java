package com.ptumulty.notch.Checklist;

import com.ptumulty.ceramic.models.BooleanModel;
import com.ptumulty.ceramic.models.ListModel;
import com.ptumulty.ceramic.models.StringModel;
import com.sun.javafx.collections.ImmutableObservableList;

import java.util.*;

public class Checklist
{
    private final StringModel name;
    private final Map<String, BooleanModel> checklistItems;

    public Checklist(String name)
    {
        this.name = new StringModel(name);
        checklistItems = new HashMap<>();
    }

    public StringModel getName()
    {
        return name;
    }

    public void addNewChecklistItem(String itemName)
    {
        if (!checklistItems.containsKey(itemName))
        {
            checklistItems.put(itemName, new BooleanModel(false));
        }
    }

    public void addNewChecklistItems(List<String> itemNames)
    {
        for (String name : itemNames)
        {
            if (!checklistItems.containsKey(name))
            {
                checklistItems.put(name, new BooleanModel(false));
            }
        }
    }

    public List<String> getChecklistTaskNames()
    {
        return new ArrayList<>(List.copyOf(checklistItems.keySet()));
    }

    public Optional<BooleanModel> getItemCheckedState(String itemName)
    {
        return Optional.ofNullable(checklistItems.get(itemName));
    }

    @Override
    public String toString()
    {
        return name.toString();
    }
}
