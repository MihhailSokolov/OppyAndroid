package com.kiwi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kiwi.clientside.ClientController;
import com.kiwi.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FriendsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ClientController clientController;

    /*TODO
     * Sort to points
     * Get friendlist from server
     * Prettify (add user profile pic? change text color etc)
     * Add friend functionality
     * Delete friend functionality
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        clientController = new ClientController((User)getIntent().getSerializableExtra("user"), true);
        clientController.updateUser();
        clientController.updateFriendList();
        List<User> users = clientController.getUser().getFriends();

        mAdapter = new UserAdapter(users, getApplication());


        recyclerView = findViewById(R.id.recyclerview);


        recyclerView.setHasFixedSize(true);

        recyclerView.setAdapter(mAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}
