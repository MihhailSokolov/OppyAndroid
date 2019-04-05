package com.kiwi.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListData {
       public static HashMap<String, List<String>> getData(List<Action> actionList) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<>();

        List <String> categories = new ArrayList<>();
        categories.add("Recycling");
        categories.add("energy");
        categories.add("misc");
        categories.add("transport");
        categories.add("food");

        List<String> recycling = getCategoryList("recycling", actionList);
        List<String> energy = getCategoryList("energy", actionList);
        List<String> food = getCategoryList("food", actionList);
        List<String> transport = getCategoryList("transport", actionList);
        List<String> misc = getCategoryList("misc", actionList);

        expandableListDetail.put("Recylcing", recycling);
        expandableListDetail.put("Energy", energy);
        expandableListDetail.put("Food", food);
        expandableListDetail.put("Transport", transport);
        expandableListDetail.put("Misc", misc);

        return expandableListDetail;
    }

    public static List<String> getCategoryList(String categoryName, List<Action> actionList){
        List<String> categoryList = new ArrayList<>();
        if (actionList != null) {
            for (Action act : actionList) {
                if (act.getCategory().toLowerCase().equals(categoryName.toLowerCase())) {
                    categoryList.add(act.getActionName());
                }
            }
            return categoryList;
        }
        return null;
    }
}
