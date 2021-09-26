package com.ptumulty.notch.CustomComponents;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class CheckableRegion extends StackPane
{
    private FontIcon checkIcon;
    private FontIcon uncheckedIcon;
    private FontIcon notApplicableIcon;

    private Background checkedBackground;
    private Background uncheckedBackground;
    private Background notApplicableBackground;

    private final Label label;

    private BooleanProperty checkedProperty;

    private boolean active;
    private boolean checked;

    public CheckableRegion()
    {
        this(false);
    }

    public CheckableRegion(boolean initialState)
    {
        active = true;
        checked = initialState;
        checkedProperty = new SimpleBooleanProperty(checked);

        configureIcons();

        configureBackgrounds();

        label = new Label();
        label.setGraphic(getStateIcon());
        getChildren().add(label);

        checkedProperty.addListener((observable, oldValue, newValue) ->
        {
            if (oldValue != newValue)
            {
                checked = newValue;
                label.setGraphic(getStateIcon());

                setBackground(getStateBackground());
            }
        });

        setBackground(getStateBackground());

        configureMouseListener();
    }

    private Background getStateBackground()
    {
        return checked ? checkedBackground : uncheckedBackground;
    }

    private void configureBackgrounds()
    {
        checkedBackground = new Background(new BackgroundFill(Color.LAWNGREEN, null, null));
        uncheckedBackground = new Background(new BackgroundFill(Color.PALEVIOLETRED, null, null));
        notApplicableBackground = new Background(new BackgroundFill(Color.TRANSPARENT, null, null));
    }

    private void configureMouseListener()
    {
        setOnMouseClicked(event ->
        {
            if (active)
            {
                checkedProperty.set(!checked);
            }
        });
    }

    private void configureIcons()
    {
        checkIcon = new FontIcon(FontAwesomeSolid.CHECK);
        checkIcon.setIconSize(30);
        checkIcon.setIconColor(Color.GREEN);

        uncheckedIcon = new FontIcon(FontAwesomeSolid.TIMES);
        uncheckedIcon.setIconSize(30);
        uncheckedIcon.setIconColor(Color.DARKRED);

        notApplicableIcon = new FontIcon(FontAwesomeSolid.ELLIPSIS_H);
        notApplicableIcon.setIconSize(30);
        notApplicableIcon.setIconColor(Color.WHITE);
    }

    public void setActive(boolean isActive)
    {
        active = isActive;
        label.setGraphic(!active ? notApplicableIcon : getStateIcon());
        setBackground(!active ? notApplicableBackground : getStateBackground());
    }

    private FontIcon getStateIcon()
    {
        return checked ? checkIcon : uncheckedIcon;
    }

    public BooleanProperty checkedProperty()
    {
        return checkedProperty;
    }
}
