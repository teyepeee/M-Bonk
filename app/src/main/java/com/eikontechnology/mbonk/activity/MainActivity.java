package com.eikontechnology.mbonk.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eikontechnology.mbonk.R;
import com.eikontechnology.mbonk.data.UserDAO;
import com.eikontechnology.mbonk.entity.User;
import com.eikontechnology.mbonk.fragment.FragmentHome;
import com.eikontechnology.mbonk.fragment.FragmentMap;
import com.eikontechnology.mbonk.fragment.FragmentServices;
import com.eikontechnology.mbonk.fragment.FragmentStatus;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends GoogleSignInActivity
        implements GoogleApiClient.OnConnectionFailedListener, NavigationView.OnNavigationItemSelectedListener {

    private GoogleApiClient mGoogleApiClient;
    private String email;
    private String name;
    private TextView txtEmail;
    private TextView txtName;

    SharedPreferences sharedPref;

    final Context context = this;
    UserDAO userDAO = new UserDAO(context);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String email1 = getIntent().getStringExtra("Email");

        User loggedInUser = null;
        try {
            loggedInUser = userDAO.ListAllUser().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        String gpsProvider = LocationManager.GPS_PROVIDER;

        // Prompts user to enable location services if it is not already enabled
        if (!locManager.isProviderEnabled(gpsProvider)) {

            // Alert Dialog
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setMessage("Untuk mengakses aplikasi ini anda harus menyalakan Location Service");
            alertDialog.setCancelable(false);
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,
                    "Buka Location Service", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String locConfig = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                            Intent enableGPS = new Intent(locConfig);
                            startActivity(enableGPS);
                        }
                    });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                    "Batal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent startMain = new Intent(Intent.ACTION_MAIN);
                            startMain.addCategory(Intent.CATEGORY_HOME);
                            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(startMain);
                            finish();
                        }
                    });
            alertDialog.show();
        }

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = FragmentHome.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        }

        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mGoogleApiClient = getGoogleApiClient();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            name = extras.getString("displayName", "");
            email = extras.getString("emailAddress", "");
        }

        // Get components from navigation drawer's header
        View headerView = mNavigationView.getHeaderView(0);
        txtName = (TextView) headerView.findViewById(R.id.name);
        txtEmail = (TextView) headerView.findViewById(R.id.email);
        CircleImageView circleImageView = (CircleImageView) headerView.findViewById(R.id.profile_image);

        LinearLayout linearLayout = (LinearLayout) headerView.findViewById(R.id.profile_nav);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Set email and name on header
        txtEmail.setText(email);
        txtName.setText(name);

        if (((int) loggedInUser.USR_IS_LOGIN) == 1) {
            txtEmail.setText(loggedInUser.USR_EMAIL);
            txtName.setText(loggedInUser.USR_NAME);
            String imageUrl = loggedInUser.USR_PP;

            Picasso.with(context).load(imageUrl)
                    .placeholder(R.drawable.profile).error(R.drawable.profile)
                    .into(circleImageView);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.user_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
            signOutGoogle(mGoogleApiClient);
            startActivity(new Intent(this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;

        switch (id) {
            case R.id.beranda:
                setTitle("M-Bonk");
                fragmentClass = FragmentHome.class;
                break;
            case R.id.layanan:
                setTitle("Layanan");
                fragmentClass = FragmentServices.class;
                break;
            case R.id.peta:
                setTitle("Peta");
                fragmentClass = FragmentMap.class;
                break;
            case R.id.status:
                setTitle("Status");
                fragmentClass = FragmentStatus.class;
                break;
            default:
                fragmentClass = FragmentHome.class;
                break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item and close the drawer
        item.setChecked(true);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
