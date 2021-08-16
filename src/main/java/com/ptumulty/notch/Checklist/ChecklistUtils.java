package com.ptumulty.notch.Checklist;

public class ChecklistUtils
{
    public static void printChecklistFromCategory(ChecklistCategory category)
    {
        for (Checklist checklist : category.getChecklists().getItemsSnapshot())
        {
            System.out.println(checklist);
            for (String itemName : checklist.getChecklistItemNames())
            {
                System.out.println("\t" + itemName);
            }
        }
    }
}
