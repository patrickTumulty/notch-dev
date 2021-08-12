package com.ptumulty.notch;

import com.ptumulty.notch.Checklist.ChecklistItem;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import com.ptumulty.notch.Checklist.ChecklistTask;
import com.ptumulty.notch.Checklist.ChecklistUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class NotchApplication
{
    public static void main(String[] args)
    {
//        Application.launch(NotchUIApplication.class, args);
        ChecklistCategory category = new ChecklistCategory("Completion Tasks");

        List<ChecklistTask> items = new ArrayList<>();
        items.add(new ChecklistTask("Take out Trash"));
        items.add(new ChecklistTask("Feed the Cat"));
        items.add(new ChecklistTask("Forget I Fed The Cat"));

        ChecklistItem checklist1 = new ChecklistItem("Todo1");
        checklist1.getChecklistTasks().setList(items);

        ChecklistItem checklist2 = new ChecklistItem("Todo2");
        checklist2.getChecklistTasks().setList(items);

        category.getChecklists().addItem(checklist1);
        category.getChecklists().addItem(checklist2);

        ChecklistUtils.printChecklistFromCategory(category);
    }

}
