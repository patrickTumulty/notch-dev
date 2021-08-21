package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.components.ListSelectionListener;
import com.ptumulty.ceramic.utility.Disposable;
import com.ptumulty.notch.AppContext;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import com.ptumulty.notch.Checklist.ChecklistCategoryManager;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChecklistCategoryListView extends ListView<ChecklistCategoryListItemView>
                                       implements ChecklistCategoryManager.ChecklistManagerListener,
                                                  Disposable
{
    private final Map<ChecklistCategory, ChecklistCategoryListItemView> categoryModelToViewMap;
    private List<ListSelectionListener<ChecklistCategoryListItemView>> listeners;

    public ChecklistCategoryListView()
    {
        AppContext.get().getBean(ChecklistCategoryManager.class).addListener(this);

        categoryModelToViewMap = new HashMap<>();
        listeners = new ArrayList<>();
    }

    @Override
    public void checklistCategoryAdded(ChecklistCategory categoryAdded)
    {

        ChecklistCategoryListItemView listItemView = new ChecklistCategoryListItemView(
                                                     new ChecklistCategoryListItem(categoryAdded));
        listItemView.setOnMousePressed(event -> listeners.forEach(listener -> listener.itemSelected(listItemView)));
        categoryModelToViewMap.put(categoryAdded, listItemView);
        getItems().add(categoryModelToViewMap.get(categoryAdded));
        getSelectionModel().select(listItemView);
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
        AppContext.get().getBean(ChecklistCategoryManager.class).removeListener(this);
    }

    public void addListener(ListSelectionListener<ChecklistCategoryListItemView> listener)
    {
        listeners.add(listener);
    }

    public void removeListener(ListSelectionListener<ChecklistCategoryListItemView> listener)
    {
        listeners.add(listener);
    }
}
