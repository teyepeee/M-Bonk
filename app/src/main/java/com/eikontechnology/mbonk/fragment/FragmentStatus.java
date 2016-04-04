package com.eikontechnology.mbonk.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eikontechnology.mbonk.R;
import com.eikontechnology.mbonk.adapter.PotholeAdapter;
import com.eikontechnology.mbonk.model.ListPothole;
import com.eikontechnology.mbonk.utils.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class FragmentStatus extends Fragment {

    private List<ListPothole> mUserPotholes = new ArrayList<>(30);
    RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_status, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_status);

        setupRecyclerView();

        return rootView;
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        initializeCardItemList();
        mRecyclerView.setAdapter(new PotholeAdapter(mUserPotholes));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
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

}