package com.example.snakecourse;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.SQLOutput;

public class ProfileActivity extends AppCompatActivity {
    DatabaseHelper db;
    Cursor cursor;
    Button usernameditbutton, backbutton, donebutton, logoutbutton, passwordeditbtn,passwordeditdone,editavatarbtn,editavatardone;
    ImageView avatar,avatarnew;
    Bitmap image;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_profile);
        final Intent i = getIntent();
        final String email = i.getStringExtra("email");
        String username = i.getStringExtra("username");
        TextView userdisplay = (TextView) findViewById(R.id.usernamedisplay);
        userdisplay.setText(username);
        TextView emaildisplay = (TextView) findViewById(R.id.emaildisplay);
        emaildisplay.setText(email);
        image = getImage();
        avatar = (ImageView) findViewById(R.id.profileavatar);
        avatar.setImageBitmap(image);
        usernameditbutton = (Button) findViewById(R.id.editusername);
        backbutton = (Button) findViewById(R.id.backbut);
        donebutton = (Button) findViewById(R.id.editusernamedone);
        logoutbutton = (Button) findViewById(R.id.logout);
        passwordeditbtn = (Button) findViewById(R.id.changepassbtn) ;
        passwordeditdone = (Button) findViewById(R.id.editpassworddone);
        editavatarbtn = (Button) findViewById(R.id.editavarbtn);
        editavatardone = (Button) findViewById(R.id.editavatardone);
        donebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = i.getStringExtra("email");
                editName(email);
            }
        });
        usernameditbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText useredit = (EditText) findViewById(R.id.usernameedittext);
                TextView userdisplay = (TextView) findViewById(R.id.usernamedisplay);
                usernameditbutton.setVisibility(View.GONE);
                donebutton.setVisibility(View.VISIBLE);
                userdisplay.setVisibility(View.GONE);
                useredit.setVisibility(View.VISIBLE);

    }
});
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = i.getStringExtra("email");
                String username = getuser(email);
                Intent j = new Intent(ProfileActivity.this, MainActivity.class);
                j.putExtra("username",username);
                j.putExtra("email",email);
                startActivity(j);
            }
        });
        logoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logout = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(logout);
                finish();
                Toast.makeText(ProfileActivity.this,getResources().getString(R.string.toastloggedout), Toast.LENGTH_SHORT).show();
            }
        });
        passwordeditbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView editpasswordtxt = (TextView) findViewById(R.id.changepasswordtext);
                EditText editpassword = (EditText) findViewById(R.id.editpassword);
                EditText confirmpassword = (EditText) findViewById(R.id.confirmpassword);
                editpasswordtxt.setVisibility(View.GONE);
                editpassword.setVisibility(View.VISIBLE);
                confirmpassword.setVisibility(View.VISIBLE);
                passwordeditbtn.setVisibility(View.GONE);
                passwordeditdone.setVisibility(View.VISIBLE);
            }
        });
        passwordeditdone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = i.getStringExtra("email");
                TextView editpasswordtxt = (TextView) findViewById(R.id.changepasswordtext);
                EditText editpassword = (EditText) findViewById(R.id.editpassword);
                EditText confirmpassword = (EditText) findViewById(R.id.confirmpassword);
                String password = editpassword.getText().toString();
                String newpassword = confirmpassword.getText().toString();
                changePassword(password,newpassword,email);
                editpasswordtxt.setVisibility(View.VISIBLE);
                editpassword.setVisibility(View.GONE);
                confirmpassword.setVisibility(View.GONE);
                passwordeditdone.setVisibility(View.GONE);
                passwordeditbtn.setVisibility(View.VISIBLE);

            }
        });

        editavatarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editavatarbtn.setVisibility(View.GONE);
                editavatardone.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery(email);
                    }
                } else {
                    pickImageFromGallery(email);
                }
            }

        });
        editavatardone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = i.getStringExtra("email");
                ImageView avatardone = (ImageView) findViewById(R.id.profileavatar);
                byte[] kep = imageViewToByte(avatardone);
                db.updateAvatar(kep,email);
                editavatarbtn.setVisibility(View.VISIBLE);
                editavatardone.setVisibility(View.GONE);

            }
        });
    }
    public void editName(String email) {
        TextView userdisplay = (TextView) findViewById(R.id.usernamedisplay);
        EditText useredit = (EditText) findViewById(R.id.usernameedittext);
        if(useredit.getText().toString()==null||useredit.getText().toString().equalsIgnoreCase("")) {
            Toast.makeText(ProfileActivity.this,getResources().getString(R.string.emptyedituser), Toast.LENGTH_SHORT).show();
        }
        else {
            String newuser = useredit.getText().toString();
            db.updateName(newuser, email);
            String username = getuser(email);
            userdisplay.setText(username);
            useredit.setVisibility(View.GONE);
            userdisplay.setVisibility(View.VISIBLE);
            usernameditbutton.setVisibility(View.VISIBLE);
            donebutton.setVisibility(View.GONE);

        }
    }
    public String getuser(String email){
        Intent i = getIntent();
        String username = i.getStringExtra("username");
        Cursor usern = db.getUsername(email);
        if(usern.moveToFirst()) {
            do {
                String user = usern.getString(0);
                username = user;
            }
            while (usern.moveToNext());
        }
    return username;
    }
    public void changePassword(String password, String confirmpassword, String email) {
        if (password.equals(confirmpassword)) {
                String pwhash = db.MD5(confirmpassword);
              Boolean success = db.updatePassword(pwhash, email);
                if (success == true) {
                    Toast.makeText(this, getResources().getString(R.string.successpasschange), Toast.LENGTH_SHORT).show();
                }

        }
    }
    public Bitmap getImage() {
        Intent i = getIntent();
        String email = i.getStringExtra("email");
        Cursor c = db.getAvatar(email);
        c.moveToFirst();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(c.getBlob(0));
        Bitmap b = BitmapFactory.decodeStream(inputStream);
        return b;
    }

    public void pickImageFromGallery(String email) {
        System.out.println(email);
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
        avatarnew = (ImageView) findViewById(R.id.profileavatar);
        byte[] kep = imageViewToByte(avatarnew);
        db.updateAvatar(kep,email);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent();
                    String email = i.getStringExtra("email");
                    pickImageFromGallery(email);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.permissiondenied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            avatar.setImageURI(data.getData());
        }
    }
    private byte[] imageViewToByte(ImageView image) {
        Bitmap bm = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
}
