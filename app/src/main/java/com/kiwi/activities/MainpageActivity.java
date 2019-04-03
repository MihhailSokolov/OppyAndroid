package com.kiwi.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiwi.clientside.ClientController;
import com.kiwi.model.User;

public class MainpageActivity extends AppCompatActivity {
    private ClientController clientController;
    private TextView decaytext;
    private TextView pointstext;
    private ImageView oppyimage;
    private Button friendsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        clientController = new ClientController((User)getIntent().getSerializableExtra("user"), true);
        clientController.updateUser();

        pointstext = findViewById(R.id.pointstext);
        decaytext = findViewById(R.id.decaytext);
        oppyimage = findViewById(R.id.oppyimage);

        pointstext.setText(String.valueOf(clientController.getUser().getScore()));
        setOppyImage();

        friendsButton =findViewById(R.id.friendsbutton);
        friendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent friendsIntent = new Intent(MainpageActivity.this, FriendsActivity.class);
                friendsIntent.putExtra("user", clientController.getUser());
                startActivity(friendsIntent);
            }
        });


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
