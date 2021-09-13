package com.ptumulty.notch.ChecklistUI;

import com.ptumulty.ceramic.components.BooleanComponent;
import com.ptumulty.ceramic.models.BooleanModel;
import com.ptumulty.ceramic.models.ListModel;
import com.ptumulty.notch.ChecklistUI.popups.ConfigureCategoryPopupWindow;
import com.ptumulty.notch.ChecklistUI.popups.ConfigureChecklistPopupWindow;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
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

public class ExtendedChecklistTableView extends BorderPane implements ListModel.ListModelListener<String>
{
    private final int PREF_COLUMN_WIDTH = 75;

    private final Map<String, TableColumn<ChecklistTableItem, ChecklistTableItem>> taskColumnMap;
    private final TableView<ChecklistTableItem> tableView;
    private ChecklistCategoryListItem checklistCategoryListItem;
    private Button createChecklistButton;
    private Button createCategoryButton;
    private Button configureCategoryButton;
    private HBox controlBarLeft;
    private HBox controlBarRight;
    private TextField filterField;
    private ChoiceBox<VisibilityOptions> visibilityOptions;
    private ChecklistCategoryChoiceBox checklistCategorySelect;


    public ExtendedChecklistTableView()
    {
        taskColumnMap = new HashMap<>();
        tableView = new TableView<>();
        tableView.setFixedCellSize(35);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPlaceholder(new Label("No Category Selected"));

        configureControlbar();

        setCenter(tableView);
        BorderPane.setAlignment(tableView, Pos.CENTER);

        configureFilterField();

        configureVisibilityOption();

        configureCategorySelectChoiceBox();

        configureConfigureCategoryButton();

        configureCreateCategoryButton();

        controlBarRight.getChildren().add(new Separator(Orientation.VERTICAL));

        configureCreateChecklistButton();
    }

    private void configureConfigureCategoryButton()
    {
        configureCategoryButton = new Button();
        configureCategoryButton.disableProperty().set(true);
        FontIcon fontIcon = new FontIcon(FontAwesomeSolid.COG);
        fontIcon.setIconSize(20);
        fontIcon.setIconColor(Color.WHITE);
        configureCategoryButton.setGraphic(fontIcon);
        configureCategoryButton.setOnAction(event ->
                new ConfigureCategoryPopupWindow(Optional.of(checklistCategoryListItem.getCategory())));
        controlBarRight.getChildren().add(configureCategoryButton);
    }

    private void configureCategorySelectChoiceBox()
    {
        checklistCategorySelect = new ChecklistCategoryChoiceBox();
        checklistCategorySelect.getSelectionModel()
                               .selectedItemProperty()
                               .addListener((observable, oldValue, newValue) ->
                                       setChecklistCategoryListItem(newValue));
        controlBarRight.getChildren().add(checklistCategorySelect);
    }

    private void configureCreateCategoryButton()
    {
        createCategoryButton = new Button();
        createCategoryButton.setOnAction(event -> new ConfigureCategoryPopupWindow());
        FontIcon fontIcon = new FontIcon(FontAwesomeSolid.FOLDER_PLUS);
        fontIcon.setIconSize(20);
        fontIcon.setIconColor(Color.WHITE);
        createCategoryButton.setGraphic(fontIcon);
        controlBarRight.getChildren().add(createCategoryButton);
    }

    private void configureControlbar()
    {
        controlBarLeft = new HBox();
        controlBarLeft.setAlignment(Pos.CENTER_LEFT);
        controlBarLeft.setPadding(new Insets(5, 10, 5, 10));
        controlBarLeft.setSpacing(10);

        controlBarRight = new HBox();
        controlBarRight.setAlignment(Pos.CENTER_RIGHT);
        controlBarRight.setPadding(new Insets(5, 10, 5, 10));
        controlBarRight.setSpacing(10);

        BorderPane controlBarContainer = new BorderPane();
        setTop(controlBarContainer);

        controlBarContainer.setLeft(controlBarLeft);
        controlBarContainer.setRight(controlBarRight);
        BorderPane.setAlignment(controlBarLeft, Pos.CENTER);
        BorderPane.setAlignment(controlBarRight, Pos.CENTER);
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
        controlBarLeft.getChildren().add(visibilityOptions);
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
        controlBarLeft.getChildren().add(filterField);
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

    private void configureCreateChecklistButton()
    {
        createChecklistButton = new Button();
        FontIcon icon = new FontIcon(FontAwesomeSolid.PLUS);
        icon.setIconSize(20);
        icon.setIconColor(Color.WHITE);
        createChecklistButton.setGraphic(icon);
        createChecklistButton.disableProperty().set(true);
        createChecklistButton.setOnAction(event ->
                new ConfigureChecklistPopupWindow(checklistCategoryListItem, Optional.empty()));

        controlBarRight.getChildren().add(createChecklistButton);
    }

    private void disposeTable()
    {
        tableView.getColumns().clear();
        taskColumnMap.clear();
    }

    private void buildTable()
    {
        configureNameColumn();

        for (String taskName : taskColumnMap.keySet())
        {
            if (taskColumnMap.get(taskName) == null)
            {
                configureTaskColumn(taskName);
            }
        }
    }

    private void configureTaskColumn(String taskName)
    {
        TableColumn<ChecklistTableItem, ChecklistTableItem> checklistTaskColumn = new TableColumn<>(taskName);
        checklistTaskColumn.setSortable(false);
        checklistTaskColumn.setMinWidth(PREF_COLUMN_WIDTH);
        checklistTaskColumn.setCellValueFactory(TableColumn.CellDataFeatures::getValue);
        checklistTaskColumn.setCellFactory(param -> new CheckableTableCell(taskName));
        tableView.getColumns().add(checklistTaskColumn);
        taskColumnMap.put(taskName, checklistTaskColumn);
    }

    public void setChecklistCategoryListItem(ChecklistCategoryListItem checklistCategoryListItem)
    {
        if (this.checklistCategoryListItem != checklistCategoryListItem)
        {
            if (this.checklistCategoryListItem != null)
            {
                this.checklistCategoryListItem.getCategory().getDefaultChecklistTasksModel().removeListener(this);
            }

            this.checklistCategoryListItem = checklistCategoryListItem;

            this.checklistCategoryListItem.getCategory().getDefaultChecklistTasksModel().addListener(this);

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
        configureCategoryButton.disableProperty().set(false);

        tableView.setItems(checklistCategoryListItem.getFilterChecklists());
    }

    private void reloadTable()
    {
        onContextChange(checklistCategoryListItem);
    }

    private void assembleTaskColumns()
    {
        for (String task : checklistCategoryListItem.getCategory().getCategoryTasksSnapshot())
        {
            addTaskToTaskColumnMap(task);
        }
    }

    private void configureNameColumn()
    {
        TableColumn<ChecklistTableItem, ChecklistTableItem> checklistNameColumn = new TableColumn<>("Item Name");
        checklistNameColumn.setEditable(false);
        checklistNameColumn.setReorderable(false);
        checklistNameColumn.setSortable(false);
        checklistNameColumn.setMinWidth(PREF_COLUMN_WIDTH);
        checklistNameColumn.setCellValueFactory(TableColumn.CellDataFeatures::getValue);
        checklistNameColumn.setCellFactory(param -> new ChecklistNameTableCell(checklistCategoryListItem));
        tableView.getColumns().add(checklistNameColumn);
    }

    private void addTaskToTaskColumnMap(String item)
    {
        if (!taskColumnMap.containsKey(item))
        {
            taskColumnMap.put(item, null);
        }
    }

    @Override
    public void itemAdded(String item)
    {
        addTaskToTaskColumnMap(item);
        configureTaskColumn(item);
    }

    @Override
    public void itemRemoved(String item)
    {
        if (taskColumnMap.containsKey(item))
        {
            tableView.getColumns().remove(taskColumnMap.get(item));
            taskColumnMap.remove(item);
        }
    }

    @Override
    public void listChanged()
    {
        reloadTable();
    }

    private static class ChecklistNameTableCell extends TableCell<ChecklistTableItem, ChecklistTableItem>
    {
        private ChecklistCategoryListItem checklistCategoryListItem;

        ChecklistNameTableCell(ChecklistCategoryListItem checklistCategoryListItem)
        {
            this.checklistCategoryListItem = checklistCategoryListItem;
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
                Button button = new Button();
                button.setOnAction(event -> new ConfigureChecklistPopupWindow(checklistCategoryListItem, Optional.of(item)));
                button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent");
                FontIcon fontIcon = new FontIcon(FontAwesomeSolid.COG);
                fontIcon.setIconColor(Color.WHITE);

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().add(button);
                hBox.getChildren().add(new Label(item.getChecklist().getName().get()));

                button.setGraphic(fontIcon);
                setGraphic(hBox);
            }
        }
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
                    this.setOnMouseClicked(event ->
                    {
                        if (item.getChecklist().getTaskState(name).isPresent())
                        {
                            BooleanModel booleanModel = item.getChecklist().getTaskState(name).get();
                            boolean currentState = booleanModel.get();
                            booleanModel.setValue(!currentState);
                        }
                    });
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
