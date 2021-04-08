package com.purchase.avertimed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.purchase.avertimed.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper_MultipleData extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "avertimed.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "gamedetails";
    public static String Game_id = "Id";
    public static String Game_Name = "Game_Name";
    public static String Image_Url = "Image_Url";
    private Context context;

    public static final String ID = "Fav_Id";
    public static final String FAV_Name = "FAV";
    public static final String FAV_QT = "FAV_QT";
    public static final String FAV_IURL = "FAV_IURL";
    public static final String FAV_Price = "FAV_Price";
    private static final String TABLE_NAME_FAV = "fav_game_data";
    private static final String TABLE_WISH = "TABLE_WISH";


    public static final String RECENT_SEARCH = "RECENT_SEARCH";
    private static final String TABLE_NAME_RECENTSEARCH = "TABLE_NAME_RECENTSEARCH";


    /* renamed from: db */
    private SQLiteDatabase f110db = getWritableDatabase();

    public DbHelper_MultipleData(Context context2) {
        super(context2, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context2;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String cat_rec = "Create table " + TABLE_NAME_FAV +
                "(" + FAV_Name + " TEXT,"
                + ID + " TEXT,"
                + FAV_IURL + " TEXT,"
                + FAV_QT+ " TEXT,"
                + FAV_Price + " TEXT"
                + ")";


        String wish = "Create table " + TABLE_WISH +
                "(" + FAV_Name + " TEXT,"
                + ID + " TEXT,"
                + FAV_IURL + " TEXT,"
                + FAV_QT+ " TEXT,"
                + FAV_Price + " TEXT"
                + ")";

        String recent_search = "Create table " + TABLE_NAME_RECENTSEARCH +
                "(" + RECENT_SEARCH + " TEXT" + ")";

        db.execSQL(cat_rec);
        db.execSQL(wish);
        db.execSQL(recent_search);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAV);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_RECENTSEARCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISH);
        onCreate(db);
    }

    public Boolean insert_Rec(String fav_name,
                              String id,
                              String image_Url,String Price,String QT) {

        Log.e("Data",fav_name+id+image_Url);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FAV_Name, fav_name);
        values.put(ID, id);
        values.put(FAV_IURL, image_Url);
        values.put(FAV_QT, QT);
        values.put(FAV_Price, Price);
        db.insert(TABLE_NAME_FAV, null, values);
        db.close();
        return null;

    }

    public Boolean insert_Fav(String fav_name,
                              String id,
                              String image_Url,String Price,String QT) {

        Log.e("Data",fav_name+id+image_Url);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FAV_Name, fav_name);
        values.put(ID, id);
        values.put(FAV_IURL, image_Url);
        values.put(FAV_QT, QT);
        values.put(FAV_Price, Price);
        db.insert(TABLE_WISH, null, values);
        db.close();
        return null;

    }

    public Boolean insert_Search(String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RECENT_SEARCH, name);
        db.insert(TABLE_NAME_RECENTSEARCH, null, values);
        db.close();
        return null;

    }


    public Boolean Update(String id,String QT) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FAV_QT, QT);
        db.update(TABLE_NAME_FAV, values,ID+"=" +id,null);
        db.close();
        return null;

    }
    public Boolean delete_rec(String name){

        SQLiteDatabase db = this.getWritableDatabase();



        db.delete(TABLE_NAME_FAV,ID+"="+ name,null);
        db.close();
        return null;


    }

    public Boolean delete(String name){

        SQLiteDatabase db = this.getWritableDatabase();



        db.delete(TABLE_WISH,ID+"="+ name,null);
        db.close();
        return null;


    }

    public Boolean DeleteAll(){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_FAV,null,null);
        db.close();
        return null;


    }



    public List<FavDatabaseModel> getFav_Rec() {
        List<FavDatabaseModel> List = new ArrayList<FavDatabaseModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_FAV;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    FavDatabaseModel contact = new FavDatabaseModel();
                    // contact.setId(Integer.parseInt(cursor.getString(0)));
                    contact.setFavName(cursor.getString(0));
                    contact.setId(cursor.getString(1));
                    contact.setImage_url(cursor.getString(2));
                    contact.setQT(cursor.getString(3));
                    contact.setPrice(cursor.getString(4));
                    //Log.e("values", "old data value: " + contact.getFname());
                    List.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
            cursor.close();
        }

        return List;

    }


    public List<FavDatabaseModel> getFav() {
        List<FavDatabaseModel> List = new ArrayList<FavDatabaseModel>();

        String selectQuery = "SELECT  * FROM " + TABLE_WISH;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    FavDatabaseModel contact = new FavDatabaseModel();
                    // contact.setId(Integer.parseInt(cursor.getString(0)));
                    contact.setFavName(cursor.getString(0));
                    contact.setId(cursor.getString(1));
                    contact.setImage_url(cursor.getString(2));
                    contact.setQT(cursor.getString(3));
                    contact.setPrice(cursor.getString(4));
                    //Log.e("values", "old data value: " + contact.getFname());
                    List.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
            cursor.close();
        }

        return List;

    }


    public List<FavDatabaseModel> getSearch() {
        List<FavDatabaseModel> List = new ArrayList<FavDatabaseModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME_RECENTSEARCH;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    FavDatabaseModel contact = new FavDatabaseModel();
                    contact.setFavName(cursor.getString(0));
                    List.add(0,contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
            cursor.close();
        }

        return List;

    }


}
