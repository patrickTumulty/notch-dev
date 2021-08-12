package com.ptumulty.notch.Checklist;

public class ChecklistUtils
{
    public static void printChecklistFromCategory(ChecklistCategory category)
    {
        for (ChecklistItem checklist : category.getChecklists().getListItems())
        {
            System.out.println(checklist);
            for (ChecklistTask checklistItem : checklist.getChecklistTasks().getListItems())
            {
                System.out.println("\t" + checklistItem);
            }
        }
    }
}
