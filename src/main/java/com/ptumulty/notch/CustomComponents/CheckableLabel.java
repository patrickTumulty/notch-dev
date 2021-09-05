package com.ptumulty.notch.CustomComponents;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


public class CheckableLabel extends HBox
{
    private final CheckBox checkbox;
    private final Label label;

    public CheckableLabel(String labelTitle)
    {
        this(labelTitle, true);
    }

    public CheckableLabel(String labelTitle, boolean checked)
    {
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(5));
        setSpacing(5);

        checkbox = new CheckBox();
        checkbox.selectedProperty().addListener(
                (observable, oldValue, newValue) ->
                        setStyle(newValue ? "-fx-background-color: -accent-light" : "-fx-background-color: transparent"));
        checkbox.setSelected(checked);

        getChildren().add(checkbox);

        label = new Label(labelTitle);
        getChildren().add(label);

        setId("checkable-label");
    }

    public boolean isSelected()
    {
        return checkbox.selectedProperty().get();
    }

    public String getLabelText()
    {
        return label.getText();
    }

}
