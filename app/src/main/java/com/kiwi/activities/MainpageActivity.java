package com.kiwi.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiwi.clientside.ClientController;
import com.kiwi.model.User;

public class MainpageActivity extends AppCompatActivity {
    private ClientController clientController;
    private TextView decaytext;
    private TextView pointstext;
    private ImageView oppyimage;
    private Toolbar toolbar;
    private Intent friendsIntent;
    private Intent settingsIntent;
    private Intent leaderboardIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        clientController = new ClientController((User)getIntent().getSerializableExtra("user"), true);
        clientController.updateUser();

        friendsIntent = new Intent(MainpageActivity.this, FriendsActivity.class);
        settingsIntent = new Intent(MainpageActivity.this, SettingsActivity.class);
        leaderboardIntent = new Intent(MainpageActivity.this, LeaderboardActivity.class);

        pointstext = findViewById(R.id.pointstext);
        decaytext = findViewById(R.id.decaytext);
        oppyimage = findViewById(R.id.oppyimage);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        pointstext.setText(String.valueOf(clientController.getUser().getScore()));
        setOppyImage();
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


    private void setOppyImage(){
        int userScore = clientController.getUser().getScore();
        if (userScore >= 15000) {
            oppyimage.setImageDrawable(getResources().getDrawable(R.drawable.oppy1));
        } else if (userScore >= 10000) {
            oppyimage.setImageDrawable(getResources().getDrawable(R.drawable.oppy2));
        } else if (userScore >= 5000) {
            oppyimage.setImageDrawable(getResources().getDrawable(R.drawable.oppy3));
        } else if (userScore < -15000) {
            oppyimage.setImageDrawable(getResources().getDrawable(R.drawable.oppy7));
        } else if (userScore < -10000) {
            oppyimage.setImageDrawable(getResources().getDrawable(R.drawable.oppy6));
        } else if (userScore < -5000) {
            oppyimage.setImageDrawable(getResources().getDrawable(R.drawable.oppy5));
        } else {
            oppyimage.setImageDrawable(getResources().getDrawable(R.drawable.oppy4));
        }
    }
}
