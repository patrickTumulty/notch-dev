package com.ptumulty.notch;

import com.ptumulty.notch.Checklist.Checklist;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Data
public class ChecklistData
{
    String id;
    String name;
    Map<String, Boolean> checklistTasks;
    LocalDate dateCreated;

    ChecklistData(String name, Map<String, Boolean> checklistTasks, LocalDate dateCreated)
    {
        this.name = name;
        this.checklistTasks = checklistTasks;
        this.dateCreated = dateCreated;
    }

    public static ChecklistData toDataClass(Checklist checklist)
    {
        Map<String, Boolean> tasks = new HashMap<>();
        for (String taskName : checklist.getTaskNamesSnapshot())
        {
            if (checklist.getTaskState(taskName).isPresent())
            {
                tasks.put(taskName, checklist.getTaskState(taskName).get().get());
            }
        }
        return new ChecklistData(checklist.getName().get(), tasks, checklist.getDateCreated());
    }

    public static Checklist fromDataClass(ChecklistData checklistData)
    {
        Checklist checklist = new Checklist(checklistData.getName(), new ArrayList<>(checklistData.getChecklistTasks().keySet()));
        for (Map.Entry<String, Boolean> task : checklistData.getChecklistTasks().entrySet())
        {
            if (checklist.getTaskState(task.getKey()).isPresent())
            {
                checklist.getTaskState(task.getKey()).get().setValue(task.getValue());
            }
        }
        checklist.setDateCreated(checklistData.getDateCreated());
        return checklist;
    }
}
