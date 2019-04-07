package com.kiwi.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kiwi.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserViewHolder> {

    List<User> list;
    Context context;

    public UserAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_view, parent, false);
        UserViewHolder holder = new UserViewHolder(v);
        return holder;

    }

    public void onBindViewHolder(@NonNull UserViewHolder userViewHolder, int position) {
        if (list.get(position).getProfilePicture() == null || list.get(position).getProfilePicture().isEmpty())
            userViewHolder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.oppy100x100));
        else
            userViewHolder.imageView.setImageBitmap(StringToBitmap(list.get(position).getProfilePicture()));
        userViewHolder.usernameTextView.setText(list.get(position).getUsername());
        userViewHolder.scoreTextView.setText(String.valueOf(list.get(position).getScore()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void insert(int position, User data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    public void remove(User data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
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

