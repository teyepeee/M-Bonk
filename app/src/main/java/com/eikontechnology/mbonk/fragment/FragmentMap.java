package com.eikontechnology.mbonk.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.eikontechnology.mbonk.R;

public class FragmentMap extends Fragment {

    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mWebView = (WebView) rootView.findViewById(R.id.maps);
        initWebView();

        return rootView;

    }

    private void initWebView() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.grey_0));
        mWebView.loadUrl("file:///android_asset/web/index.html");
    }


}