package com.kiwi.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import com.kiwi.clientside.ClientController;
import com.kiwi.model.User;


public class LoginActivity extends AppCompatActivity {
    private User user;
    private ClientController clientController;
    /**
     *  You must implement this callback, which fires when the system creates your activity.
     *  Your implementation should initialize the essential components of your activity:
     *  For example, your app should create views and bind data to lists here.
     *  Most importantly, this is where you must call setContentView() to define the
     *  layout for the activity's user interface.
     * When onCreate() finishes, the next callback is always onStart().
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = findViewById(R.id.loginButton);

        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        final Intent intent = new Intent(this, MainpageActivity.class);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView usernameText = findViewById(R.id.usernametext);
                EditText passwordText = findViewById(R.id.passtext);
                TextView statusText = findViewById(R.id.statustext);
                user = new User(usernameText.getText().toString(), passwordText.getText().toString(),
                        "", 0, new Date());
                clientController = new ClientController(user, false);
                String response = clientController.login();
                if (response.equals("true")){
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else {
                    statusText.setText("Login failed.");
                    statusText.setTextColor(Color.parseColor("#FF0000"));
                }

            }
        });
    }

    /**
     * As onCreate() exits, the activity enters the Started state, and the activity becomes visible
     * to the user. This callback contains what amounts to the activity’s final preparations for
     * coming to the foreground and becoming interactive.
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * The system invokes this callback just before the activity starts interacting with the user.
     * At this point, the activity is at the top of the activity stack, and captures all user input.
     * Most of an app’s core functionality is implemented in the onResume() method.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     *  The system calls onPause() when the activity loses focus and enters a Paused state.
     *  This state occurs when, for example, the user taps the Back or Recents button. When the
     *  system calls onPause() for your activity, it technically means your activity is still
     *  partially visible, but most often is an indication that the user is leaving the activity,
     *  and the activity will soon enter the Stopped or Resumed state.
     *
     * An activity in the Paused state may continue to update the UI if the user is expecting the
     * UI to update. Examples of such an activity include one showing a navigation map screen or a
     * media player playing. Even if such activities lose focus, the user expects their UI to
     * continue updating.
     *
     * You should not use onPause() to save application or user data, make network calls, or
     * execute database transactions. For information about saving data, see Saving and
     * restoring activity state.
     *
     * Once onPause() finishes executing, the next callback is either onStop() or onResume(),
     * depending on what happens after the activity enters the Paused state.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }
}
