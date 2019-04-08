package com.kiwi.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kiwi.activities.R;


import java.util.List;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Action> list;

    public ListViewAdapter(Context context, List<Action> selectedActions) {
        this.list = selectedActions;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Action getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.action_item_view, parent, false);
        }

        TextView tvName = convertView.findViewById(R.id.action_name_text);
        TextView tvPoints = convertView.findViewById(R.id.action_pts);
        tvName.setText(list.get(position).getActionName());
        tvPoints.setText(String.valueOf(list.get(position).getPoints()));
        return convertView;
    }
}

