package com.ptumulty.notch.Checklist;

import java.util.List;

public interface ChecklistCategoryManager
{
    void addChecklistCategory(ChecklistCategory category) throws ChecklistCategoryManagerImpl.CategoryAlreadyExistsException;

    void removeChecklistCategory(ChecklistCategory name);

    void createAndAddChecklistCategory(String name) throws ChecklistCategoryManagerImpl.CategoryAlreadyExistsException;

    List<ChecklistCategory> getAllChecklistCategories();

    void removeChecklistCategory(String name);

    void addListener(ChecklistManagerListener listener);

    void removeListener(ChecklistManagerListener listener);

    interface ChecklistManagerListener
    {
        void checklistCategoryAdded(ChecklistCategory categoryAdded);

        void checklistCategoryRemoved(ChecklistCategory categoryRemoved);
    }
}
