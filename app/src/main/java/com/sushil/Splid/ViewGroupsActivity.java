package com.sushil.Splid;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ViewGroupsActivity extends AppCompatActivity {

    private ListView groupListView;
    private DatabaseHelper dbHelper;
    private final ArrayList<String> groupNames = new ArrayList<>();
    private final ArrayList<Long> groupIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_groups);

        Button backButton = findViewById(R.id.backButton);
        groupListView = findViewById(R.id.groupListView);

        dbHelper = new DatabaseHelper(this);

        backButton.setOnClickListener(v -> finish());

        loadGroups();


    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGroups();
    }

    private void loadGroups() {

        Cursor cursor = dbHelper.getReadableDatabase()
                .rawQuery("SELECT id, group_name FROM groups", null);

        groupNames.clear();
        groupIds.clear();

        int index = 1;
        while (cursor.moveToNext()) {
            long groupId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
            String groupName = cursor.getString(cursor.getColumnIndexOrThrow("group_name"));

            groupIds.add(groupId);
            groupNames.add(index + ". " + groupName);
            index++;
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, groupNames
        );
        groupListView.setAdapter(adapter);


        groupListView.setOnItemClickListener((adapterView, view, position, id) -> {
            long selectedGroupId = groupIds.get(position);
            String numberedName = groupNames.get(position);
            String selectedGroupName = numberedName.contains(". ")
                    ? numberedName.substring(numberedName.indexOf(". ") + 2)
                    : numberedName;

            Intent intent = new Intent(this, GroupDetailsActivity.class);
            intent.putExtra("groupId", selectedGroupId);
            intent.putExtra("groupName", selectedGroupName);
            startActivity(intent);
        });


        groupListView.setOnItemLongClickListener((adapterView, view, position, id) -> {
            long selectedGroupId = groupIds.get(position);
            String selectedGroupName = groupNames.get(position);

            new AlertDialog.Builder(this)
                    .setTitle("Delete Group")
                    .setMessage("Are you sure you want to delete the group \"" + selectedGroupName + "\"?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteGroup(selectedGroupId);
                        loadGroups();  // Refresh list after deletion
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });
    }
}
