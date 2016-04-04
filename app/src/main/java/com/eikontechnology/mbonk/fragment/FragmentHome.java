package com.eikontechnology.mbonk.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.eikontechnology.mbonk.R;
import com.eikontechnology.mbonk.activity.PotholeActivity;

public class FragmentHome extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        RelativeLayout potholeService = (RelativeLayout) rootView.findViewById(R.id.perbaikan_jalan);
        potholeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PotholeActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

}