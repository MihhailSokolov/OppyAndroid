package com.kiwi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kiwi.clientside.ClientController;
import com.kiwi.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeMailActivity extends AppCompatActivity {
    private ClientController clientController;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changemail);
        clientController = new ClientController((User)getIntent().getSerializableExtra("user"), true);
        clientController.updateUser();
        user = clientController.getUser();

        final TextView passText = findViewById(R.id.yourPassText);
        final TextView newMailText = findViewById(R.id.newMailText);
        Button changeMailButton = findViewById(R.id.changeMailButton);

        changeMailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String pass = passText.getText().toString();
                String mail = newMailText.getText().toString();
                if (mail.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "New e-mail field cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                final Pattern mailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                Matcher matcher = mailRegex.matcher(mail);
                if (!matcher.find()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
                    return;
                }
                String response = clientController.updateEmail(mail, pass);
                if (response.equals("true")) {
                    Toast.makeText(getApplicationContext(), "E-mail is changed", Toast.LENGTH_LONG).show();
                    Intent settingIntent = new Intent(ChangeMailActivity.this, SettingsActivity.class);
                    user = clientController.getUser();
                    user.setEmail(mail);
                    settingIntent.putExtra("user", user);
                    startActivity(settingIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
