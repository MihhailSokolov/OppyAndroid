package com.kiwi.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kiwi.clientside.ClientController;
import com.kiwi.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {
    private ClientController clientController;
    private User user;
    private Toolbar toolbar;
    private Intent friendsIntent;
    private Intent mainPageIntent;
    private Intent leaderboardIntent;
    private Intent actionIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        clientController = new ClientController((User)getIntent().getSerializableExtra("user"), true);
        clientController.updateUser();
        user = clientController.getUser();

        friendsIntent = new Intent(SettingsActivity.this, FriendsActivity.class);
        mainPageIntent = new Intent(SettingsActivity.this, MainpageActivity.class);
        leaderboardIntent = new Intent(SettingsActivity.this, LeaderboardActivity.class);
        actionIntent = new Intent(SettingsActivity.this, ActionActivity.class);

        Button resetPointsButton = findViewById(R.id.resetPointsButton);
        Button deleteAccountButton = findViewById(R.id.deleteAccButton);
        Button changeEmailButton = findViewById(R.id.changeEmailButton);
        Button changePassButton = findViewById(R.id.changePassButton);
        Button logOutButton = findViewById(R.id.logOutButton);
        ImageView profilePic = findViewById(R.id.profilePic);
        TextView usernameText = findViewById(R.id.textUsername);
        TextView mailText = findViewById(R.id.textEmail);
        Switch anonSwitch = findViewById(R.id.anonSwitch);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (user.getProfilePicture() == null || user.getProfilePicture().isEmpty())
            profilePic.setImageDrawable(getResources().getDrawable(R.drawable.oppy100x100));
        else
            profilePic.setImageBitmap(StringToBitmap(user.getProfilePicture()));
        usernameText.setText(user.getUsername());
        mailText.setText(user.getEmail());
        anonSwitch.setChecked(user.getAnonymous());

        anonSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String response = clientController.updateAnonymous(isChecked);
                String status = isChecked ? "anonymous" : "not anonymous";
                if (response.equals("true"))
                    Toast.makeText(getApplicationContext(), "Successfully changed your status to " + status, Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Could not change your status", Toast.LENGTH_LONG).show();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent,"Select picture"), 42);
                setResult(Activity.RESULT_OK);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent loginIntent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        changePassButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent changePassIntent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                changePassIntent.putExtra("user", user);
                startActivity(changePassIntent);
            }
        });

        changeEmailButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent changeMailIntent = new Intent(SettingsActivity.this, ChangeMailActivity.class);
                changeMailIntent.putExtra("user", user);
                startActivity(changeMailIntent);
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Enter your password");
                final EditText input = new EditText(SettingsActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton("Delete account", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String pass = input.getText().toString();
                        if (pass.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Password field cannot be empty", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String response = clientController.deleteAccount(pass);
                        if (response.equals("true")) {
                            Toast.makeText(getApplicationContext(), "Deleted account", Toast.LENGTH_LONG).show();
                            Intent registerIntent = new Intent(SettingsActivity.this, RegisterActivity.class);
                            startActivity(registerIntent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        resetPointsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Enter your password");
                final EditText input = new EditText(SettingsActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);
                builder.setPositiveButton("Reset points", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String pass = input.getText().toString();
                        if (pass.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Password field cannot be empty", Toast.LENGTH_LONG).show();
                            return;
                        }
                        String response = clientController.reset(pass);
                        if (response.equals("true")) {
                            Toast.makeText(getApplicationContext(), "Points have been reset", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
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
            case R.id.action_mainpage:
                mainPageIntent.putExtra("user", clientController.getUser());
                startActivity(mainPageIntent);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            Bitmap newProfilePic;
            try {
                newProfilePic = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not get picture file", Toast.LENGTH_LONG).show();
                return;
            }
            newProfilePic = Bitmap.createScaledBitmap(newProfilePic, 100, 100, true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            newProfilePic.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream .toByteArray();
            String encodedPic = Base64.encodeToString(byteArray, Base64.DEFAULT);
            clientController = new ClientController(user, true);
            String response = clientController.updateProfilePic(encodedPic);
            if (response.equals("true")) {
                ImageView profilePic = findViewById(R.id.profilePic);
                profilePic.setImageBitmap(newProfilePic);
                Toast.makeText(getApplicationContext(), "Successfully changed profile picture", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Could not change profile picture", Toast.LENGTH_LONG).show();
            }
        }
    }

    private Bitmap StringToBitmap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
