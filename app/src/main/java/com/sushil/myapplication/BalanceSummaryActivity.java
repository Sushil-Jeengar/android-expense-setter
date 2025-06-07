package com.sushil.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BalanceSummaryActivity extends AppCompatActivity {

    private ListView expenseListView;
    private TextView emptyView;
    private DatabaseHelper dbHelper;
    private long groupId;

    private ArrayList<ExpenseItem> expenseItems = new ArrayList<>();
    private ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_summary);

        expenseListView = findViewById(R.id.balanceListView);
        emptyView       = findViewById(R.id.balanceEmptyView);

        dbHelper = new DatabaseHelper(this);
        groupId  = getIntent().getLongExtra("groupId", -1);

        loadExpenses();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpenses();
    }

    private void loadExpenses() {
        expenseItems.clear();

        String sql = "SELECT e." + DatabaseHelper.COLUMN_AMOUNT      + ", " +
                "       e." + DatabaseHelper.COLUMN_PAID_BY     + ", " +
                "       e." + DatabaseHelper.COLUMN_DATE        + ", " +
                "       e." + DatabaseHelper.COLUMN_DESCRIPTION + ", " +
                "       g." + DatabaseHelper.COLUMN_GROUP_NAME  + "  AS group_name " +
                "FROM "   + DatabaseHelper.TABLE_EXPENSES + " e " +
                "JOIN `groups` g " +
                "  ON e." + DatabaseHelper.COLUMN_GROUP_ID_FK + " = g." + DatabaseHelper.COLUMN_GROUP_ID +
                " WHERE e." + DatabaseHelper.COLUMN_GROUP_ID_FK + " = ?" +
                " ORDER BY e." + DatabaseHelper.COLUMN_DATE + " DESC";

        Cursor c = dbHelper.getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(groupId)});

        if (c.moveToFirst()) {
            do {
                double amount      = c.getDouble(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT));
                String paidBy      = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PAID_BY));
                String date        = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
                String description = c.getString(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
                String groupName   = c.getString(c.getColumnIndexOrThrow("group_name"));

                expenseItems.add(new ExpenseItem(amount, paidBy, date, description, groupName));
            } while (c.moveToNext());
        }
        c.close();

        if (expenseAdapter == null) {
            expenseAdapter = new ExpenseAdapter(this, expenseItems);
            expenseListView.setAdapter(expenseAdapter);
        } else {
            expenseAdapter.notifyDataSetChanged();
        }

        emptyView.setVisibility(expenseItems.isEmpty() ? View.VISIBLE : View.GONE);
    }

    /** Simple model for one expense row */
    public static class ExpenseItem {
        public final double amount;
        public final String paidBy;
        public final String date;
        public final String description;
        public final String groupName;

        public ExpenseItem(double amount, String paidBy, String date, String description, String groupName) {
            this.amount = amount;
            this.paidBy = paidBy;
            this.date = date;
            this.description = description;
            this.groupName = groupName;
        }
    }
}
