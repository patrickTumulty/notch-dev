package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.utility.Disposable;
import com.ptumulty.notch.AppContext;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import com.ptumulty.notch.Checklist.ChecklistManager;
import javafx.scene.control.ListView;

import java.util.HashMap;
import java.util.Map;

public class ChecklistCategoryListView extends ListView<ChecklistCategoryListItemView> implements
        ChecklistManager.ChecklistManagerListener, Disposable
{
    private final Map<ChecklistCategory, ChecklistCategoryListItemView> categoryModelToViewMap;

    public ChecklistCategoryListView()
    {
        AppContext.get().getBean(ChecklistManager.class).addListener(this);

        categoryModelToViewMap = new HashMap<>();
    }

    @Override
    public void checklistCategoryAdded(ChecklistCategory categoryAdded)
    {
        categoryModelToViewMap.put(categoryAdded,
                                   new ChecklistCategoryListItemView(new ChecklistCategoryListItem(categoryAdded)));
        getItems().add(categoryModelToViewMap.get(categoryAdded));
    }

    @Override
    public void checklistCategoryRemoved(ChecklistCategory categoryRemoved)
    {
        getItems().remove(categoryModelToViewMap.get(categoryRemoved));
        categoryModelToViewMap.remove(categoryRemoved);
    }

    @Override
    public void dispose()
    {
        AppContext.get().getBean(ChecklistManager.class).removeListener(this);
    }
}
