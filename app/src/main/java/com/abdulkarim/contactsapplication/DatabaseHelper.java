package com.abdulkarim.contactsapplication;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.dailyexpensetrackers.helpers.DateHelper;
import com.example.dailyexpensetrackers.modal.Expanse;
import com.example.dailyexpensetrackers.modal.Income;
import com.example.dailyexpensetrackers.modal.User;
import com.example.dailyexpensetrackers.util.KeyFrame;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {



    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "DailyExpenseTracker.db";

    // User table name
    private static final String TABLE_USER = "user";

    // Add Income table name
    private static final String TABLE_INCOME = "income_list";

    // Add Expanse table name
    private static final String TABLE_EXPANSE = "expanse_list";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // Income table Columns names
    private static final String COLUMN_INCOME_ID = "income_id";
    private static final String COLUMN_INCOME_TYPE = "income_type";
    private static final String COLUMN_INCOME_NAME = "income_name";
    private static final String COLUMN_INCOME_AMOUNT = "income_amount";
    private static final String COLUMN_INCOME_DATE = "income_date";

    // Expanse table Columns names

    private static final String COLUMN_EXPANSE_ID = "expanse_id";
    private static final String COLUMN_EXPANSE_TYPE = "expanse_type";
    private static final String COLUMN_EXPANSE_NAME = "expanse_name";
    private static final String COLUMN_EXPANSE_AMOUNT = "expanse_amount";
    private static final String COLUMN_EXPANSE_DATE = "expanse_date";


    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table sql query
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " ("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ");";

        // create income table
        String CREATE_INCOME_TABLE = "CREATE TABLE " + TABLE_INCOME + " ("
                + COLUMN_INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_INCOME_TYPE + " TEXT,"
                + COLUMN_INCOME_NAME + " TEXT," + COLUMN_INCOME_AMOUNT + " INTEGER," + COLUMN_INCOME_DATE + " TEXT" + ");";

        // create expanse table
        String CREATE_EXPANSE_TABLE = "CREATE TABLE " + TABLE_EXPANSE + " ("
                + COLUMN_EXPANSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_EXPANSE_TYPE + " TEXT,"
                + COLUMN_EXPANSE_NAME + " TEXT," + COLUMN_EXPANSE_AMOUNT + " INTEGER," + COLUMN_EXPANSE_DATE + " TEXT" + ");";
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_INCOME_TABLE);
        db.execSQL(CREATE_EXPANSE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        // drop table sql query
        String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
        db.execSQL(DROP_USER_TABLE);

        // drop income table if exist
        String DROP_INCOME_TABLE = "DROP TABLE IF EXISTS " + TABLE_INCOME;
        db.execSQL(DROP_INCOME_TABLE);
        // drop expanse table if exist
        // drop expanse table
        String DROP_EXPANSE_TABLE = "DROP TABLE IF EXISTS " + TABLE_EXPANSE;
        db.execSQL(DROP_EXPANSE_TABLE);

        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();

    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder = COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // update expense
    public void updateExpense(Expanse expanse) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPANSE_TYPE, expanse.getExpanseType());
        values.put(COLUMN_EXPANSE_NAME, expanse.getName());
        values.put(COLUMN_EXPANSE_AMOUNT, expanse.getAmount());
        values.put(COLUMN_EXPANSE_DATE, expanse.getExpanseDate());

        // updating row
        Log.v("UPDTESSSS",""+expanse.getId());
        db.update(TABLE_EXPANSE, values, COLUMN_EXPANSE_ID + " = ?", new String[]{String.valueOf(expanse.getId())});
        db.close();
    }

    // update income

    public void updateIncome(Income income) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_INCOME_TYPE, income.getIncomeType());
        values.put(COLUMN_INCOME_NAME, income.getIncomeName());
        values.put(COLUMN_INCOME_AMOUNT, income.getIncomeAmount());
        values.put(COLUMN_INCOME_DATE, income.getIncomeDate());

        // updating row
        Log.v("UPDTESSSS",""+income.getId());
        db.update(TABLE_INCOME, values, COLUMN_INCOME_ID + " = ?", new String[]{String.valueOf(income.getId())});
        db.close();
    }
    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        String[] columns = {COLUMN_USER_ID};

        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'admin@gmail.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password,Context context) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,COLUMN_USER_NAME,COLUMN_USER_EMAIL
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            SharedPreferences sharedPreferences = context.getSharedPreferences("login",0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String userName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
            String userEmail = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
            editor.putString(KeyFrame.USER_EMAIL,userEmail);
            editor.putString(KeyFrame.USER_NAME,userName);
            editor.apply();
            cursor.close();
            db.close();
            return true;
        }
        cursor.close();
        db.close();
        return false;
    }


    // add Income into database

    public void insertIncome(Income income){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_INCOME_TYPE, income.getIncomeType());
        contentValues.put(COLUMN_INCOME_NAME, income.getIncomeName());
        contentValues.put(COLUMN_INCOME_AMOUNT, income.getIncomeAmount());
        contentValues.put(COLUMN_INCOME_DATE, income.getIncomeDate());

        database.insert(TABLE_INCOME,null,contentValues);
        database.close();
        Log.d("HELLO","Income is inserted");
    }


    // get all income datas to income Fragment;

    public List<Income> getAllIncome() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_INCOME_ID,
                COLUMN_INCOME_TYPE,
                COLUMN_INCOME_NAME,
                COLUMN_INCOME_AMOUNT,
                COLUMN_INCOME_DATE
        };
        // sorting orders
        String sortOrder = COLUMN_INCOME_TYPE + " ASC";
        List<Income> incomesList = new ArrayList<Income>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_INCOME, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Income income = new Income();
                income.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_ID))));
                income.setIncomeType(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_TYPE)));
                income.setIncomeName(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_NAME)));
                income.setIncomeAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_INCOME_AMOUNT)));
                income.setIncomeDate(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_DATE)));
                // Adding user record to list
                incomesList.add(income);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return expanse list
        return incomesList;
    }

    // get current income data to show monthly income fragment

    public List<Income> getPreviousMonthIncomeDatas(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_INCOME_ID,
                COLUMN_INCOME_TYPE,
                COLUMN_INCOME_NAME,
                COLUMN_INCOME_AMOUNT,
                COLUMN_INCOME_DATE
        };
        // sorting orders
        String sortOrder = COLUMN_INCOME_DATE + " ASC";
        String where = " strftime('%Y',income_date) = strftime('%Y',date('now')) AND  strftime('%m',income_date) = strftime('%m',date('now','-1 month'))";
        List<Income> incomeList = new ArrayList<Income>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INCOME, //Table to query
                columns,    //columns to return
                where,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Income income = new Income();
                DateHelper dateHelper = new DateHelper();

                income.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_ID))));
                income.setIncomeType(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_TYPE)));
                income.setIncomeName(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_NAME)));
                income.setIncomeAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_INCOME_AMOUNT)));
                income.setIncomeDate(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_DATE)));

                // Adding user record to list
                incomeList.add(income);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return expanse list
        return incomeList;
    }

    // get current days data and show in the Today Income Fragment

    public List<Income> getCurrentDayIncomeDatas(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_INCOME_ID,
                COLUMN_INCOME_TYPE,
                COLUMN_INCOME_NAME,
                COLUMN_INCOME_AMOUNT,
                COLUMN_INCOME_DATE
        };
        // sorting orders
        String sortOrder = COLUMN_INCOME_DATE + " ASC";
        String where = COLUMN_INCOME_DATE+ " >= DATE('now')";

        List<Income> incomeList = new ArrayList<Income>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INCOME, //Table to query
                columns,    //columns to return
                where,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Income income = new Income();
                DateHelper dateHelper = new DateHelper();

                income.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_ID))));
                income.setIncomeType(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_TYPE)));
                income.setIncomeName(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_NAME)));
                income.setIncomeAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_INCOME_AMOUNT)));
                income.setIncomeDate(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_DATE)));

                // Adding user record to list
                incomeList.add(income);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return expanse list
        return incomeList;
    }

    // get weekly income data and show in the weekly Income Fragment

    public List<Income> getCurrentWeeklyIncomeDatas(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_INCOME_ID,
                COLUMN_INCOME_TYPE,
                COLUMN_INCOME_NAME,
                COLUMN_INCOME_AMOUNT,
                COLUMN_INCOME_DATE
        };
        // sorting orders
        String sortOrder = COLUMN_INCOME_DATE + " ASC";
        String where = COLUMN_INCOME_DATE+ " >= DATETIME('now','-6 day')";

        List<Income> incomeList = new ArrayList<Income>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INCOME, //Table to query
                columns,    //columns to return
                where,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Income income = new Income();
                DateHelper dateHelper = new DateHelper();

                income.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_ID))));
                income.setIncomeType(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_TYPE)));
                income.setIncomeName(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_NAME)));
                income.setIncomeAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_INCOME_AMOUNT)));
                income.setIncomeDate(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_DATE)));

                // Adding user record to list
                incomeList.add(income);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return expanse list
        return incomeList;
    }

    // get monthly income data and show in the monthly Income Fragment

    public List<Income> getCurrentMonthlyIncomeDatas(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_INCOME_ID,
                COLUMN_INCOME_TYPE,
                COLUMN_INCOME_NAME,
                COLUMN_INCOME_AMOUNT,
                COLUMN_INCOME_DATE
        };
        // sorting orders
        String sortOrder = COLUMN_INCOME_DATE + " ASC";
        String where = " strftime('%Y',income_date) = strftime('%Y',date('now')) AND  strftime('%m',income_date) = strftime('%m',date('now'))";

        List<Income> incomeList = new ArrayList<Income>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INCOME, //Table to query
                columns,    //columns to return
                where,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Income income = new Income();
                DateHelper dateHelper = new DateHelper();

                income.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_ID))));
                income.setIncomeType(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_TYPE)));
                income.setIncomeName(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_NAME)));
                income.setIncomeAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_INCOME_AMOUNT)));
                income.setIncomeDate(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_DATE)));

                // Adding user record to list
                incomeList.add(income);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return expanse list
        return incomeList;
    }

    // get current year income data and show in the Yearly Income Fragment

    public List<Income> getCurrentYearlyIncomeDatas(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_INCOME_ID,
                COLUMN_INCOME_TYPE,
                COLUMN_INCOME_NAME,
                COLUMN_INCOME_AMOUNT,
                COLUMN_INCOME_DATE
        };
        // sorting orders
        String sortOrder = COLUMN_INCOME_DATE + " ASC";
        String where = " strftime('%Y',income_date) = strftime('%Y',date('now'))";

        List<Income> incomeList = new ArrayList<Income>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_INCOME, //Table to query
                columns,    //columns to return
                where,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Income income = new Income();
                DateHelper dateHelper = new DateHelper();

                income.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_ID))));
                income.setIncomeType(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_TYPE)));
                income.setIncomeName(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_NAME)));
                income.setIncomeAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_INCOME_AMOUNT)));
                income.setIncomeDate(cursor.getString(cursor.getColumnIndex(COLUMN_INCOME_DATE)));

                // Adding user record to list
                incomeList.add(income);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return expanse list
        return incomeList;
    }


    // add Expanse into database ;

    public void insertExpanse(Expanse expanse){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues Values = new ContentValues();

        Values.put(COLUMN_EXPANSE_TYPE, expanse.getExpanseType());
        Values.put(COLUMN_EXPANSE_NAME, expanse.getName());
        Values.put(COLUMN_EXPANSE_AMOUNT, expanse.getAmount());
        Values.put(COLUMN_EXPANSE_DATE, expanse.getExpanseDate());

        // Inserting Row
        db.insert(TABLE_EXPANSE,null,Values);
        db.close();
        Log.d("YAHOO!","Data Incerted");

    }

    // fetch all expanse data from sqlite;

    public List<Expanse> getAllExpanse() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_EXPANSE_ID,
                COLUMN_EXPANSE_TYPE,
                COLUMN_EXPANSE_NAME,
                COLUMN_EXPANSE_AMOUNT,
                COLUMN_EXPANSE_DATE
        };
        // sorting orders
        String sortOrder = COLUMN_EXPANSE_TYPE + " ASC";
        List<Expanse> expanseList = new ArrayList<Expanse>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_EXPANSE, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expanse expanse = new Expanse();
                DateHelper dateHelper = new DateHelper();

                expanse.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_ID))));
                expanse.setExpanseType(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_TYPE)));
                expanse.setName(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_NAME)));
                expanse.setAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_EXPANSE_AMOUNT)));
                expanse.setExpanseDate(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_DATE)));

                // Adding user record to list
                expanseList.add(expanse);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return expanse list
        return expanseList;
    }


    // get total expanse
    public int getTotalExpense(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_EXPANSE_AMOUNT + ") as Total FROM " + TABLE_EXPANSE,null);
        if (cursor.moveToFirst()) {
            int total = cursor.getInt(cursor.getColumnIndex("Total"));
            cursor.close();
            return total;
        }
        cursor.close();
        db.close();
        return 0;
    }

    // get current days data from Expanse table and sum all data;

    // get total income
    public int getTotalIncome(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_INCOME_AMOUNT + ") as Total FROM " + TABLE_INCOME, null);
        if (cursor.moveToFirst()) {
            int total = cursor.getInt(cursor.getColumnIndex("Total"));
            cursor.close();
            return total;
        }
        cursor.close();
        db.close();
        return 0;
    }


    // get Current day all list data to show TodayExpenseFragment

    public List<Expanse> getCurrentTodayDatas(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_EXPANSE_ID,
                COLUMN_EXPANSE_TYPE,
                COLUMN_EXPANSE_NAME,
                COLUMN_EXPANSE_AMOUNT,
                COLUMN_EXPANSE_DATE
        };
        // sorting orders
        String sortOrder = COLUMN_EXPANSE_DATE + " ASC";
        String where = COLUMN_EXPANSE_DATE+ " >= DATE('now')";
        List<Expanse> expanseList = new ArrayList<Expanse>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXPANSE, //Table to query
                columns,    //columns to return
                where,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expanse expanse = new Expanse();
                DateHelper dateHelper = new DateHelper();

                expanse.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_ID))));
                expanse.setExpanseType(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_TYPE)));
                expanse.setName(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_NAME)));
                expanse.setAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_EXPANSE_AMOUNT)));
                expanse.setExpanseDate(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_DATE)));

                // Adding user record to list
                expanseList.add(expanse);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return expanse list
        return expanseList;
    }

    // get current week data all list expense to show in WeekExpenseFragment

    public List<Expanse> getCurrentWeekDatas(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_EXPANSE_ID,
                COLUMN_EXPANSE_TYPE,
                COLUMN_EXPANSE_NAME,
                COLUMN_EXPANSE_AMOUNT,
                COLUMN_EXPANSE_DATE
        };
        // sorting orders
        String sortOrder = COLUMN_EXPANSE_DATE + " ASC";
        String where = COLUMN_EXPANSE_DATE+ " >= DATETIME('now','-6 day')";
        List<Expanse> expanseList = new ArrayList<Expanse>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXPANSE, //Table to query
                columns,    //columns to return
                where,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expanse expanse = new Expanse();
                DateHelper dateHelper = new DateHelper();

                expanse.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_ID))));
                expanse.setExpanseType(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_TYPE)));
                expanse.setName(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_NAME)));
                expanse.setAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_EXPANSE_AMOUNT)));
                expanse.setExpanseDate(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_DATE)));

                // Adding user record to list
                expanseList.add(expanse);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return expanse list
        return expanseList;
    }


    // get current month data and show in list in monthlyExpenseFragment

    public List<Expanse> getCurrentMonthDatas(){
        // array of columns to fetch
        String[] columns = {
                COLUMN_EXPANSE_ID,
                COLUMN_EXPANSE_TYPE,
                COLUMN_EXPANSE_NAME,
                COLUMN_EXPANSE_AMOUNT,
                COLUMN_EXPANSE_DATE
        };
        // sorting orders
        String sortOrder = COLUMN_EXPANSE_DATE + " ASC";
        String where = " strftime('%Y',expanse_date) = strftime('%Y',date('now')) AND  strftime('%m',expanse_date) = strftime('%m',date('now'))";
        List<Expanse> expanseList = new ArrayList<Expanse>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXPANSE, //Table to query
                columns,    //columns to return
                where,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expanse expanse = new Expanse();
                DateHelper dateHelper = new DateHelper();

                expanse.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_ID))));
                expanse.setExpanseType(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_TYPE)));
                expanse.setName(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_NAME)));
                expanse.setAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_EXPANSE_AMOUNT)));
                expanse.setExpanseDate(cursor.getString(cursor.getColumnIndex(COLUMN_EXPANSE_DATE)));

                // Adding user record to list
                expanseList.add(expanse);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return expanse list
        return expanseList;
    }

    // delete expense item
    public void deleteExpense(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPANSE, COLUMN_EXPANSE_ID    + "    = ?", new String[] { String.valueOf(id)});
    }





}




