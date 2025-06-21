package com.sushil.Splid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private EditText etGroupName, etMemberName;
    private ArrayList<String> memberList;
    private ArrayAdapter<String> adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageButton backButton = findViewById(R.id.imageButton);
        etGroupName = findViewById(R.id.etGroupName);
        etMemberName = findViewById(R.id.etMemberName);
        Button btnAddMember = findViewById(R.id.btnAddMember);
        Button btnViewGroups = findViewById(R.id.btnViewGroups);
        Button btnCreateGroup = findViewById(R.id.btnCreateGroup);
        ListView memberListView = findViewById(R.id.memberListView);

        dbHelper = new DatabaseHelper(this);
        memberList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, memberList);
        memberListView.setAdapter(adapter);

        backButton.setOnClickListener(v -> onBackPressed());

        btnAddMember.setOnClickListener(v -> {
            String name = etMemberName.getText().toString().trim();

            if (!name.isEmpty()) {
                memberList.add(name);
                adapter.notifyDataSetChanged();
                etMemberName.setText("");
                Toast.makeText(this, "Member added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Enter member name", Toast.LENGTH_SHORT).show();
            }
        });

        btnCreateGroup.setOnClickListener(v -> {
            String groupName = etGroupName.getText().toString().trim();

            if (groupName.isEmpty()) {
                Toast.makeText(this, "Enter group name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (memberList.isEmpty()) {
                Toast.makeText(this, "Add at least one member", Toast.LENGTH_SHORT).show();
                return;
            }

            long groupId = dbHelper.insertGroup(groupName);

            for (String member : memberList) {
                dbHelper.insertMember(groupId, member);
            }

            Toast.makeText(this, "Group created!", Toast.LENGTH_SHORT).show();
            etGroupName.setText("");
            memberList.clear();
            adapter.notifyDataSetChanged();
        });

        btnViewGroups.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity2.this, ViewGroupsActivity.class);
            startActivity(intent);
        });
    }
}
