package com.sushil.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {

    private TextView tvMemberName, tvSelectedDate;
    private EditText etDescription, etAmount;
    private Button btnAddExpense;
    private ListView listViewMembers;

    private DatabaseHelper dbHelper;
    private long groupId;
    private String paidByMember;
    private ArrayList<String> groupMembers = new ArrayList<>();
    private ArrayList<String> selectedMembers = new ArrayList<>();

    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        tvMemberName = findViewById(R.id.tvMemberName);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        etDescription = findViewById(R.id.etDescription);
        etAmount = findViewById(R.id.etAmount);
        btnAddExpense = findViewById(R.id.btnAddExpense);
        listViewMembers = findViewById(R.id.listViewMembers);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        groupId = intent.getLongExtra("groupId", -1);
        paidByMember = intent.getStringExtra("memberName");

        // Validate intent data
        if (groupId == -1 || TextUtils.isEmpty(paidByMember)) {
            Toast.makeText(this, "Invalid data passed", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvMemberName.setText("Paid by: " + paidByMember);

        loadGroupMembers();

        tvSelectedDate.setText(dateFormat.format(calendar.getTime()));
        tvSelectedDate.setOnClickListener(v -> showDatePickerDialog());

        listViewMembers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listViewMembers.setOnItemClickListener((parent, view, position, id) -> {
            String member = groupMembers.get(position);
            if (selectedMembers.contains(member)) {
                selectedMembers.remove(member);
            } else {
                selectedMembers.add(member);
            }
        });

        btnAddExpense.setOnClickListener(v -> addExpense());
    }

    private void loadGroupMembers() {
        groupMembers = dbHelper.getMembersByGroup(groupId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_multiple_choice, groupMembers);
        listViewMembers.setAdapter(adapter);

        selectedMembers.clear();  // clear previous selection

        for (int i = 0; i < groupMembers.size(); i++) {
            listViewMembers.setItemChecked(i, true);
            selectedMembers.add(groupMembers.get(i));
        }
    }

    private void showDatePickerDialog() {
        new DatePickerDialog(this,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    tvSelectedDate.setText(dateFormat.format(calendar.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void addExpense() {
        String description = etDescription.getText().toString().trim();
        String amountStr = etAmount.getText().toString().trim();
        String date = tvSelectedDate.getText().toString();

        if (TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please enter description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(amountStr)) {
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
            return;
        }
        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedMembers.isEmpty()) {
            Toast.makeText(this, "Please select at least one member", Toast.LENGTH_SHORT).show();
            return;
        }

        String paidFor = String.join(",", selectedMembers);

        long expenseId = dbHelper.insertExpense(description, amount, paidByMember, date, groupId, paidFor);
        if (expenseId != -1) {
            Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show();

            // Navigate back to GroupDetailsActivity with groupId
            Intent intent = new Intent(AddExpenseActivity.this, GroupDetailsActivity.class);
            intent.putExtra("groupId", groupId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);

            finish();
        } else {
            Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();
        }
    }
}
