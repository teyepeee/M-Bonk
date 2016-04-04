package com.eikontechnology.mbonk.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class MyProgresDialog {

    Context context;
    ProgressDialog progressDialog;

    public MyProgresDialog(Context context) {
        this.context = context;
        progressDialog = new ProgressDialog(context);
    }

    public void showProgress(String message) {
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgress() {
        progressDialog.dismiss();
    }

}

