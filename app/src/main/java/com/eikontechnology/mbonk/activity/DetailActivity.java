package com.eikontechnology.mbonk.activity;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.eikontechnology.mbonk.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements OnClickListener {

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floatingActionButton;

    @Bind(R.id.judul_detail)
    TextView judul_detail;
    @Bind(R.id.isi_laporan)
    TextView isi_laporan;
    @Bind(R.id.kategori)
    TextView kategori;
    @Bind(R.id.gambar_detail)
    ImageView gambar_detail;

    private WebView mWebView;

    private GoogleMap mMap;
    private LatLng lokasiTujuan;

    LocationManager locManager;
    Location location;
    String provider;
    Criteria criteria = new Criteria(); // Criteria object to get provider

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_direction);
        floatingActionButton.setOnClickListener(this);

        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();

        judul_detail.setText(data.getString("judul"));
        isi_laporan.setText(data.getString("isi_laporan"));
        kategori.setText(data.getString("kategori"));
        Picasso.with(getApplicationContext()).load(data.getString("gambar")).into(gambar_detail);
        lokasiTujuan = new LatLng(data.getDouble("latitude"), data.getDouble("longitude"));

        mWebView = (WebView) findViewById(R.id.bing_maps);
        initWebView();

        /*setUpMapIfNeeded();

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);*/

    }

    @Override
    public void onClick(View view) {
        if (view == floatingActionButton) {
            Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri
                    .parse("http://maps.google.com/maps?daddr=" + lokasiTujuan.latitude + ","
                            + lokasiTujuan.longitude));
            startActivity(i);
        }
    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("file:///android_asset/web/index.html");
    }

    /*private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        provider = locManager.getBestProvider(criteria, true); // Name for best provider
        //Check for permissions if they are granted
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            return;
        }
        location = locManager.getLastKnownLocation(provider); // Get last known location, basically current location
        //Get current long and lat positions
        LatLng currentPos = new LatLng(lokasiTujuan.latitude, lokasiTujuan.longitude);
        //Add a marker on the map with the current position
        mMap.addMarker(new MarkerOptions().position(currentPos).title("Lokasi pelaporan"));

        //Controls the camera so it would zoom into current position
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentPos, 15);
        mMap.animateCamera(cameraUpdate);
    }*/

}
