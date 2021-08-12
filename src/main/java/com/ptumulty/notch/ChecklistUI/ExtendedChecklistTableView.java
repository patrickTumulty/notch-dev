package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.notch.Checklist.ChecklistTask;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExtendedChecklistTableView extends TableView<ChecklistTableItem>
{
    private Map<String, TableColumn<ChecklistTableItem, Boolean>> columnMap;
    private ChecklistCategoryListItem checklistCategoryListItem;

    ExtendedChecklistTableView()
    {
        columnMap = new HashMap<>();

        configureNameColumn();
        for (String taskName : columnMap.keySet())
        {
            if (columnMap.get(taskName) == null)
            {
                configureTaskColumn(taskName);
            }
        }
    }

    private void configureTaskColumn(String taskName)
    {
        TableColumn<ChecklistTableItem, Boolean> checklistTaskColumn = new TableColumn<>();
        checklistTaskColumn.setCellValueFactory(param ->
                                                {
                                                    Optional<BooleanProperty> booleanProperty =
                                                            param.getValue().getTaskBooleanProperty(taskName);
                                                    return booleanProperty.orElse(null);
                                                });
        getColumns().add(checklistTaskColumn);
        columnMap.put(taskName, checklistTaskColumn);
    }

    public void setChecklistCategories(ChecklistCategoryListItem checklistCategoryListItem)
    {
        this.checklistCategoryListItem = checklistCategoryListItem;

        assembleTaskColumns();

        setItems(checklistCategoryListItem.getChecklists());
    }

    private void assembleTaskColumns()
    {
        for (ChecklistTableItem checklist : this.checklistCategoryListItem.getChecklists())
        {
            for (ChecklistTask checklistTask : checklist.getChecklistItem().getChecklistTasksSnapshot())
            {
                if (!columnMap.containsKey(checklistTask.getTaskName().getValue()))
                {
                    columnMap.put(checklistTask.getTaskName().getValue(), null);
                }
            }
        }
    }

    private void configureNameColumn()
    {
        TableColumn<ChecklistTableItem, String> checklistNameColumn = new TableColumn<>();
        checklistNameColumn.setEditable(false);
        checklistNameColumn.setCellValueFactory(param -> param.getValue().titleProperty());
        getColumns().add(checklistNameColumn);
    }
}
