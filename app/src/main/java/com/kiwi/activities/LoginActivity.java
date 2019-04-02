package com.kiwi.activities;

import android.app.AlertDialog;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = findViewById(R.id.registerButton2);
        final Button registerButton = findViewById(R.id.registerButton1);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Intent mainPageIntent = new Intent(LoginActivity.this, MainpageActivity.class);
        final Intent registerPageIntent = new Intent(LoginActivity.this, RegisterActivity.class);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView usernameText = findViewById(R.id.usernametext);
                EditText passwordText = findViewById(R.id.passtext);
                user = new User(usernameText.getText().toString(), passwordText.getText().toString(),
                        "", 0, new Date());
                clientController = new ClientController(user, false);
                String response = clientController.login();
                if (response.equals("true")){
                    mainPageIntent.putExtra("user", user);
                    startActivity(mainPageIntent);
                } else {
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login error")
                            .setMessage("Can't login. Username or password is incorrect")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(registerPageIntent);
            }
        });
    }
}
