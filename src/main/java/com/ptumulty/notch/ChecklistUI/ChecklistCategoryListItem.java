package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.notch.Checklist.Checklist;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChecklistCategoryListItem
{
    private final ChecklistCategory category;
    private final ObservableList<ChecklistTableItem> checklists;

    public ChecklistCategoryListItem(ChecklistCategory category)
    {
        checklists = FXCollections.observableArrayList();
        this.category = category;

        createTableItems();
    }

    public ChecklistCategory getCategory()
    {
        return category;
    }

    public String getCategoryTitle()
    {
        return category.getCategoryTitle();
    }

    private void createTableItems()
    {
        for (Checklist checklist : this.category.getChecklists().getItemsSnapshot())
        {
            checklists.add(new ChecklistTableItem(checklist));
        }
    }

    public ObservableList<ChecklistTableItem> getChecklists()
    {
        return checklists;
    }
}
