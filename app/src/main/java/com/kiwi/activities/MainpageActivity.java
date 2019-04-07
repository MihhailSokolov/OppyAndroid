package com.kiwi.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kiwi.clientside.ClientController;
import com.kiwi.model.Preset;
import com.kiwi.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class MainpageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ClientController clientController;

    private TextView pointstext;
    private TextView decaypts;
    private Spinner spinner;
    private ImageView oppyimage;
    private Toolbar toolbar;
    private Button submittButton;
    private Button delButton;

    private Intent friendsIntent;
    private Intent settingsIntent;
    private Intent leaderboardIntent;
    private Intent actionIntent;


    private ArrayAdapter<String> dataAdapter;
    private List<String> listOfPresetNames;
    private String selectedPresetName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        clientController = new ClientController((User) getIntent().getSerializableExtra("user"), true);
        clientController.updateUser();

        friendsIntent = new Intent(MainpageActivity.this, FriendsActivity.class);
        settingsIntent = new Intent(MainpageActivity.this, SettingsActivity.class);
        leaderboardIntent = new Intent(MainpageActivity.this, LeaderboardActivity.class);
        actionIntent = new Intent(MainpageActivity.this, ActionActivity.class);

        pointstext = findViewById(R.id.pointstext);
        decaypts = findViewById(R.id.decaypts);
        oppyimage = findViewById(R.id.oppyimage);
        toolbar = findViewById(R.id.toolbar);
        spinner = findViewById(R.id.spinner);
        submittButton = findViewById(R.id.submitButton);
        delButton = findViewById(R.id.delButton);
        selectedPresetName = "";
        setSupportActionBar(toolbar);

        pointstext.setText(String.valueOf(clientController.getUser().getScore()));
        setOppyImage();
        initSpinner();
        setButtonListeners();
        startTimer();
    }

    private void startTimer() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        long milliSecondsUntilMidnight = (c.getTimeInMillis() - System.currentTimeMillis());
        new CountDownTimer(milliSecondsUntilMidnight, 60000) {
            public void onTick(long millisUntilFinished) {
                decaypts.setText(String.format("%s", String.format("%d hrs, %d min",
                        MILLISECONDS.toHours(millisUntilFinished),
                        MILLISECONDS.toMinutes(millisUntilFinished) -
                                HOURS.toMinutes(MILLISECONDS.toHours(millisUntilFinished))
                        ))
                );
            }
            public void onFinish() {
                this.start();
            }
        }.start();
    }

    private void setButtonListeners() {
        submittButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPresetName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Preset select a preset first", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Submitting preset " + selectedPresetName, Toast.LENGTH_SHORT).show();
                    String response = clientController.executePreset(selectedPresetName);
                    if (response.equals("true")) {
                        Toast.makeText(getApplicationContext(), "Great success!", Toast.LENGTH_SHORT).show();
                        update();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong...!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPresetName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Preset select a preset first", Toast.LENGTH_SHORT).show();
                } else {
                    Preset selectedPreset = null;
                    for(Preset p : clientController.getUser().getPresets()){
                        if(p.getName().equals(selectedPresetName)){
                            selectedPreset = p;
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Deleting preset " + selectedPresetName, Toast.LENGTH_SHORT).show();
                    String response = clientController.deletePreset(selectedPreset);
                    if (response.equals("true")) {
                        Toast.makeText(getApplicationContext(), "Great success!", Toast.LENGTH_SHORT).show();
                        update();
                    } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong...!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        update();

    }

    private void update() {
        clientController.updateUser();
        pointstext.setText(String.valueOf(clientController.getUser().getScore()));
        setOppyImage();
        setPresetNames();
        dataAdapter.notifyDataSetChanged();
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


    private void initSpinner() {
        listOfPresetNames = new ArrayList<>();
        setPresetNames();
        dataAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listOfPresetNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(this);

    }

    private void setOppyImage() {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedPresetName = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setPresetNames() {
        List<Preset> listOfPresets = clientController.getUser().getPresets();
        listOfPresetNames.clear();
        for (Preset preset : listOfPresets) {
            listOfPresetNames.add(preset.getName());
        }
    }
}
