package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.components.BooleanComponent;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExtendedChecklistTableView extends TableView<ChecklistTableItem>
{
    private Map<String, TableColumn<ChecklistTableItem, ChecklistTableItem>> columnMap;
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
        TableColumn<ChecklistTableItem, ChecklistTableItem> checklistTaskColumn = new TableColumn<>(taskName);
        checklistTaskColumn.setSortable(false);
        checklistTaskColumn.setCellValueFactory(TableColumn.CellDataFeatures::getValue);
        checklistTaskColumn.setCellFactory(param -> new CheckableTableCell(taskName));
        getColumns().add(checklistTaskColumn);
        columnMap.put(taskName, checklistTaskColumn);
    }

    public void setChecklistCategoryListItem(ChecklistCategoryListItem checklistCategoryListItem)
    {
        this.checklistCategoryListItem = checklistCategoryListItem;
        onContextChange(this.checklistCategoryListItem);
    }

    private void onContextChange(ChecklistCategoryListItem checklistCategoryListItem)
    {
        assembleTaskColumns();

        disposeTable();

        buildTable();

        setItems(checklistCategoryListItem.getChecklists());
    }

    private void assembleTaskColumns()
    {
        for (ChecklistTableItem checklist : checklistCategoryListItem.getChecklists())
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

    private class CheckableTableCell extends TableCell<ChecklistTableItem, ChecklistTableItem>
    {
        private final String name;

        CheckableTableCell(String name)
        {
            this.name = name;
        }

        @Override
        protected void updateItem(ChecklistTableItem item, boolean empty)
        {
            super.updateItem(item, empty);

            if (empty || item == null)
            {
                setText(null);
                setGraphic(null);
            }
            else
            {
                Optional<BooleanComponent> booleanComponent = item.getTaskBooleanComponent(name);
                if (booleanComponent.isPresent())
                {
                    setGraphic(booleanComponent.get().getRenderer());
                }
                else
                {
                    setGraphic(new Label("n/a"));
                }
            }
        }
    }
}
