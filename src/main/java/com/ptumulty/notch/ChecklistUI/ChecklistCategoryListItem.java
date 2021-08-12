package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.notch.Checklist.ChecklistCategory;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;

public class ChecklistCategoryListItem
{
    private ObservableList<ChecklistTableItem> checklists;

    ChecklistCategoryListItem(ChecklistCategory category)
    {
        checklists = new SimpleListProperty<>();
        category.getChecklists().getListItems().forEach(checklist ->
                checklists.add(new ChecklistTableItem(checklist)));
    }

    public ObservableList<ChecklistTableItem> getChecklists()
    {
        return checklists;
    }
}
