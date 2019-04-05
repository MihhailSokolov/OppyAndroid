package com.kiwi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class Action implements Serializable {

    private String id;
    private String actionName;
    private String category;
    private int points;

    public Action(){}

    /**
     * Constructor for com.kiwi.oppy.Action.
     * @param actionName Name of the action
     * @param category Category of the action
     * @param points Number of points the action gives
     */
    @JsonCreator
    public Action(@JsonProperty("actionName") String actionName, @JsonProperty("category") String category,
                  @JsonProperty("points") int points) {
        this.actionName = actionName;
        this.category = category;
        this.points = points;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return String.format("com.kiwi.oppy.Action[actionName='%s', category='%s', points='%s']",
                getActionName(), getCategory(), String.valueOf(getPoints()));
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Action)) {
            return false;
        }
        Action action = (Action) other;
        boolean equalPoints = getPoints() == action.getPoints();
        boolean equalActions = Objects.equals(getActionName(), action.getActionName());
        boolean equalCategory = Objects.equals(getCategory(), action.getCategory());
        return equalPoints && equalActions && equalCategory;
    }
}
