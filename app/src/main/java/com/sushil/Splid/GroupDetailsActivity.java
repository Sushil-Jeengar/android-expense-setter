package com.sushil.Splid;

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

    private TextView groupNameTextView;
    private ListView memberListView;
    private Button btnViewBalances;

    private DatabaseHelper dbHelper;
    private long groupId;
    private String groupName;
    private final ArrayList<String> memberNames = new ArrayList<>();
    private MemberAdapter memberAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        // UI references
        groupNameTextView = findViewById(R.id.groupNameTextView);
        memberListView = findViewById(R.id.memberListView);
        btnViewBalances = findViewById(R.id.btnViewBalances);

        // DB setup
        dbHelper = new DatabaseHelper(this);

        // Get group data from intent
        Intent intent = getIntent();
        groupId = intent.getLongExtra("groupId", -1);
        groupName = intent.getStringExtra("groupName");

        groupNameTextView.setText("Group: " + groupName);

        loadMembers();

        btnViewBalances.setOnClickListener(v -> {
            Intent intent1 = new Intent(GroupDetailsActivity.this, BalanceSummaryActivity.class);
            intent1.putExtra("groupId", groupId);
            intent1.putExtra("groupName", groupName);
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
                memberNames.add(cursor.getString(0));
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
        loadMembers(); // Refresh list when returning to this activity
    }
}
