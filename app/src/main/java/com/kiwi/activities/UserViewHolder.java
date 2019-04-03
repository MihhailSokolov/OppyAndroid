package com.kiwi.activities;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class UserViewHolder extends RecyclerView.ViewHolder {
    TextView usernameTextView;
    TextView scoreTextView;

    UserViewHolder(View itemView){
        super(itemView);
        usernameTextView = itemView.findViewById(R.id.text_list_username);
        scoreTextView = itemView.findViewById(R.id.text_list_points);
    }
}
