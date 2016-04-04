package com.eikontechnology.mbonk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

public class GeneralDataAccessObject {
    DatabaseHelper dbHelper;
    SQLiteDatabase database;

    public GeneralDataAccessObject(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        open(false);
    }

    public void open(Boolean isReadOnly) throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void beginTx() {
        database.beginTransaction();
    }

    public void setTxSuccess() {
        database.setTransactionSuccessful();
    }

    public void endTx() {
        database.endTransaction();
    }

}
