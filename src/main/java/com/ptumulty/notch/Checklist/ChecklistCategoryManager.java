package com.ptumulty.notch.Checklist;

public interface ChecklistCategoryManager
{
    void addChecklistCategory(ChecklistCategory category) throws ChecklistCategoryManagerImpl.CategoryAlreadyExistsException;

    void removeChecklistCategory(ChecklistCategory name);

    void createAndAddChecklistCategory(String name) throws ChecklistCategoryManagerImpl.CategoryAlreadyExistsException;

    void removeChecklistCategory(String name);

    void addListener(ChecklistManagerListener listener);

    void removeListener(ChecklistManagerListener listener);

    interface ChecklistManagerListener
    {
        void checklistCategoryAdded(ChecklistCategory categoryAdded);

        void checklistCategoryRemoved(ChecklistCategory categoryRemoved);
    }
}
