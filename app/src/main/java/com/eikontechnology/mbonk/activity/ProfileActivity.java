package com.eikontechnology.mbonk.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eikontechnology.mbonk.R;
import com.eikontechnology.mbonk.data.UserDAO;
import com.eikontechnology.mbonk.entity.User;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

public class ProfileActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    TextView user_email, user_phone, user_city, user_dob, user_ktp, user_gender;
    ImageView user_image;
    FloatingActionButton fab_edit;
    Button btn_usrpth;

    final Context context = this;
    UserDAO userDAO = new UserDAO(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapse);

        user_email = (TextView) findViewById(R.id.user_email);
        user_phone = (TextView) findViewById(R.id.user_phone);
        user_ktp = (TextView) findViewById(R.id.user_ktp);
        user_dob = (TextView) findViewById(R.id.user_dob);
        user_city = (TextView) findViewById(R.id.user_city);
        user_gender = (TextView) findViewById(R.id.user_gender);
        fab_edit = (FloatingActionButton) findViewById(R.id.fab_edit_profile);

        User loggedInUser = null;
        try {
            loggedInUser = userDAO.ListAllUser().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (((int) loggedInUser.USR_IS_LOGIN) == 1) {
            collapsingToolbarLayout.setTitle(loggedInUser.USR_NAME);
            user_email.setText(loggedInUser.USR_EMAIL);
            user_phone.setText(loggedInUser.USR_PHONE);
            user_ktp.setText(loggedInUser.USR_KTP);
            user_dob.setText(loggedInUser.USR_DOB);
            user_city.setText(loggedInUser.USR_CITY);
            user_gender.setText(loggedInUser.USR_GENDER);

            String imageUrl = loggedInUser.USR_PP;
            user_image = (ImageView) findViewById(R.id.user_image);

            Picasso.with(context).load(imageUrl)
                    .placeholder(R.drawable.profile).error(R.drawable.profile)
                    .into(user_image);
        }

        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        btn_usrpth = (Button) findViewById(R.id.btn_view_usrpth);
        btn_usrpth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UserPotholeActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onSupportNavigateUp(){
        finish();

        return false;
    }

}
