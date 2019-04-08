package com.kiwi.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kiwi.clientside.ClientController;
import com.kiwi.model.User;
import com.kiwi.model.UserAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class FriendsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private ClientController clientController;
    private Button addbutton;
    private Button delbutton;
    private TextView searchTextView;
    private List<User> friendList;
    private Intent mainPageIntent;
    private Intent settingsIntent;
    private Intent actionIntent;
    private Toolbar toolbar;
    private Intent leaderboardIntent;

    /*TODO
     * Prettify (add user profile pic? change text color etc)
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        settingsIntent = new Intent(FriendsActivity.this, SettingsActivity.class);
        leaderboardIntent = new Intent(FriendsActivity.this, LeaderboardActivity.class);
        clientController = new ClientController((User) getIntent().getSerializableExtra("user"),
                true);
        clientController.updateUser();
        clientController.updateFriendList();
        friendList = clientController.getUser().getFriends();
        Collections.sort(friendList, userScoreComparator);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainPageIntent = new Intent(FriendsActivity.this, MainpageActivity.class);
        settingsIntent = new Intent(FriendsActivity.this, SettingsActivity.class);
        actionIntent = new Intent(FriendsActivity.this, ActionActivity.class);


        mAdapter = new UserAdapter(friendList, getApplication());
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addbutton = findViewById(R.id.addbutton);
        delbutton = findViewById(R.id.deletebutton);
        searchTextView = findViewById(R.id.searchtext);

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> friendNames = getFriendNames(friendList);
                if (!friendNames.contains(searchTextView.getText().toString())
                        && !searchTextView.getText().toString().equals(clientController.getUser().getUsername())) {
                    String response =
                            clientController
                                    .addFriend(new User(searchTextView.getText().toString(),
                                            "", "", 0, new Date()));
                    if (response.equals("true")) {
                        Toast.makeText(getApplicationContext(), "Succes!",
                                Toast.LENGTH_SHORT).show();
                        updateFriendsList();
                    } else {
                        Toast.makeText(getApplicationContext(), "Could not follow this user.",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You are already following this user.",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        delbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String response =
                        clientController
                                .deleteFriend(new User(searchTextView.getText().toString(),
                                        "", "", 0, new Date()));
                if (response.equals("true")) {
                    Toast.makeText(getApplicationContext(), "Succes!",
                            Toast.LENGTH_SHORT).show();
                    updateFriendsList();
                } else {
                    Toast.makeText(getApplicationContext(), "Could not unfollow this user.",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

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

            case R.id.action_mainpage:
                mainPageIntent.putExtra("user", clientController.getUser());
                startActivity(mainPageIntent);
                return true;
            case R.id.action_leaderboard:
                leaderboardIntent.putExtra("user", clientController.getUser());
                startActivity(leaderboardIntent);
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

    private List<String> getFriendNames(List<User> list) {
        List<String> friendNames = new ArrayList<>();
        for (User u : list) {
            friendNames.add(u.getUsername());
        }
        return friendNames;
    }

    private void updateFriendsList() {
        clientController.updateFriendList();
        friendList.clear();
        friendList.addAll(clientController.getUser().getFriends());
        Collections.sort(friendList, userScoreComparator);
        mAdapter.notifyDataSetChanged();
    }

    private Comparator<User> userScoreComparator = new Comparator<User>() {
        @Override
        public int compare(User u1, User u2) {
            return Integer.compare(u2.getScore(), u1.getScore());
        }
    };


}
