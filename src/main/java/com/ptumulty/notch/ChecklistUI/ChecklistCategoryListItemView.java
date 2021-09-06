package com.ptumulty.notch.ChecklistUI;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ChecklistCategoryListItemView extends HBox
{
    private final ChecklistCategoryListItem categoryListItem;
    private final Label categoryTitle;

    ChecklistCategoryListItemView(ChecklistCategoryListItem categoryListItem)
    {
        this.categoryListItem = categoryListItem;
        categoryTitle = new Label(categoryListItem.getCategoryTitle());
        this.categoryListItem.getCategory().getCategoryTitleModel().addListener(() ->
        {
            if (!this.categoryListItem.getCategory().getCategoryTitle().equals(categoryTitle.getText()))
            {
                categoryTitle.setText(this.categoryListItem.getCategory().getCategoryTitle());
            }
        });
        configureVisuals();
    }

    public ChecklistCategoryListItem getCategoryListItem()
    {
        return categoryListItem;
    }

    private void configureVisuals()
    {
        getChildren().add(categoryTitle);
    }
}
