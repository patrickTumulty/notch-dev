package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.components.BooleanComponent;
import com.ptumulty.ceramic.components.ListSelectionListener;
import com.ptumulty.notch.ChecklistUI.popups.CreateChecklistPopupWindow;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExtendedChecklistTableView extends BorderPane implements ListSelectionListener<ChecklistCategoryListItemView>
{
    private final int PREF_COLUMN_WIDTH = 75;

    private final Map<String, TableColumn<ChecklistTableItem, ChecklistTableItem>> columnMap;
    private final TableView<ChecklistTableItem> tableView;
    private ChecklistCategoryListItem checklistCategoryListItem;
    private Button createChecklistButton;
    private HBox controlBar;
    private TextField filterField;

    public ExtendedChecklistTableView()
    {
        columnMap = new HashMap<>();
        tableView = new TableView<>();
        tableView.setFixedCellSize(35);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No Category Selected"));

        setCenter(tableView);
        BorderPane.setAlignment(tableView, Pos.CENTER);

        configureControlBar();

        configureFilterField();

        configureCreateChecklistButton();
    }

    private void configureFilterField()
    {
        filterField = new TextField();
        filterField.setPromptText("Filter");
        filterField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (checklistCategoryListItem == null)
            {
                return;
            }

            checklistCategoryListItem.getFilterChecklists().setPredicate(
                    filterField.getText().isBlank() ?
                            p -> true :
                            tableItem -> tableItem.getChecklist()
                                                  .getName()
                                                  .get()
                                                  .contains(filterField.getText()));
        });
        controlBar.getChildren().add(filterField);
    }

    private void configureControlBar()
    {
        controlBar = new HBox();
        controlBar.setAlignment(Pos.CENTER_LEFT);
        controlBar.setPadding(new Insets(5, 10, 5, 10));
        controlBar.setSpacing(10);
        BorderPane.setAlignment(controlBar, Pos.CENTER);
        setTop(controlBar);
    }

    private void configureCreateChecklistButton()
    {
        createChecklistButton = new Button();
        FontIcon icon = new FontIcon(FontAwesomeSolid.PLUS);
        icon.setIconSize(15);
        icon.setIconColor(Color.WHITE);
        createChecklistButton.setGraphic(icon);
        createChecklistButton.disableProperty().set(true);
        createChecklistButton.setOnAction(event -> new CreateChecklistPopupWindow(checklistCategoryListItem));
        controlBar.getChildren().add(createChecklistButton);
    }

    private void disposeTable()
    {
        tableView.getColumns().clear();
        columnMap.clear();
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
        checklistTaskColumn.setMinWidth(PREF_COLUMN_WIDTH);
        checklistTaskColumn.setCellValueFactory(TableColumn.CellDataFeatures::getValue);
        checklistTaskColumn.setCellFactory(param -> new CheckableTableCell(taskName));
        tableView.getColumns().add(checklistTaskColumn);
        columnMap.put(taskName, checklistTaskColumn);
    }

    public void setChecklistCategoryListItem(ChecklistCategoryListItem checklistCategoryListItem)
    {
        if (this.checklistCategoryListItem != checklistCategoryListItem)
        {
            this.checklistCategoryListItem = checklistCategoryListItem;
            onContextChange(this.checklistCategoryListItem);
        }
    }

    private void onContextChange(ChecklistCategoryListItem checklistCategoryListItem)
    {
        disposeTable();

        assembleTaskColumns();

        buildTable();

        tableView.setPlaceholder(new Label("Table Is Empty"));

        createChecklistButton.disableProperty().set(false);

        tableView.setItems(checklistCategoryListItem.getFilterChecklists());
    }

    private void assembleTaskColumns()
    {
        for (String task : checklistCategoryListItem.getCategory().getCategoryTasksSnapshot())
        {
            if (!columnMap.containsKey(task))
            {
                columnMap.put(task, null);
            }
        }
    }

    private void configureNameColumn()
    {
        TableColumn<ChecklistTableItem, String> checklistNameColumn = new TableColumn<>("Item Name");
        checklistNameColumn.setEditable(false);
        checklistNameColumn.setReorderable(false);
        checklistNameColumn.setSortable(false);
        checklistNameColumn.setMinWidth(PREF_COLUMN_WIDTH);
        checklistNameColumn.setCellValueFactory(param -> param.getValue().titleProperty());
        tableView.getColumns().add(checklistNameColumn);
    }

    @Override
    public void itemSelected(ChecklistCategoryListItemView selectedItem)
    {
        setChecklistCategoryListItem(selectedItem.getCategoryListItem());
    }

    private static class CheckableTableCell extends TableCell<ChecklistTableItem, ChecklistTableItem>
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
