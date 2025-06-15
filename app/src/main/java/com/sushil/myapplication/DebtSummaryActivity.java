package com.sushil.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DebtSummaryActivity extends AppCompatActivity {

    private ListView settlementListView;
    private DatabaseHelper dbHelper;
    private long groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debt_summary);

        settlementListView = findViewById(R.id.settlementListView);
        dbHelper  = new DatabaseHelper(this);
        groupId   = getIntent().getLongExtra("groupId", -1);

        showDebtSummary();
    }


    private void showDebtSummary() {

        HashMap<String, Double> net = dbHelper.calculateNetBalances(groupId);


        List<DebtSimplifier.Transaction> tx = DebtSimplifier.simplifyDebts(net);


        ArrayList<String> rows = new ArrayList<>();
        rows.add("── Net balance per member ──");
        for (String m : net.keySet()) {
            rows.add(m + " : " + String.format("%+.2f", net.get(m)));
        }
        rows.add("── Minimal transactions ──");
        if (tx.isEmpty()) {
            rows.add("All members are settled up!");
        } else {
            for (DebtSimplifier.Transaction t : tx) {
                rows.add(t.from + " pays ₹" +
                        String.format("%.2f", t.amount) +
                        " to " + t.to);
            }
        }

        // 4) Bind to ListView
        settlementListView.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_list_item_1, rows));
    }
}
