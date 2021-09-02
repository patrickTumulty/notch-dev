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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class ExtendedChecklistTableView extends BorderPane implements ListSelectionListener<ChecklistCategoryListItemView>
{
    private final int PREF_COLUMN_WIDTH = 75;

    private final Map<String, TableColumn<ChecklistTableItem, ChecklistTableItem>> columnMap;
    private final TableView<ChecklistTableItem> tableView;
    private ChecklistCategoryListItem checklistCategoryListItem;
    private Button createChecklistButton;
    private HBox controlBar;
    private BorderPane controlBarContainer;
    private TextField filterField;
    private ChoiceBox<VisibilityOptions> visibilityOptions;

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

        configureVisibilityOption();

        configureCreateChecklistButton();
    }

    private void configureVisibilityOption()
    {
        visibilityOptions = new ChoiceBox<>();
        visibilityOptions.getItems().addAll(List.of(VisibilityOptions.ALL,
                                                    VisibilityOptions.COMPLETED,
                                                    VisibilityOptions.NOT_COMPLETED));
        visibilityOptions.getSelectionModel().select(VisibilityOptions.ALL);
        visibilityOptions.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (checklistCategoryListItem == null)
            {
                return;
            }

            checklistCategoryListItem.getFilterChecklists().setPredicate(getCombinedPredicate());
        });
        controlBar.getChildren().add(visibilityOptions);
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

            checklistCategoryListItem.getFilterChecklists().setPredicate(getCombinedPredicate());
        });
        controlBar.getChildren().add(filterField);
    }

    private Predicate<ChecklistTableItem> getCombinedPredicate()
    {
        Predicate<ChecklistTableItem> filterFieldPredicate =
                filterField.getText().isBlank() ?
                        p -> true :
                        tableItem -> tableItem.getChecklist()
                                              .getName()
                                              .get()
                                              .contains(filterField.getText());

        Predicate<ChecklistTableItem> visibilityPredicate = tableItem ->
        {
            switch (visibilityOptions.getSelectionModel().getSelectedItem()) {
                case COMPLETED:
                    return tableItem.getChecklist().isComplete();
                case NOT_COMPLETED:
                    return !tableItem.getChecklist().isComplete();
            }
            return true;
        };

        return tableItem -> filterFieldPredicate.test(tableItem) && visibilityPredicate.test(tableItem);
    }

    private void configureControlBar()
    {
        controlBar = new HBox();
        controlBarContainer = new BorderPane();
        controlBarContainer.setCenter(controlBar);

        controlBar.setAlignment(Pos.CENTER_LEFT);
        controlBar.setPadding(new Insets(5, 10, 5, 10));
        controlBar.setSpacing(10);
        BorderPane.setAlignment(controlBar, Pos.CENTER);
        setTop(controlBarContainer);
    }

    private void configureCreateChecklistButton()
    {
        createChecklistButton = new Button();
        FontIcon icon = new FontIcon(FontAwesomeSolid.PLUS);
        icon.setIconSize(20);
        icon.setIconColor(Color.WHITE);
        createChecklistButton.setGraphic(icon);
        createChecklistButton.disableProperty().set(true);
        createChecklistButton.setOnAction(event -> new CreateChecklistPopupWindow(checklistCategoryListItem));
        controlBarContainer.setRight(createChecklistButton);
        BorderPane.setAlignment(createChecklistButton, Pos.CENTER);
        BorderPane.setMargin(createChecklistButton, new Insets(5, 10, 5, 10));
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

    private enum VisibilityOptions
    {
        ALL("All"),
        COMPLETED("Completed"),
        NOT_COMPLETED("Not Completed");

        private final String name;

        VisibilityOptions(String name)
        {
            this.name = name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
}
