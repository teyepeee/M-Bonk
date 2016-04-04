package com.eikontechnology.mbonk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.eikontechnology.mbonk.R;
import com.eikontechnology.mbonk.data.UserDAO;
import com.eikontechnology.mbonk.entity.User;
import com.eikontechnology.mbonk.utils.Constants;
import com.eikontechnology.mbonk.utils.ValidateUserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PotholeActivity extends AppCompatActivity {

    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int GALLERY_UPLOAD_IMAGE_REQUEST_CODE = 200;

    final Context context = this;
    UserDAO userDAO = new UserDAO(context);

    EditText mText_address, mText_description, mId_user;
    Spinner mSpinner_district, mSpinner_condition;
    ImageView mImage_photo;
    RadioGroup mRadioGroup_urgent;
    RadioButton mRadioButton_urgent;
    Button mButton_choose, mButton_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pothole);

        User loggedInUser = null;
        try {
            loggedInUser = userDAO.ListAllUser().get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String list_district[]={"Balongbendo",
                                "Buduran",
                                "Candi",
                                "Gedangan",
                                "Jabon",
                                "Krembung",
                                "Krian",
                                "Porong",
                                "Prambon",
                                "Sedati",
                                "Sidoarjo",
                                "Sukodono",
                                "Taman",
                                "Tanggulangin",
                                "Tarik",
                                "Tulangan",
                                "Waru",
                                "Wonoayu"};

        String list_condition[]={
                "Genangan",
                "Lubang",
                "Bergelombang",
                "Other"
        };

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mText_address = (EditText) findViewById(R.id.edit_address);
        mSpinner_district = (Spinner) findViewById(R.id.spinner_district);
        ArrayAdapter<String> AdapterListDistrict = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,list_district);
        AdapterListDistrict.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpinner_district.setAdapter(AdapterListDistrict);
        mRadioGroup_urgent = (RadioGroup) findViewById(R.id.radio_urgent_group);
        mSpinner_condition = (Spinner) findViewById(R.id.spinner_condition);
        ArrayAdapter<String> AdapterListCondition = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,list_condition);
        AdapterListCondition.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSpinner_condition.setAdapter(AdapterListCondition);
        mImage_photo = (ImageView) findViewById(R.id.imgPreview);
        mButton_choose = (Button) findViewById(R.id.button_photo);
        mButton_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        mText_description = (EditText) findViewById(R.id.edit_description);

        mId_user = (EditText) findViewById(R.id.id_user);
        if (loggedInUser.USR_IS_LOGIN == 1) {
            mId_user.setText(loggedInUser.USR_ID);
        }

        mButton_send = (Button) findViewById(R.id.btn_send);
        mButton_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPothole();
            }
        });

    }

    public boolean onSupportNavigateUp(){
        finish();

        return false;
    }

    private void selectImage() {
        final CharSequence[] items = { "Ambil Foto", "Pilih dari Galeri",
                "Batal" };

        AlertDialog.Builder builder = new AlertDialog.Builder(PotholeActivity.this);
        builder.setTitle("Tambah Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Ambil Foto")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
                } else if (items[item].equals("Pilih dari Galeri")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Pilih File"),
                            GALLERY_UPLOAD_IMAGE_REQUEST_CODE);
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE)
                onCaptureImageResult(data);
            else if (requestCode == GALLERY_UPLOAD_IMAGE_REQUEST_CODE)
                onSelectFromGalleryResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");

        String partFilename = currentDateFormat();
        storeCameraPhotoInSDCard(bitmap, partFilename);

        // display the image from SD Card to ImageView Control
        String storeFilename = "IMG_" + partFilename + ".jpg";
        Bitmap mBitmap = getImageFileFromSDCard(storeFilename);
        mImage_photo.setImageBitmap(mBitmap);

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImageUri = data.getData();
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);
        final int REQUIRED_SIZE = 200;
        int scale = 1;
        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        mImage_photo.setImageBitmap(bm);
    }

    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss", Locale.US);
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME + File.separator
                + "IMG_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getImageFileFromSDCard(String filename){
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME + File.separator
                + filename);
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void checkPothole() {
        // Store values at the time of the login attempt.
        String address = mText_address.getText().toString();
        /*String lat = edit_email.getText().toString();
        String lon = edit_password.getText().toString();*/
        String district = mSpinner_district.getSelectedItem().toString();
        int selected_urgent = mRadioGroup_urgent.getCheckedRadioButtonId();
        mRadioButton_urgent = (RadioButton) findViewById(selected_urgent);
        String condition = mSpinner_condition.getSelectedItem().toString();
        //String photo = edit_dob.getText().toString();
        String description = mText_description.getText().toString();

        String urgent = null;
        if (selected_urgent == R.id.rd_ya) {
            urgent = "1";
        } else if (selected_urgent == R.id.rd_tidak) {
            urgent = "0";
        }

        Log.d("Hasil", address + "," + district + "," + urgent + "," + condition + "," + description);

        boolean cancel = false;
        View focusView = null;

        ValidateUserInfo validate = new ValidateUserInfo();

        if (TextUtils.isEmpty(address)) {
            mText_address.setError(getString(R.string.error_field_required));
            focusView = mText_address;
            cancel = true;
        } else if (TextUtils.isEmpty(description)) {
            mText_description.setError(getString(R.string.error_field_required));
            focusView = mText_description;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //TODO Create account logic
            // Show a progress spinner, and kick off a background task to
            // perform the user registration attempt.
            //showProgress(true);
            //sendPothole();
        }
    }

    private void sendPothole() {
        try {
            //masukin parameter ke JSON
            JSONObject params = new JSONObject();
            params.put("USR_ID", mId_user.getText().toString());
            params.put("PH_ADDRESS", mText_address.getText().toString());
            params.put("PH_DISTRICT", mSpinner_district.getSelectedItem().toString());
            params.put("PH_URGENT", mRadioButton_urgent.getText().toString());
            params.put("PH_CONDITION", mSpinner_condition.getSelectedItem().toString());
            //params.put("PH_PHOTO", edit_ktp.getText().toString());
            params.put("PH_DESCRIPTION", mText_description.getText().toString());
            params.put("PH_STATUS", 1);

            //create JSON Object POSTrequest ke server
            JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, Constants.URL_POTHOLE, params,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("Call Api Result", response.toString());
                            try {
                                String result = response.getString("result");
                                if (result.equals("200")) {
                                    Toast.makeText(context, "Success!", Toast.LENGTH_SHORT).show();
                                    Intent move = new Intent(context, MainActivity.class);
                                    startActivity(move);
                                    finish();
                                } else {
                                    Toast.makeText(context, "Ups, Try again", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            Toast.makeText(context, "Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            //POST ke server
            Volley.newRequestQueue(context).add(postRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
