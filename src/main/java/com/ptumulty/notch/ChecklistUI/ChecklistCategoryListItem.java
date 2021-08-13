package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.notch.Checklist.Checklist;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ChecklistCategoryListItem
{
    private ChecklistCategory category;
    private ObservableList<ChecklistTableItem> checklists;

    public ChecklistCategoryListItem(ChecklistCategory category)
    {
        checklists = FXCollections.observableArrayList();
        this.category = category;
        for (Checklist checklist : this.category.getChecklists().getListItems())
        {
            checklists.add(new ChecklistTableItem(checklist));
        }
    }

    public ObservableList<ChecklistTableItem> getChecklists()
    {
        return checklists;
    }
}
