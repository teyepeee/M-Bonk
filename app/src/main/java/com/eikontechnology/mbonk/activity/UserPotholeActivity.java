package com.eikontechnology.mbonk.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.eikontechnology.mbonk.R;
import com.eikontechnology.mbonk.adapter.PotholeAdapter;
import com.eikontechnology.mbonk.model.ListPothole;
import com.eikontechnology.mbonk.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class UserPotholeActivity extends AppCompatActivity {

    private List<ListPothole> mUserPotholes = new ArrayList<>(30);
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpothole);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_user_pothole);

        setupRecyclerView();

    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(UserPotholeActivity.this));
        mRecyclerView.setHasFixedSize(true);
        initializeCardItemList();
        mRecyclerView.setAdapter(new PotholeAdapter(mUserPotholes));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(UserPotholeActivity.this, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    private void initializeCardItemList() {
        ListPothole listPothole;
        String[] pthTitles = getResources().getStringArray(R.array.pth_titles);
        final int length = pthTitles.length;
        for(int i=0;i<length;i++){
            listPothole = new ListPothole(pthTitles[i]);
            mUserPotholes.add(listPothole);
        }
    }

    public boolean onSupportNavigateUp(){
        finish();

        return false;
    }

}
