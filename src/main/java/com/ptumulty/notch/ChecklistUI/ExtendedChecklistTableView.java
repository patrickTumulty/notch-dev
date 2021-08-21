package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.ceramicfx.CancelableActionPopupWindow;
import com.ptumulty.ceramic.components.BooleanComponent;
import com.ptumulty.ceramic.components.ListSelectionListener;
import com.ptumulty.notch.Checklist.Checklist;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExtendedChecklistTableView extends StackPane implements ListSelectionListener<ChecklistCategoryListItemView>
{
    private final int PREF_COLUMN_WIDTH = 75;

    private final Map<String, TableColumn<ChecklistTableItem, ChecklistTableItem>> columnMap;
    private ChecklistCategoryListItem checklistCategoryListItem;
    private final TableView<ChecklistTableItem> tableView;
    private Button createChecklistButton;

    public ExtendedChecklistTableView()
    {
        columnMap = new HashMap<>();
        tableView = new TableView<>();
        tableView.setFixedCellSize(35);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        getChildren().add(tableView);
        StackPane.setAlignment(tableView, Pos.CENTER);

        configureCreateChecklistButton();

        buildTable();
    }

    private void configureCreateChecklistButton()
    {
        createChecklistButton = new Button();
        FontIcon icon = new FontIcon(FontAwesomeSolid.PLUS);
        icon.setIconSize(15);
        icon.setIconColor(Color.WHITE);
        createChecklistButton.setGraphic(icon);
        createChecklistButton.setId("add-checklist-button");
        createChecklistButton.disableProperty().set(true);
        createChecklistButton.setShape(new Circle(50));
        createChecklistButton.setPrefSize(50, 50);
        createChecklistButton.setOnAction(event ->
        {
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(10,10,0,10));

            TextField textField = new TextField();
            vBox.getChildren().add(textField);

            EventHandler<ActionEvent> createChecklist = event1 ->
            {
                Checklist checklist = new Checklist(textField.getText());
                checklistCategoryListItem.getCategory().getChecklists().addItem(checklist);
                onContextChange(checklistCategoryListItem);
            };

            CancelableActionPopupWindow popupWindow = new CancelableActionPopupWindow(vBox, "Create", createChecklist);
            popupWindow.setStylesheet("css/main-stylesheet.css");
            popupWindow.setActionButtonID("action-button");
            popupWindow.setWindowTitle("Create Checklist");
            popupWindow.show(250, 100);
        });
        getChildren().add(createChecklistButton);
        StackPane.setAlignment(createChecklistButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(createChecklistButton, new Insets(0, 10, 10, 0) );
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

        createChecklistButton.disableProperty().set(false);

        tableView.setItems(checklistCategoryListItem.getChecklists());
    }

    private void assembleTaskColumns()
    {
        for (ChecklistTableItem checklist : checklistCategoryListItem.getChecklists())
        {
            for (String columnTitle : checklist.getChecklist().getChecklistTaskNames())
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
