package com.example.snakecourse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLClientInfoException;

import static android.content.ContentValues.TAG;
import static android.provider.Contacts.SettingsColumns.KEY;


public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(Context context) {
        super(context,"Login.db" , null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(username , email text primary key,password,gender,avatar BLOB)");
        db.execSQL("Create table leaderboard(username text ,score int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
db.execSQL("drop table if exists user");
db.execSQL("drop table if exists leaderboard");
onCreate(db);
    }
    public boolean insert(String username, String email,String password, String gender,byte[] avatar){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("email",email);
        contentValues.put("password",password);
        contentValues.put("gender",gender);
        contentValues.put("avatar", avatar);
        long ins = db.insert("user",null,contentValues);
        if (ins==-1) return false;
        else return true;
    }
    public boolean insertScore(int score,String user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor username = getUsername(user);
        if(user.equalsIgnoreCase("Vendég")||user.equalsIgnoreCase("Guest")){
            contentValues.put("username",user);
            contentValues.put("score",score);
            System.out.println(user);
        }
       else if(username.moveToFirst()) {
            do {
                String usern = username.getString(0);
                contentValues.put("username",usern);
            }
            while (username.moveToNext());
            contentValues.put("score",score);
       }

        long ins = db.insert("leaderboard",null,contentValues);
        if (ins == -1 ) return false;
        else return true;
    }
    public boolean checkmail(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from user where email =?",new String[]{email});
        if (cursor.getCount()>0) return false;
        else return true;
    }
    public Boolean emailpassword(String email,String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user where email =? and password =?",new String[]{email,password});
        if (cursor.getCount()>0)return true;
        else return false;
    }
    public Cursor getUsername(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select username from user where email =?" , new String[]{email});
                if(cursor.getCount()==0){
                    return null;
                }
                return cursor;
    }
    public Cursor getAvatar(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select avatar from user where email =?" , new String[]{email});
        if(cursor.getCount()==0){
            return null;
        }
        return cursor;
    }
    public void updateName(String newName, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",newName);
        db.update("user",contentValues,"email=\'"+email+"\'",null);
    }

    public Boolean updatePassword(String newPassword, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password",newPassword);
        long update = db.update("user",contentValues,"email=\'"+email+"\'",null);
        if(update == -1) {return false;}
        else {return true;}
    }
    public void updateAvatar(byte[] kep, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("avatar",kep);
        db.update("user",contentValues,"email=\'"+email+"\'",null);
    }

    public Cursor scoreboard()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from leaderboard order by score desc", null);
        return cursor;
    }
    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        } catch(UnsupportedEncodingException ex){
        }
        return null;
    }


    }


