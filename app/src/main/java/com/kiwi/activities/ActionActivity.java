package com.kiwi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kiwi.clientside.ClientController;
import com.kiwi.model.Action;
import com.kiwi.model.ExpandableListAdapter;

import android.widget.ExpandableListView;
import android.widget.Toast;

import com.kiwi.model.ExpandableListData;
import com.kiwi.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionActivity extends AppCompatActivity {

    private Button savebutton;
    private Button submitButton;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<Action>> expandableListDetail;
    private ClientController clientController;

    private List<Action> actionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        clientController = new ClientController((User)getIntent().getSerializableExtra("user"), true);
        clientController.updateUser();

        clientController.updateActionList();
        actionList = clientController.getActionList();


        savebutton = findViewById(R.id.saveButton);
        submitButton = findViewById(R.id.submitButton);
        expandableListView = findViewById(R.id.elv);

        expandableListDetail = ExpandableListData.getData(actionList);
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
//        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });
    }
}