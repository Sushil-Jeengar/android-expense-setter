package com.sushil.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BalanceSummaryActivity extends AppCompatActivity {

    private ListView balanceListView;
    private TextView balanceEmptyView;
    private DatabaseHelper dbHelper;
    private long groupId;
    private ArrayList<String> balanceSummaryList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_summary);

        balanceListView = findViewById(R.id.balanceListView);
        balanceEmptyView = findViewById(R.id.balanceEmptyView);
        dbHelper = new DatabaseHelper(this);

        groupId = getIntent().getLongExtra("groupId", -1);

        loadBalanceSummary();
    }

    private void loadBalanceSummary() {
        balanceSummaryList.clear();

        String query = "SELECT paid_by, SUM(amount) FROM expenses WHERE group_id = ? GROUP BY paid_by";
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, new String[]{String.valueOf(groupId)});

        if (cursor.moveToFirst()) {
            do {
                String paidBy = cursor.getString(0);
                double sum = cursor.getDouble(1);
                balanceSummaryList.add(paidBy + ": ₹ " + String.format("%.2f", sum));
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (balanceSummaryList.isEmpty()) {
            balanceEmptyView.setText("No expenses found.");
            balanceEmptyView.setVisibility(TextView.VISIBLE);
            balanceListView.setVisibility(ListView.GONE);
        } else {
            balanceEmptyView.setVisibility(TextView.GONE);
            balanceListView.setVisibility(ListView.VISIBLE);
            ArrayListAdapter adapter = new ArrayListAdapter(this, balanceSummaryList);
            balanceListView.setAdapter(adapter);
        }
    }
}
