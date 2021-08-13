package com.ptumulty.notch.ChecklistUI;

import javafx.beans.property.BooleanProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExtendedChecklistTableView extends TableView<ChecklistTableItem>
{
    private Map<String, TableColumn<ChecklistTableItem, Boolean>> columnMap;
    private ChecklistCategoryListItem checklistCategoryListItem;

    public ExtendedChecklistTableView()
    {
        columnMap = new HashMap<>();

        buildTable();
    }

    private void disposeTable()
    {
        getColumns().clear();
    }

    private void buildTable()
    {
        configureNameColumn();
        for (String taskName : columnMap.keySet())
        {
            if (columnMap.get(taskName) == null)
            {
                configureTaskColumns(taskName);
            }
        }
    }

    private void configureTaskColumns(String taskName)
    {
        TableColumn<ChecklistTableItem, Boolean> checklistTaskColumn = new TableColumn<>(taskName);
        checklistTaskColumn.setSortable(false);
        checklistTaskColumn.setCellValueFactory(param ->
                                                {
                                                    Optional<BooleanProperty> booleanProperty =
                                                            param.getValue().getTaskBooleanProperty(taskName);
                                                    return booleanProperty.orElse(null);
                                                });
        getColumns().add(checklistTaskColumn);
        columnMap.put(taskName, checklistTaskColumn);
    }

    public void onContextChange(ChecklistCategoryListItem checklistCategoryListItem)
    {
        this.checklistCategoryListItem = checklistCategoryListItem;

        assembleTaskColumns();

        disposeTable();

        buildTable();

        setItems(checklistCategoryListItem.getChecklists());
    }

    private void assembleTaskColumns()
    {
        for (ChecklistTableItem checklist : this.checklistCategoryListItem.getChecklists())
        {
            for (String columnTitle : checklist.getChecklist().getChecklistItemNames())
            {
                if (!columnMap.containsKey(columnTitle))
                {
                    columnMap.put(columnTitle, null);
                }
            }
        }
    }

    private void configureNameColumn()
    {
        TableColumn<ChecklistTableItem, String> checklistNameColumn = new TableColumn<>("Item Name");
        checklistNameColumn.setEditable(false);
        checklistNameColumn.setSortable(false);
        checklistNameColumn.setCellValueFactory(param -> param.getValue().titleProperty());
        getColumns().add(checklistNameColumn);
    }
}
