package com.kiwi.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kiwi.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    List<User> userList;

    public UserAdapter(List<User> users) {
        this.userList = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_friends,
                viewGroup, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int i) {
        userViewHolder.textViewUsername.setText(userList.get(i).getUsername());
        userViewHolder.textViewPoints.setText(String.valueOf(userList.get(i).getScore()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUsername;
        public TextView textViewPoints;

        public UserViewHolder(View v) {
            super(v);
            textViewUsername = v.findViewById(R.id.text_list_username);
            textViewPoints = v.findViewById(R.id.text_list_points);
        }
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.list_item_view;
    }



}
