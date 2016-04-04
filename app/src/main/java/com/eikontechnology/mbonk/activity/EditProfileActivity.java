package com.eikontechnology.mbonk.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.eikontechnology.mbonk.R;
import com.eikontechnology.mbonk.data.UserDAO;
import com.eikontechnology.mbonk.entity.User;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    EditText useredit_name, useredit_email, useredit_phone, useredit_city;
    Spinner useredit_gender;
    TextView useredit_dob, useredit_ktp;
    CircleImageView useredit_image;
    Button btnedit_save;
    private DatePickerDialog mDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    final Context context = this;
    UserDAO userDAO = new UserDAO(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        useredit_name = (EditText) findViewById(R.id.useredit_name);
        useredit_email = (EditText) findViewById(R.id.useredit_email);
        useredit_phone = (EditText) findViewById(R.id.useredit_phone);
        useredit_ktp = (TextView) findViewById(R.id.useredit_ktp);
        useredit_dob = (TextView) findViewById(R.id.useredit_dob);
        useredit_city = (EditText) findViewById(R.id.useredit_city);
        useredit_gender = (Spinner) findViewById(R.id.useredit_gender);
        btnedit_save = (Button) findViewById(R.id.btnedit_save);

        User loggedInUser = null;
        try {
            loggedInUser = userDAO.ListAllUser().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (((int) loggedInUser.USR_IS_LOGIN) == 1) {
            useredit_name.setText(loggedInUser.USR_NAME);
            useredit_email.setText(loggedInUser.USR_EMAIL);
            useredit_phone.setText(loggedInUser.USR_PHONE);
            useredit_ktp.setText(loggedInUser.USR_KTP);
            useredit_dob.setText(loggedInUser.USR_DOB);
            useredit_city.setText(loggedInUser.USR_CITY);
            //user_gender.setText(loggedInUser.USR_EMAIL);

            String imageUrl = loggedInUser.USR_PP;
            useredit_image = (CircleImageView) findViewById(R.id.useredit_image);

            Picasso.with(context).load(imageUrl)
                    .placeholder(R.drawable.profile).error(R.drawable.profile)
                    .into(useredit_image);
        }


        btnedit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onSupportNavigateUp(){
        finish();

        return false;
    }
}
