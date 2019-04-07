package com.kiwi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.kiwi.clientside.ClientController;
import com.kiwi.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

public class LeaderboardActivity extends AppCompatActivity {
    ClientController clientController;
    User user;
    private Intent friendsIntent;
    private Intent settingsIntent;
    private Intent mainPageIntent;
    private Intent actionIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboards);
        clientController = new ClientController((User) getIntent().getSerializableExtra("user"), true);
        clientController.updateUser();
        user = clientController.getUser();

        friendsIntent = new Intent(LeaderboardActivity.this, FriendsActivity.class);
        settingsIntent = new Intent(LeaderboardActivity.this, SettingsActivity.class);
        mainPageIntent = new Intent(LeaderboardActivity.this, MainpageActivity.class);
        actionIntent = new Intent(LeaderboardActivity.this, ActionActivity.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        clientController = new ClientController((User) getIntent().getSerializableExtra("user"), true);
        clientController.updateUser();
        clientController.updateTop50();
        List<User> top50 = clientController.getTop50();
        List<User> no1Player = new ArrayList<>();
        no1Player.add(top50.get(0));

        List<Integer> no1Pos = new ArrayList<>();
        no1Pos.add(1);

        List<Integer> top50pos = new ArrayList<>();
        for (int i = 1; i <= top50.size(); i++)
            top50pos.add(i);

        Integer position = Integer.parseInt(clientController.getPosition());
        List<Integer> yourRos = new ArrayList<>();
        yourRos.add(position);

        List<User> userList = new ArrayList<>();
        userList.add(clientController.getUser());

        RecyclerView.Adapter leadAdapterTop50 = new LeaderboardAdapter(top50pos, top50, getApplication());
        RecyclerView recyclerTop50View = findViewById(R.id.recyclerTop50View);
        recyclerTop50View.setHasFixedSize(true);
        recyclerTop50View.setAdapter(leadAdapterTop50);
        recyclerTop50View.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.Adapter leadAdapterNo1 = new LeaderboardAdapter(no1Pos, no1Player, getApplication());
        RecyclerView recyclerNo1View = findViewById(R.id.no1RecyclerView);
        recyclerNo1View.setHasFixedSize(true);
        recyclerNo1View.setAdapter(leadAdapterNo1);
        recyclerNo1View.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.Adapter leadAdapterPos = new LeaderboardAdapter(yourRos, userList, getApplication());
        RecyclerView recyclerPosView = findViewById(R.id.positionRecyclerView);
        recyclerPosView.setHasFixedSize(true);
        recyclerPosView.setAdapter(leadAdapterPos);
        recyclerPosView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
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
            case R.id.action_actionpage:
                actionIntent.putExtra("user", clientController.getUser());
                startActivity(actionIntent);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
