package com.ptumulty.notch.Checklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChecklistManagerImpl implements ChecklistManager
{
    private final Map<String, ChecklistCategory> checklistCategoriesMap;
    private final List<ChecklistManagerListener> listeners;

    public ChecklistManagerImpl()
    {
        checklistCategoriesMap = new HashMap<>();
        listeners = new ArrayList<>();
    }

    @Override
    public void addChecklistCategory(ChecklistCategory category) throws CategoryAlreadyExistsException
    {
        if (!checklistCategoriesMap.containsKey(category.getCategoryTitle()))
        {
            checklistCategoriesMap.put(category.getCategoryTitle(), category);
            notifyCategoryAdded(category);
        }
        else
        {
            throw new CategoryAlreadyExistsException("A Checklist category with the title " +
                    category.getCategoryTitle() +
                    "already exists");
        }
    }

    private void notifyCategoryAdded(ChecklistCategory category)
    {
        listeners.forEach(listener -> listener.checklistCategoryAdded(category));
    }

    @Override
    public void removeChecklistCategory(ChecklistCategory category)
    {
        removeChecklistCategory(category.getCategoryTitle());
    }

    @Override
    public void createAndAddChecklistCategory(String name) throws CategoryAlreadyExistsException
    {
        addChecklistCategory(new ChecklistCategory(name));
    }

    @Override
    public void removeChecklistCategory(String name)
    {
        notifyCategoryRemoved(checklistCategoriesMap.get(name));
        checklistCategoriesMap.remove(name);
    }

    private void notifyCategoryRemoved(ChecklistCategory category)
    {
        listeners.forEach(listener -> listener.checklistCategoryRemoved(category));
    }

    @Override
    public void addListener(ChecklistManagerListener listener)
    {
        listeners.add(listener);
    }

    @Override
    public void removeListener(ChecklistManagerListener listener)
    {
        listeners.remove(listener);
    }

    public static class CategoryAlreadyExistsException extends Exception
    {
        CategoryAlreadyExistsException(String msg)
        {
            super(msg);
        }
    }
}
