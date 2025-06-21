package com.sushil.Splid;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class MemberAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> members;
    private final LayoutInflater inflater;
    private final DatabaseHelper dbHelper;
    private final long groupId;

    public MemberAdapter(Context context, List<String> members, DatabaseHelper dbHelper, long groupId) {
        this.context = context;
        this.members = members;
        this.dbHelper = dbHelper;
        this.groupId = groupId;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int position) {
        return members.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView memberNameTextView;
        Button deleteButton;
        Button addExpenseButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final String member = members.get(position);
        final int pos = position;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.member_list_item, parent, false);
            holder = new ViewHolder();
            holder.memberNameTextView = convertView.findViewById(R.id.memberNameTextView);
            holder.deleteButton = convertView.findViewById(R.id.deleteButton);
            holder.addExpenseButton = convertView.findViewById(R.id.addExpenseButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.memberNameTextView.setText(member);


        holder.deleteButton.setOnClickListener(null);

        holder.deleteButton.setOnClickListener(v -> {
            Log.d("MemberAdapter", "Delete clicked for member: " + member + ", position: " + pos);
            new AlertDialog.Builder(context)
                    .setTitle("Delete Member")
                    .setMessage("Delete " + member + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        int deletedRows = db.delete("members", "member_name = ? AND group_id = ?", new String[]{member, String.valueOf(groupId)});
                        Log.d("MemberAdapter", "Deleted rows: " + deletedRows);
                        if (deletedRows > 0) {
                            members.remove(pos);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Deleted " + member, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete " + member, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });


        holder.addExpenseButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddExpenseActivity.class);
            intent.putExtra("memberName", member);
            intent.putExtra("groupId", groupId);
            context.startActivity(intent);
        });

        return convertView;
    }
}
