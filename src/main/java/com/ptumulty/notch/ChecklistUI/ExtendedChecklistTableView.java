package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.components.BooleanComponent;
import com.ptumulty.ceramic.components.ListSelectionListener;
import com.ptumulty.notch.Checklist.Checklist;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ExtendedChecklistTableView extends StackPane implements ListSelectionListener<ChecklistCategoryListItemView>
{
    private final Map<String, TableColumn<ChecklistTableItem, ChecklistTableItem>> columnMap;
    private ChecklistCategoryListItem checklistCategoryListItem;
    private final TableView<ChecklistTableItem> tableView;
    private final Button createChecklistButton;

    public ExtendedChecklistTableView()
    {
        columnMap = new HashMap<>();
        tableView = new TableView<>();

        getChildren().add(tableView);
        StackPane.setAlignment(tableView, Pos.CENTER);

        createChecklistButton = new Button("+");
        createChecklistButton.setOnAction(event ->
        {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            VBox vBox = new VBox();
            vBox.setAlignment(Pos.CENTER);
            vBox.setPadding(new Insets(10));
            vBox.setSpacing(10);

            TextField textField = new TextField();
            vBox.getChildren().add(textField);

            Button createButton = new Button("Create");
            createButton.setOnAction(event2 ->
            {
                Checklist checklist = new Checklist(textField.getText());
                checklistCategoryListItem.getCategory().getChecklists().addItem(checklist);
                stage.close();
            });
            vBox.getChildren().add(createButton);

            stage.setScene(new Scene(vBox, 200, 100));
            stage.show();
        });


        createChecklistButton.disableProperty().set(true);
        createChecklistButton.setShape(new Circle(40));
        createChecklistButton.setPrefSize(40, 40);

        getChildren().add(createChecklistButton);
        StackPane.setAlignment(createChecklistButton, Pos.TOP_RIGHT);
        StackPane.setMargin(createChecklistButton, new Insets(34, 10, 0, 0) );
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

        if (checklistCategoryListItem.getChecklists().isEmpty())
        {
            tableView.setItems(FXCollections.emptyObservableList());
        }
        else
        {
            tableView.setItems(checklistCategoryListItem.getChecklists());
        }
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
        checklistNameColumn.setSortable(false);
        checklistNameColumn.setCellValueFactory(param -> param.getValue().titleProperty());
        tableView.getColumns().add(checklistNameColumn);
    }

    @Override
    public void itemSelected(ChecklistCategoryListItemView selectedItem)
    {
        setChecklistCategoryListItem(selectedItem.getCategoryListItem());
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
