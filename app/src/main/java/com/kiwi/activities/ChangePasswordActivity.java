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

public class ChangePasswordActivity extends AppCompatActivity {
    private ClientController clientController;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        clientController = new ClientController((User)getIntent().getSerializableExtra("user"), true);
        clientController.updateUser();
        user = clientController.getUser();

        final TextView oldPassText = findViewById(R.id.oldPassText);
        final TextView newPassText1 = findViewById(R.id.newPassText1);
        final TextView newPassText2 = findViewById(R.id.newPassText2);
        Button changePassButton = findViewById(R.id.changeButton);

        changePassButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String oldPass = oldPassText.getText().toString();
                String newPass1 = newPassText1.getText().toString();
                String newPass2 = newPassText2.getText().toString();
                if (!newPass1.equals(newPass2)) {
                    Toast.makeText(getApplicationContext(), "New passwords should match", Toast.LENGTH_LONG).show();
                    return;
                }
                if (newPass1.length() < 8) {
                    Toast.makeText(getApplicationContext(), "New password should be at least 8 characters long", Toast.LENGTH_LONG).show();
                    return;
                }
                String response = clientController.updatePass(newPass1, oldPass);
                if (response.equals("true")) {
                    Toast.makeText(getApplicationContext(), "Password is changed", Toast.LENGTH_LONG).show();
                    Intent settingIntent = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
                    user = clientController.getUser();
                    user.setPassword(clientController.hash(newPass1));
                    settingIntent.putExtra("user", user);
                    startActivity(settingIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
