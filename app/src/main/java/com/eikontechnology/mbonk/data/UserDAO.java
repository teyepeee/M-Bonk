package com.eikontechnology.mbonk.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.eikontechnology.mbonk.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;

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

public class UserDAO extends GeneralDataAccessObject {

    private static final String TAG = UserDAO.class.getSimpleName();

    public UserDAO(Context context) {
        super(context);
    }

    public void userInsertOrUpdate(User data, boolean isUpdate) throws SQLException {

        ContentValues values = new ContentValues();
        values.put(USR_ID, data.USR_ID);
        values.put(USR_NAME, data.USR_NAME);
        values.put(USR_EMAIL, data.USR_EMAIL);
        values.put(USR_PASSWORD, data.USR_PASSWORD);
        values.put(USR_PHONE, data.USR_PHONE);
        values.put(USR_KTP, data.USR_KTP);
        values.put(USR_DOB, data.USR_DOB);
        values.put(USR_CITY, data.USR_CITY);
        values.put(USR_GENDER, data.USR_GENDER);
        values.put(USR_PP, data.USR_PP);
        values.put(USR_IS_LOGIN, data.USR_IS_LOGIN);

        open();

        if (isUpdate) {
            String whereClause = USR_ID + "=?";
            String[] whereArgs = {"" + data.USR_ID};
            database.update(TBL_USER, values, whereClause, whereArgs);
        }else {
            long id = database.insert(TBL_USER, null, values);
            Log.d(TAG, "New user inserted into sqlite: " + id);
        }

        close();
    }

    public ArrayList<User> ListAllUser() throws SQLException {
        String sql = "SELECT * FROM " + TBL_USER;

        open();
        Cursor cursor = database.rawQuery(sql, null);
        ArrayList<User> result = new ArrayList<User>();
        while (cursor.moveToNext()) {
            User user = new User();
            user.USR_ID = cursor.getString(0);
            user.USR_NAME = cursor.getString(1);
            user.USR_EMAIL = cursor.getString(2);
            user.USR_PASSWORD = cursor.getString(3);
            user.USR_PHONE = cursor.getString(4);
            user.USR_KTP = cursor.getString(5);
            user.USR_DOB = cursor.getString(6);
            user.USR_CITY = cursor.getString(7);
            user.USR_GENDER = cursor.getString(8);
            user.USR_PP = cursor.getString(9);
            user.USR_IS_LOGIN = cursor.getInt(10);
            result.add(user);
        }
        close();

        return result;
    }

    public void clearAllUser() throws Exception {
        open();
        database.execSQL("DELETE FROM TBL_USER");
        close();
    }

}
