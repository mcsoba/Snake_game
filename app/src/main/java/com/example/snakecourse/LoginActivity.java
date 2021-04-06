package com.example.snakecourse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText e1,e2;
    Button b1,b2;
    DatabaseHelper db;
    private TextView succeslogin;
    public static String veremail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);
        e1 = (EditText)findViewById(R.id.logEmail);
        e2 = (EditText)findViewById(R.id.logPass);
        b1 = (Button)findViewById(R.id.login);
        b2 = (Button)findViewById(R.id.backlogbut);
        succeslogin = (TextView) findViewById(R.id.succeslog);


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                String pwhash  = db.MD5(password);
                Boolean checkmailpass = db.emailpassword(email,pwhash);
                if (checkmailpass == true) {
                    Toast.makeText(getApplicationContext(), getResources().getString((R.string.succeslogin)), Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("email",email);
                    Cursor username = db.getUsername(email);
                    if(username.moveToFirst()) {
                        do {
                            String user = username.getString(0);
                            i.putExtra("username",user);
                        }
                        while (username.moveToNext());
                    }



                    startActivity(i);
                    veremail = email;
                }
                else
                    Toast.makeText(getApplicationContext(), getResources().getString((R.string.wronglogin)), Toast.LENGTH_SHORT).show();
            }


        });



    }

}