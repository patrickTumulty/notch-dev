package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.components.ListSelectionListener;
import com.ptumulty.ceramic.utility.Disposable;
import com.ptumulty.notch.AppContext;
import com.ptumulty.notch.Checklist.ChecklistCategory;
import com.ptumulty.notch.Checklist.ChecklistCategoryManager;
import com.ptumulty.notch.ChecklistUI.popups.ConfigureCategoryPopupWindow;
import com.ptumulty.notch.ChecklistUI.popups.ConfigureChecklistPopupWindow;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.util.*;

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

        ContextMenu contextMenu = new ContextMenu();

        MenuItem addChecklist = new MenuItem("Add Checklist");
        addChecklist.setOnAction(event ->
                new ConfigureChecklistPopupWindow(getSelectionModel().getSelectedItem().getCategoryListItem(), Optional.empty()));

        MenuItem newCategory = new MenuItem("New Category");
        newCategory.setOnAction(event -> new ConfigureCategoryPopupWindow());

        MenuItem configure = new MenuItem("Configure"); // TODO Configure
        MenuItem delete = new MenuItem("Delete"); // TODO Delete: Create warning popup

        contextMenu.getItems().addAll(List.of(addChecklist, new SeparatorMenuItem(), newCategory, configure, delete));

        setContextMenu(contextMenu);

        setOnContextMenuRequested(event ->
        {
            boolean contextOnListItem = !(event.getTarget() instanceof ChecklistCategoryListItemView);
            addChecklist.setDisable(contextOnListItem);
            configure.setDisable(contextOnListItem);
            delete.setDisable(contextOnListItem);
        });
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
