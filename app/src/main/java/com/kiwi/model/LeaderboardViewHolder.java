package com.kiwi.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kiwi.activities.R;

public class LeaderboardViewHolder extends RecyclerView.ViewHolder {
    TextView positionTextView;
    TextView usernameTextView;
    TextView scoreTextView;

    LeaderboardViewHolder(View itemView){
        super(itemView);
        positionTextView = itemView.findViewById(R.id.text_list_rank);
        usernameTextView = itemView.findViewById(R.id.text_list_username);
        scoreTextView = itemView.findViewById(R.id.text_list_points);
    }
}
