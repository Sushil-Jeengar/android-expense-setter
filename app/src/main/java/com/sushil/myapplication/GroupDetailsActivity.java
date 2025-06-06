package com.sushil.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GroupDetailsActivity extends AppCompatActivity {

    TextView groupNameTextView;
    ListView memberListView;
    Button btnViewBalances;

    DatabaseHelper dbHelper;
    long groupId;
    String groupName;
    ArrayList<String> memberNames = new ArrayList<>();
    MemberAdapter memberAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        groupNameTextView = findViewById(R.id.groupNameTextView);
        memberListView = findViewById(R.id.memberListView);
        btnViewBalances = findViewById(R.id.btnViewBalances);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        groupId = intent.getLongExtra("groupId", -1);
        groupName = intent.getStringExtra("groupName");

        groupNameTextView.setText("Group: " + groupName);

        loadMembers();

        btnViewBalances.setOnClickListener(v -> {
            Intent intent1 = new Intent(GroupDetailsActivity.this, BalanceSummaryActivity.class);
            intent1.putExtra("groupId", groupId);
            startActivity(intent1);
        });
    }

    private void loadMembers() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT member_name FROM members WHERE group_id = ?",
                new String[]{String.valueOf(groupId)}
        );

        memberNames.clear();

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                memberNames.add(name);
            } while (cursor.moveToNext());
        }

        cursor.close();

        if (memberAdapter == null) {
            memberAdapter = new MemberAdapter(this, memberNames, dbHelper, groupId);
            memberListView.setAdapter(memberAdapter);
        } else {
            memberAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh member list every time the activity resumes
        loadMembers();
    }
}
