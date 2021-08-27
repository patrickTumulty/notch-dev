package com.ptumulty.notch.Checklist;

import com.ptumulty.ceramic.models.BooleanModel;
import com.ptumulty.ceramic.models.StringModel;

import java.time.LocalDate;
import java.util.*;

public class Checklist
{
    private final StringModel name;
    private final Map<String, BooleanModel> checklistTasks;
    private final LocalDate dateCreated;

    public Checklist(String name)
    {
        this(name, Collections.emptyList());
    }

    public Checklist(String name, List<String> tasks)
    {
        this.name = new StringModel(name);
        checklistTasks = new HashMap<>();
        dateCreated = LocalDate.now();
        System.out.println(dateCreated);
        addTasks(tasks);
    }

    public StringModel getName()
    {
        return name;
    }

    public void addTask(String taskName)
    {
        if (!checklistTasks.containsKey(taskName))
        {
            checklistTasks.put(taskName, new BooleanModel(false));
        }
    }

    public void addTasks(List<String> taskNames)
    {
        for (String name : taskNames)
        {
            if (!checklistTasks.containsKey(name))
            {
                checklistTasks.put(name, new BooleanModel(false));
            }
        }
    }

    public List<String> getTaskNamesSnapshot()
    {
        return new ArrayList<>(List.copyOf(checklistTasks.keySet()));
    }

    public Optional<BooleanModel> getTaskState(String taskName)
    {
        return Optional.ofNullable(checklistTasks.get(taskName));
    }

    @Override
    public String toString()
    {
        return name.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Checklist checklist = (Checklist) o;
        return Objects.equals(name, checklist.name);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(name, checklistTasks);
    }
}
