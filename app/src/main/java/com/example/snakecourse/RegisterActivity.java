package com.example.snakecourse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    DatabaseHelper db;
    private EditText userName, e1, e2, e3;
    private TextView txtWarName, txtWarEmail, txtWarpass, txtWarcpass;
    private Button b1, b2, b3, b4;
    private ImageView userAvatar;
    private CheckBox checkAgreement;
    private RadioGroup rgGender;
    private RadioButton radioButton;
    private ConstraintLayout parent;
    private Uri image_URI;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private static final int PERMISSION_CODE2 = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db = new DatabaseHelper(this);
        userName = (EditText) findViewById(R.id.userName);
        e1 = (EditText) findViewById(R.id.logEmail);
        e2 = (EditText) findViewById(R.id.logPass);
        e3 = (EditText) findViewById(R.id.cpass);
        txtWarName = (TextView) findViewById(R.id.txtWarName);
        txtWarEmail = (TextView) findViewById(R.id.txtWarEmail);
        txtWarpass = (TextView) findViewById(R.id.txtWarpass);
        txtWarcpass = (TextView) findViewById(R.id.txtWarcpass);
        b1 = (Button) findViewById(R.id.register);
        b2 = (Button) findViewById(R.id.registUpload);
        b3 = (Button) findViewById(R.id.backregbut);
        b4 = (Button) findViewById(R.id.registUpload2);
        userAvatar = (ImageView) findViewById(R.id.userAvatar);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        checkAgreement = (CheckBox) findViewById(R.id.agreementcheckbox);
        parent = (ConstraintLayout) findViewById(R.id.parent);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE2);
                    } else {
                        openCamera();
                    }
                } else openCamera();
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void register() {
        Log.d(TAG, " Regisztráció folyamat");
        String s = userName.getText().toString();
        String s1 = e1.getText().toString();
        String s2 = e2.getText().toString();
        String s3 = e3.getText().toString();
        byte[] kep = imageViewToByte(userAvatar);
        int selectedId = rgGender.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        String Gender = radioButton.getText().toString();

        if (validatedata()) {
            if (checkAgreement.isChecked()) {

                if (s2.equals(s3)) {
                    Boolean checkmail = db.checkmail(s1);
                    if (checkmail == true) {
                        String pwhash = db.MD5(s2);
                        Boolean insert = db.insert(s, s1, pwhash, Gender, kep);
                        if (insert == true) {
                            Toast.makeText(this, getResources().getString(R.string.regsucces), Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.emailexists), Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.boxtick), Toast.LENGTH_SHORT).show();

            }


        }
    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bm = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    public boolean validatedata() {
        Log.d(TAG, "Adatok vizsgálása:");

        String s = userName.getText().toString();
        String s1 = e1.getText().toString();
        String s2 = e2.getText().toString();
        String s3 = e3.getText().toString();

        if (userName.getText().toString().equals("")) {
            Log.d(TAG, "Username test");
            txtWarName.setVisibility(View.VISIBLE);
            txtWarName.setText(getResources().getString(R.string.txtwarname));

            return false;

        }
        if (e1.getText().toString().equals("")) {
            Log.d(TAG, "email test");
            txtWarEmail.setVisibility(View.VISIBLE);
            txtWarEmail.setText(getResources().getString(R.string.txtwaremail));
            return false;
        }

        if (!isValidEmail(s1)) {
            Log.d(TAG, "email pattern test");
            txtWarEmail.setVisibility(View.VISIBLE);
            txtWarEmail.setText(getResources().getString(R.string.wrongemail));
            return false;
        }

        if (e2.getText().toString().equals("")) {
            txtWarpass.setVisibility(View.VISIBLE);
            txtWarpass.setText(getResources().getString(R.string.txtwarpass));
            return false;
        }
        if (e3.getText().toString().equals("")) {
            txtWarcpass.setVisibility(View.VISIBLE);
            txtWarcpass.setText(getResources().getString(R.string.txtwarpass));
            return false;
        }
        if (!s2.equals(s3)) {
            txtWarcpass.setVisibility(View.VISIBLE);
            txtWarcpass.setText(getResources().getString(R.string.txtwarcpass));
            return false;
        } else return true;
    }

    public void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.permissiondenied), Toast.LENGTH_SHORT).show();
                }
            }
            case PERMISSION_CODE2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, getResources().getString(R.string.permissiondenied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            userAvatar.setImageURI(data.getData());
        }
        if (resultCode == RESULT_OK && requestCode == IMAGE_CAPTURE_CODE) {
            userAvatar.setImageURI(image_URI);
        }
    }

    public void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Új kép");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Kamerával készítve");
        image_URI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_URI);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);


    }
}

