package com.kiwi.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.CompoundButton;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        clientController = new ClientController((User)getIntent().getSerializableExtra("user"), true);
        clientController.updateUser();
        user = clientController.getUser();

        ImageView profilePic = findViewById(R.id.profilePic);
        TextView usernameText = findViewById(R.id.textUsername);
        TextView mailText = findViewById(R.id.textEmail);
        Switch anonSwitch = findViewById(R.id.anonSwitch);
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
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent,"Select picture"), 42);
                setResult(Activity.RESULT_OK);
            }
        });
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
