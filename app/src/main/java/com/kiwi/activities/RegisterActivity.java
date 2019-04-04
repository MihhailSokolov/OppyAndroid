package com.kiwi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kiwi.clientside.ClientController;
import com.kiwi.model.User;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private ClientController clientController;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Pattern mailRegex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
                Pattern.CASE_INSENSITIVE);

        final Button registerButton = findViewById(R.id.registerButton2);
        final Button backButton = findViewById(R.id.backButton1);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Intent loginPageIntent = new Intent(RegisterActivity.this, LoginActivity.class);


        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(loginPageIntent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TextView usernameText = findViewById(R.id.usernametext);
                TextView mailText = findViewById(R.id.mailtext);
                EditText passwordText = findViewById(R.id.passtext);
                EditText passwordText2 = findViewById(R.id.passtext2);

                String username = usernameText.getText().toString();
                String mail = mailText.getText().toString();
                String pass1 = passwordText.getText().toString();
                String pass2 = passwordText2.getText().toString();

                if (username.isEmpty() || mail.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.unfilled_fields),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                Matcher matcher = mailRegex.matcher(mail);
                if (!matcher.find()) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.invalid_email), Toast.LENGTH_LONG).show();
                    return;
                }

                if (!pass1.equals(pass2) || pass1.length() < 8) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.invalid_register_pass),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                user = new User(username, pass1,
                        mail, 0, new Date());
                clientController = new ClientController(user, false);
                String response = clientController.register();
                if (response.equals("true")){
                    Toast.makeText(getApplicationContext(), "Registered successfully", Toast.LENGTH_LONG).show();
                    loginPageIntent.putExtra("user", user);
                    startActivity(loginPageIntent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.invalid_register_username_email),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
