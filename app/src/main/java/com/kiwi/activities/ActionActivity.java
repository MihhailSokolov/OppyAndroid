package com.kiwi.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.kiwi.clientside.ClientController;
import com.kiwi.model.Action;
import com.kiwi.model.ExpandableListAdapter;

import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.kiwi.model.ExpandableListData;
import com.kiwi.model.ListViewAdapter;
import com.kiwi.model.Preset;
import com.kiwi.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActionActivity extends AppCompatActivity {

    private Button savebutton;
    private Button submitButton;
    private Switch solarPanelSwitch;
    private Toolbar toolbar;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private List<String> expandableListTitle;
    private HashMap<String, List<Action>> expandableListDetail;
    private ClientController clientController;

    private List<Action> actionList;
    private List<Action> selectedActions;

    private Intent friendsIntent;
    private Intent settingsIntent;
    private Intent mainPageIntent;
    private Intent leaderboardIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        clientController = new ClientController((User)getIntent().getSerializableExtra("user"), true);
        clientController.updateUser();

        clientController.updateActionList();
        actionList = clientController.getActionList();

        toolbar = findViewById(R.id.toolbar);
        savebutton = findViewById(R.id.saveButton);
        submitButton = findViewById(R.id.submitButton);
        solarPanelSwitch = findViewById(R.id.solarPanelSwitch);

        expandableListView = findViewById(R.id.elv);
        listView = findViewById(R.id.lv);

        expandableListDetail = ExpandableListData.getData(actionList);
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new ExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        selectedActions = new ArrayList<>();
        listViewAdapter = new ListViewAdapter(this, selectedActions);
        listView.setAdapter(listViewAdapter);


        friendsIntent = new Intent(ActionActivity.this, FriendsActivity.class);
        settingsIntent = new Intent(ActionActivity.this, SettingsActivity.class);
        mainPageIntent = new Intent(ActionActivity.this, MainpageActivity.class);
        leaderboardIntent = new Intent(ActionActivity.this, LeaderboardActivity.class);

        setSupportActionBar(toolbar);
        setSwitch();
        setListGroupListener();
        setListChildListener();
        setSubmitButtonListener();
        setSaveButtonListener();
        setListViewListener();
    }

    private void setSwitch() {
        solarPanelSwitch.setChecked(clientController.getUser().getHasSolarPanels());

        solarPanelSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final boolean hasSolarPanels = clientController.getUser().getHasSolarPanels();
                AlertDialog.Builder builder = new AlertDialog.Builder(ActionActivity.this);
                builder.setTitle("Toggle solar panels?");
                if(!hasSolarPanels){
                    builder.setMessage(getString(R.string.pos_switch_text) +
                            "\n\nAre you sure you want to continue?");
                } else {
                    builder.setMessage(getString(R.string.neg_switch_text) +
                            "\n\nAre you sure you want to continue?");
                }
                builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clientController.updateSolarPanel(!hasSolarPanels);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        solarPanelSwitch.setChecked(clientController.getUser().getHasSolarPanels());
                    }
                });
                builder.show();
            }
        });

    }

    private void setSubmitButtonListener(){
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String responseMsg;
                if (selectedActions.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "You haven't selected any actions!", Toast.LENGTH_LONG).show();
                } else {
                    responseMsg = clientController.takeActions(selectedActions);
                    if (responseMsg.equals("true")) {
                        selectedActions.clear();
                        listViewAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Well done! You made Oppy a little bit happier!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void setSaveButtonListener() {
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedActions.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "You haven't selected any actions!", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActionActivity.this);
                    builder.setTitle("Selected actions...");

                    final EditText presetNameText = new EditText(ActionActivity.this);
                    presetNameText.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(presetNameText);
                    builder.setPositiveButton("Save!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String presetName = presetNameText.getText().toString();
                            if (presetName.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "No preset name given.", Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                List<String> actionNames = new ArrayList<>();
                                for (Action act : selectedActions) {
                                    actionNames.add(act.getActionName());
                                }
                                Preset preset = new Preset(presetName, actionNames);
                                String responseMsg = clientController.addPreset(preset);
                                if (responseMsg.equals("true")) {
                                    Toast.makeText(getApplicationContext(), "Preset added!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Something went wrong...", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            }
        });
    }


    private void setListGroupListener(){
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });
    }

    private void setListChildListener(){
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                Action selectedAction = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition);
                if(!selectedActions.contains(selectedAction)) {
                    selectedActions.add(selectedAction);
                    listViewAdapter.notifyDataSetChanged();
                    Toast.makeText(
                            getApplicationContext(),
                            "Added to list"
                            , Toast.LENGTH_SHORT
                    ).show();
//                    parent.setItemChecked(index, true);
                }
                else{
//                    parent.setItemChecked(index, false);
                    selectedActions.remove(selectedAction);
                    listViewAdapter.notifyDataSetChanged();
                    Toast.makeText(
                            getApplicationContext(),
                                    "Removed from list."
                            , Toast.LENGTH_SHORT
                    ).show();
                }
                return false;
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                settingsIntent.putExtra("user", clientController.getUser());
                startActivity(settingsIntent);
                return true;

            case R.id.action_friends:
                friendsIntent.putExtra("user", clientController.getUser());
                startActivity(friendsIntent);
                return true;
            case R.id.action_mainpage:
                mainPageIntent.putExtra("user", clientController.getUser());
                startActivity(mainPageIntent);
                return true;

            case R.id.action_leaderboard:
                leaderboardIntent.putExtra("user", clientController.getUser());
                startActivity(leaderboardIntent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void setListViewListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if(selectedActions.contains(listView.getItemAtPosition(position))){
                    selectedActions.remove(listView.getItemAtPosition(position));
                    Toast.makeText(
                            getApplicationContext(),
                            "Removed from list."
                            , Toast.LENGTH_SHORT
                    ).show();
                    listViewAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}