package com.kiwi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListData {
       public static HashMap<String, List<Action>> getData(List<Action> actionList) {
        HashMap<String, List<Action>> expandableListDetail = new HashMap<>();

        List <String> categories = new ArrayList<>();
        categories.add("Recycling");
        categories.add("energy");
        categories.add("misc");
        categories.add("transport");
        categories.add("food");

        List<Action> recycling = getCategoryList("recycling", actionList);
        List<Action> energy = getCategoryList("energy", actionList);
        List<Action> food = getCategoryList("food", actionList);
        List<Action> transport = getCategoryList("transport", actionList);
        List<Action> misc = getCategoryList("misc", actionList);

        expandableListDetail.put("Recycling", recycling);
        expandableListDetail.put("Energy", energy);
        expandableListDetail.put("Food", food);
        expandableListDetail.put("Transport", transport);
        expandableListDetail.put("Misc", misc);

        return expandableListDetail;
    }

    public static List<Action> getCategoryList(String categoryName, List<Action> actionList){
        List<Action> categoryList = new ArrayList<>();
        if (actionList != null) {
            for (Action act : actionList) {
                if (act.getCategory().toLowerCase().equals(categoryName.toLowerCase())) {
                    categoryList.add(act);
                }
            }
            return categoryList;
        }
        return null;
    }
}
