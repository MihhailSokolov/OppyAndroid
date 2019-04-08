package com.kiwi.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiwi.activities.R;

public class UserViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView usernameTextView;
    TextView scoreTextView;

    UserViewHolder(View itemView){
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        usernameTextView = itemView.findViewById(R.id.text_list_username);
        scoreTextView = itemView.findViewById(R.id.text_list_points);
    }
}
