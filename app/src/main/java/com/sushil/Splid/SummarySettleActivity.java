package com.sushil.Splid;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

public class SummarySettleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_settle);

        long gid = getIntent().getLongExtra("groupId", -1);
        String gName = getIntent().getStringExtra("groupName");
        if (gName != null) setTitle("Balance • " + gName);

        DatabaseHelper db = new DatabaseHelper(this);

        // Get net balance and transaction summary
        HashMap<String, Double> net = DebtSettleUtil.netPerMember(db, gid);
        List<String> settle = DebtSettleUtil.minimalTransactions(net);

        // You must replace static text views in XML with these containers:
        LinearLayout balanceContainer = findViewById(R.id.balanceContainer);
        LinearLayout transactionContainer = findViewById(R.id.transactionContainer);

        // Dynamically add net balances
        for (String member : net.keySet()) {
            double amount = net.get(member);
            TextView tv = new TextView(this);
            tv.setText(member + " : " + String.format("%+.2f", amount));
            tv.setTextColor(amount >= 0 ? Color.parseColor("#10B981") : Color.parseColor("#EF4444"));
            tv.setTextSize(15);
            tv.setPadding(0, 8, 0, 8);
            balanceContainer.addView(tv);
        }

        // Dynamically add minimal transaction summary
        for (String transaction : settle) {
            TextView tv = new TextView(this);
            tv.setText(transaction);
            tv.setTextColor(Color.parseColor("#374151"));
            tv.setTextSize(15);
            tv.setPadding(0, 8, 0, 8);
            transactionContainer.addView(tv);
        }


    }
}
