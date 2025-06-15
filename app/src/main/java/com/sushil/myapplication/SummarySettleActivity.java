package com.sushil.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SummarySettleActivity extends AppCompatActivity {

    @Override protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        setContentView(R.layout.activity_summary_settle);

        long   gid   = getIntent().getLongExtra("groupId", -1);
        String gName = getIntent().getStringExtra("groupName");
        if (gName != null) setTitle("Balance • "+gName);

        DatabaseHelper db = new DatabaseHelper(this);


        HashMap<String,Double> net = DebtSettleUtil.netPerMember(db, gid);


        List<String> settle = DebtSettleUtil.minimalTransactions(net);


        List<String> rows = new ArrayList<>();
        rows.add("── Net balance per member ──");
        for (String m : net.keySet())
            rows.add(m+" : "+String.format("%+.2f", net.get(m)));
        rows.add("── Minimal transactions ──");
        rows.addAll(settle);


        ListView lv = findViewById(R.id.summarySettleListView);
        lv.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, rows));
    }
}
