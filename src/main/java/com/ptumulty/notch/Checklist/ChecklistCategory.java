package com.ptumulty.notch.Checklist;

import com.ptumulty.ceramic.models.ListModel;
import com.ptumulty.ceramic.models.StringModel;

public class ChecklistCategory
{
    private final StringModel categoryTitle;
    private final ListModel<Checklist> checklists;

    public ChecklistCategory(String categoryTitle)
    {
        this.categoryTitle = new StringModel(categoryTitle);
        checklists = new ListModel<>();
    }

    public StringModel getCategoryTitle()
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
    public String toString()
    {
        return categoryTitle.toString();
    }
}
