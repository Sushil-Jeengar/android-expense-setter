package com.sushil.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "expense_tracker.db";
    public static final int DATABASE_VERSION = 2;


    public static final String TABLE_GROUPS = "groups";
    public static final String TABLE_MEMBERS = "members";
    public static final String TABLE_EXPENSES = "expenses";


    public static final String COLUMN_GROUP_ID = "id";
    public static final String COLUMN_GROUP_NAME = "group_name";


    public static final String COLUMN_MEMBER_ID = "id";
    public static final String COLUMN_MEMBER_NAME = "member_name";
    public static final String COLUMN_MEMBER_GROUP_ID = "group_id";


    public static final String COLUMN_EXPENSE_ID = "id";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_PAID_BY = "paid_by";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_GROUP_ID_FK = "group_id";
    public static final String COLUMN_PAID_FOR = "paid_for";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createGroupsTable = "CREATE TABLE `groups` (" +
                COLUMN_GROUP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_GROUP_NAME + " TEXT NOT NULL UNIQUE);";

        String createMembersTable = "CREATE TABLE " + TABLE_MEMBERS + " (" +
                COLUMN_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MEMBER_NAME + " TEXT NOT NULL, " +
                COLUMN_MEMBER_GROUP_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_MEMBER_GROUP_ID + ") REFERENCES `groups`(" + COLUMN_GROUP_ID + ") ON DELETE CASCADE);";

        String createExpensesTable = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                COLUMN_AMOUNT + " REAL NOT NULL, " +
                COLUMN_PAID_BY + " TEXT NOT NULL, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_GROUP_ID_FK + " INTEGER NOT NULL, " +
                COLUMN_PAID_FOR + " TEXT NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_GROUP_ID_FK + ") REFERENCES `groups`(" + COLUMN_GROUP_ID + ") ON DELETE CASCADE);";

        db.execSQL(createGroupsTable);
        db.execSQL(createMembersTable);
        db.execSQL(createExpensesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
        db.execSQL("DROP TABLE IF EXISTS `groups`");
        onCreate(db);
    }


    public long insertGroup(String groupName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_GROUP_NAME, groupName);
        return db.insert("`groups`", null, values);
    }


    public long insertMember(long groupId, String memberName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEMBER_NAME, memberName);
        values.put(COLUMN_MEMBER_GROUP_ID, groupId);
        return db.insert(TABLE_MEMBERS, null, values);
    }


    public long insertExpense(String description, double amount, String paidBy, String date, long groupId, String paidFor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_PAID_BY, paidBy);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_GROUP_ID_FK, groupId);
        values.put(COLUMN_PAID_FOR, paidFor);
        return db.insert(TABLE_EXPENSES, null, values);
    }


    public void deleteGroup(long groupId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("`groups`", COLUMN_GROUP_ID + "=?", new String[]{String.valueOf(groupId)});
    }


    public ArrayList<String> getMembersByGroup(long groupId) {
        ArrayList<String> members = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_MEMBER_NAME + " FROM " + TABLE_MEMBERS +
                        " WHERE " + COLUMN_MEMBER_GROUP_ID + " = ?", new String[]{String.valueOf(groupId)});

        if (cursor.moveToFirst()) {
            do {
                String memberName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEMBER_NAME));
                members.add(memberName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return members;
    }


    public Cursor getAllGroups() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM `groups`", null);
    }


    public Cursor getExpensesByGroup(long groupId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_EXPENSES + " WHERE " + COLUMN_GROUP_ID_FK + " = ?",
                new String[]{String.valueOf(groupId)});
    }


    public HashMap<String, Double> calculateNetBalances(long groupId) {
        HashMap<String, Double> balances = new HashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PAID_BY + ", " + COLUMN_PAID_FOR + ", " + COLUMN_AMOUNT +
                " FROM " + TABLE_EXPENSES +
                " WHERE " + COLUMN_GROUP_ID_FK + " = ?", new String[]{String.valueOf(groupId)});

        if (cursor.moveToFirst()) {
            do {
                String paidBy = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAID_BY));
                String paidForRaw = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PAID_FOR));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT));

                String[] paidForList = paidForRaw.split(",");
                double share = amount / paidForList.length;


                balances.put(paidBy, balances.getOrDefault(paidBy, 0.0) + amount);


                for (String member : paidForList) {
                    member = member.trim();
                    balances.put(member, balances.getOrDefault(member, 0.0) - share);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return balances;
    }
}
