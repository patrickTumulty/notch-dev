package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.notch.AppContext;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import com.ptumulty.notch.Checklist.ChecklistCategoryManager;
import javafx.scene.control.ChoiceBox;

import java.util.HashMap;
import java.util.Map;

public class ChecklistCategoryChoiceBox extends ChoiceBox<ChecklistCategoryListItem>
{
    private final Map<String, ChecklistCategoryListItem> listItems;

    ChecklistCategoryChoiceBox()
    {
        listItems = new HashMap<>();

        AppContext.get().getBean(ChecklistCategoryManager.class).addListener(new ChecklistCategoryManager.ChecklistManagerListener()
        {
            @Override
            public void checklistCategoryAdded(ChecklistCategory categoryAdded)
            {
                listItems.put(categoryAdded.getCategoryTitle(), new ChecklistCategoryListItem(categoryAdded));
                getItems().add(listItems.get(categoryAdded.getCategoryTitle()));
                getSelectionModel().select(listItems.get(categoryAdded.getCategoryTitle()));
            }

            @Override
            public void checklistCategoryRemoved(ChecklistCategory categoryRemoved)
            {
                getItems().remove(listItems.get(categoryRemoved.getCategoryTitle()));
            }
        });
    }
}
