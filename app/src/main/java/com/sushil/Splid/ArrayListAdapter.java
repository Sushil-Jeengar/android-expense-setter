package com.sushil.Splid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ArrayListAdapter extends ArrayAdapter<String> {
    public ArrayListAdapter(@NonNull Context context, @NonNull List<String> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        String item = getItem(position);
        if (item != null) {
            view.setText(item);
        }
        return view;
    }
}
