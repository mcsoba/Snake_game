package com.example.snakecourse;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    Cursor cursor;
    Button b1, b2,b3,b4,b5;
    TextView Greet;
    public static String veremail;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        setContentView(R.layout.activity_main);
        b1 = (Button) findViewById(R.id.mainregbut);
        b2 = (Button) findViewById(R.id.mainlogbut);
        b3 = (Button) findViewById(R.id.playbut);
        b4 = (Button) findViewById(R.id.leaderboard);
        Greet = (TextView) findViewById(R.id.Greet);
        b5 = (Button) findViewById(R.id.profile);



        final Intent i = getIntent();
        String email = i.getStringExtra("email");
        String username = i.getStringExtra("username");

        final TextView hi = (TextView) findViewById(R.id.Greet);
        hi.setText(getResources().getString(R.string.welcome));


        if(username==null) {
            username = getResources().getString(R.string.guest);

        }
        else {
            veremail = email;
        }
        final TextView textViewToChange = (TextView) findViewById(R.id.getUsername);
        textViewToChange.setText(username);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SnakeActivity.class);
                startActivity(i);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LeaderBoardActivity.class);
                startActivity(i);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = i.getStringExtra("username");
                if(username==null){
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.toastnotlogged), Toast.LENGTH_SHORT).show();
                }
                else {
                    String email = i.getStringExtra("email");
                Intent j = new Intent(MainActivity.this, ProfileActivity.class);
                j.putExtra("username",username);
                j.putExtra("email",email);
                startActivity(j);

            }
            }
        });
    }

}