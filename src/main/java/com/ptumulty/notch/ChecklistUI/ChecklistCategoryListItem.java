package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.models.ListModel;
import com.ptumulty.notch.Checklist.Checklist;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.HashMap;
import java.util.Map;

public class ChecklistCategoryListItem
{
    private final ChecklistCategory category;
    private final ObservableList<ChecklistTableItem> checklists;
    private final FilteredList<ChecklistTableItem> filterChecklists;
    private final Map<Checklist, ChecklistTableItem> listToTableItem;

    public ChecklistCategoryListItem(ChecklistCategory category)
    {
        checklists = FXCollections.observableArrayList();
        filterChecklists = new FilteredList<>(checklists, p -> true);

        this.category = category;
        listToTableItem = new HashMap<>();

        configureCategoryListListener();

        createTableItems();
    }

    private void configureCategoryListListener()
    {
        this.category.getChecklists().addListener(new ListModel.ListModelListener<>()
        {
            @Override
            public void itemAdded(Checklist item)
            {
                listToTableItem.put(item, new ChecklistTableItem(item));
                checklists.add(0, listToTableItem.get(item));
            }

            @Override
            public void itemRemoved(Checklist item)
            {
                checklists.remove(listToTableItem.get(item));
                listToTableItem.remove(item);
            }

            @Override
            public void listChanged()
            {
                checklists.clear();
                listToTableItem.clear();
                createTableItems();
            }
        });
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

    public FilteredList<ChecklistTableItem> getFilterChecklists()
    {
        return filterChecklists;
    }
}
