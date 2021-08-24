package com.ptumulty.notch.Checklist;

import com.ptumulty.ceramic.models.ListModel;
import com.ptumulty.ceramic.models.StringModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChecklistCategory
{
    private final StringModel categoryTitle;
    private final ListModel<Checklist> checklists;

    private final ListModel<String> defaultChecklistTasks;
    private final ListModel<String> additionalChecklistItems;

    public ChecklistCategory(String categoryTitle)
    {
        this.categoryTitle = new StringModel(categoryTitle);
        checklists = new ListModel<>();

        defaultChecklistTasks = new ListModel<>();
        additionalChecklistItems = new ListModel<>();
    }

    public void setCategoryTasks(List<String> items)
    {
        defaultChecklistTasks.setList(items);
    }

    public void addAdditionalChecklistItems(List<String> items)
    {
        additionalChecklistItems.addItems(items);
    }

    public List<String> getCategoryTasksSnapshot()
    {
        return new ArrayList<>(List.copyOf(defaultChecklistTasks.getItemsSnapshot()));
    }

    public List<String> getAdditionalChecklistItemsSnapshot()
    {
        return new ArrayList<>(List.copyOf(additionalChecklistItems.getItemsSnapshot()));
    }

    public String getCategoryTitle()
    {
        return categoryTitle.get();
    }

    public StringModel getCategoryTitleModel()
    {
        return categoryTitle;
    }

    public ListModel<Checklist> getChecklists()
    {
        return checklists;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChecklistCategory category = (ChecklistCategory) o;
        return categoryTitle.get().equals(category.categoryTitle.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(categoryTitle.get());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return categoryTitle.toString();
    }
}
