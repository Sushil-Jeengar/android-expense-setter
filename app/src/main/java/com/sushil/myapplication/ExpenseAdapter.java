package com.sushil.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<BalanceSummaryActivity.ExpenseItem> {

    private final LayoutInflater inflater;

    public ExpenseAdapter(@NonNull Context ctx, @NonNull List<BalanceSummaryActivity.ExpenseItem> items) {
        super(ctx, 0, items);
        inflater = LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.expense_list_item, parent, false);
        }

        BalanceSummaryActivity.ExpenseItem item = getItem(position);
        if (item != null) {
            ((TextView) v.findViewById(R.id.tvPayerName)).setText("Paid by: " + item.paidBy);
            ((TextView) v.findViewById(R.id.tvAmount)).setText("Amount: ₹" + item.amount);
            ((TextView) v.findViewById(R.id.tvDate)).setText("Date: " + item.date);
            ((TextView) v.findViewById(R.id.tvDescription)).setText("Description: " + item.description);
            ((TextView) v.findViewById(R.id.tvGroupName)).setText("Group: " + item.groupName);
        }

        return v;
    }
}
