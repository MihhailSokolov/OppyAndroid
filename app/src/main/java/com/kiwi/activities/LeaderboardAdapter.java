package com.kiwi.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiwi.model.User;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardViewHolder> {

    List<Integer> positions;
    List<User> list;
    Context context;

    public LeaderboardAdapter(List<Integer> positions, List<User> list, Context context) {
        this.positions = positions;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_item_view, parent, false);
        return new LeaderboardViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder leaderboardViewHolder, int position) {
        leaderboardViewHolder.positionTextView.setText(String.valueOf(positions.get(position)));
        leaderboardViewHolder.usernameTextView.setText(list.get(position).getUsername());
        leaderboardViewHolder.scoreTextView.setText(String.valueOf(list.get(position).getScore()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

