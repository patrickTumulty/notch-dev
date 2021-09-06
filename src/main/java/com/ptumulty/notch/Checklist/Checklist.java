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
    private final BooleanModel isCompleted;
    private List<ChecklistListener> listeners;

    public Checklist(String name)
    {
        this(name, Collections.emptyList());
    }

    public Checklist(String name, List<String> tasks)
    {
        this.name = new StringModel(name);
        listeners = new ArrayList<>();
        checklistTasks = new HashMap<>();
        dateCreated = LocalDate.now();
        isCompleted = new BooleanModel(false);
        addTasks(tasks);
    }

    public LocalDate getDateCreated()
    {
        return dateCreated;
    }

    public StringModel getName()
    {
        return name;
    }

    public boolean isComplete()
    {
        return isCompleted.get();
    }

    private void calculateIsComplete()
    {
        for (BooleanModel booleanModel : checklistTasks.values())
        {
            if (!booleanModel.get())
            {
                isCompleted.setValue(false);
                return;
            }
        }
        isCompleted.setValue(true);
    }

    public void addTask(String taskName)
    {
        if (!checklistTasks.containsKey(taskName))
        {
            checklistTasks.put(taskName, new BooleanModel(false));
            listeners.forEach(listener -> listener.taskAdded(taskName));
            listeners.forEach(ChecklistListener::checklistTasksChanged);
        }
        checklistTasks.get(taskName).addListener(this::calculateIsComplete);
    }

    public void removeTask(String taskName)
    {
        checklistTasks.remove(taskName);
        listeners.forEach(listener -> listener.taskRemoved(taskName));
        listeners.forEach(ChecklistListener::checklistTasksChanged);
    }

    public void removeTasks(List<String> tasks)
    {
        tasks.forEach(checklistTasks::remove);
    }

    public void addTasks(List<String> taskNames)
    {
        taskNames.forEach(this::addTask);
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

    public void addListener(ChecklistListener listener)
    {
        this.listeners.add(listener);
    }

    public void removeListener(ChecklistListener listener)
    {
        this.listeners.remove(listener);
    }

    public interface ChecklistListener
    {
        void taskAdded(String task);

        void taskRemoved(String task);

        void checklistTasksChanged();
    }
}
