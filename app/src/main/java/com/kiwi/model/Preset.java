package com.kiwi.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class Preset {

    private String name;
    private List<String> actionList;

    @JsonCreator
    public Preset(@JsonProperty("name") String name, @JsonProperty("actionList") List<String> actionList) {
        this.name = name;
        this.actionList = actionList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getActionList() {
        return actionList;
    }

    public void setActionList(List<String> actionList) {
        this.actionList = actionList;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Preset preset = (Preset) other;
        return Objects.equals(name, preset.name)
                && Objects.equals(actionList, preset.actionList);
    }
}
