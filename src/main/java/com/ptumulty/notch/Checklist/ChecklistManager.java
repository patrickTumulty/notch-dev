package com.ptumulty.notch.Checklist;

public interface ChecklistManager
{
    void addChecklistCategory(ChecklistCategory name) throws ChecklistManagerImpl.CategoryAlreadyExistsException;

    void removeChecklistCategory(ChecklistCategory name);

    void createAndAddChecklistCategory(String name) throws ChecklistManagerImpl.CategoryAlreadyExistsException;

    void removeChecklistCategory(String name);

    void addListener(ChecklistManagerListener listener);

    void removeListener(ChecklistManagerListener listener);

    interface ChecklistManagerListener
    {
        void checklistCategoryAdded(ChecklistCategory categoryAdded);

        void checklistCategoryRemoved(ChecklistCategory categoryRemoved);
    }
}
