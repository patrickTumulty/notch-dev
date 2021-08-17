package com.ptumulty.notch.ChecklistUI;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ChecklistCategoryListItemView extends HBox
{
    private final ChecklistCategoryListItem categoryListItem;

    ChecklistCategoryListItemView(ChecklistCategoryListItem categoryListItem)
    {
        this.categoryListItem = categoryListItem;

        configureVisuals();
    }

    public ChecklistCategoryListItem getCategoryListItem()
    {
        return categoryListItem;
    }

    private void configureVisuals()
    {
        getChildren().add(new Label(categoryListItem.getCategoryTitle()));
    }
}
