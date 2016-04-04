package com.eikontechnology.mbonk.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.eikontechnology.mbonk.helper.SQLiteHandler;

import static com.eikontechnology.mbonk.data.DBConstants.TBL_USER;
import static com.eikontechnology.mbonk.data.DBConstants.USR_CITY;
import static com.eikontechnology.mbonk.data.DBConstants.USR_DOB;
import static com.eikontechnology.mbonk.data.DBConstants.USR_EMAIL;
import static com.eikontechnology.mbonk.data.DBConstants.USR_GENDER;
import static com.eikontechnology.mbonk.data.DBConstants.USR_ID;
import static com.eikontechnology.mbonk.data.DBConstants.USR_IS_LOGIN;
import static com.eikontechnology.mbonk.data.DBConstants.USR_KTP;
import static com.eikontechnology.mbonk.data.DBConstants.USR_NAME;
import static com.eikontechnology.mbonk.data.DBConstants.USR_PASSWORD;
import static com.eikontechnology.mbonk.data.DBConstants.USR_PHONE;
import static com.eikontechnology.mbonk.data.DBConstants.USR_PP;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    public DatabaseHelper(Context context) {
        super(context, "mbonk.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TBL_USER + "("
                + USR_ID + " VARCHAR PRIMARY KEY,"
                + USR_NAME + " VARCHAR,"
                + USR_EMAIL + " VARCHAR,"
                + USR_PASSWORD + " VARCHAR,"
                + USR_PHONE + " VARCHAR,"
                + USR_KTP + " VARCHAR,"
                + USR_DOB + " VARCHAR,"
                + USR_CITY + " VARCHAR,"
                + USR_GENDER + " VARCHAR,"
                + USR_PP + " VARCHAR,"
                + USR_IS_LOGIN + " INTEGER"
                + ");");

        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TBL_USER);

        // Create tables again
        onCreate(sqLiteDatabase);
    }
}
