package com.sushil.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // New Group Button
        Button newGroupButton = findViewById(R.id.button7);
        newGroupButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
        });

        // About Us Button
        Button aboutUsButton = findViewById(R.id.button6);
        aboutUsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
            startActivity(intent);
        });

        // Groups Button - opens ViewGroupsActivity
        Button groupsButton = findViewById(R.id.button4);
        groupsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewGroupsActivity.class);
            startActivity(intent);
        });
    }
}
