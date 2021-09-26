package com.ptumulty.notch.CustomComponents;

import com.ptumulty.ceramic.components.UIComponent;
import com.ptumulty.ceramic.models.BooleanModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class CheckableRegionComponent extends UIComponent<BooleanModel, CheckableRegion>
{
    public CheckableRegionComponent(BooleanModel model)
    {
        super(model);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateModel()
    {
        model.setValue(renderer.checkedProperty().getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initializeRenderer()
    {
        renderer = new CheckableRegion();
        renderer.checkedProperty().addListener((observable, oldValue, newValue) ->
                model.setValue(renderer.checkedProperty().getValue()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChanged()
    {
        renderer.checkedProperty().set(model.get());

    }
}
