package com.example.vault;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int version=1;
    private static final String DATABASE_NAME="password_db";

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Data.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Data.TABLE_NAME);
        onCreate(db);
    }
    public long insertPassword(Data data){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Data.COLUMN_WEB,data.getWebsite());
        contentValues.put(Data.COLUMN_USERNAME,data.getEmail());
        contentValues.put(Data.COLUMN_PASSWORD,data.getPassword());
        long id=database.insert(Data.TABLE_NAME,null,contentValues);
        return id;
    }
    public Data getData(long id){
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.query(Data.TABLE_NAME,new String[]{Data.COLUMN_ID,Data.COLUMN_WEB,Data.COLUMN_USERNAME,Data.COLUMN_PASSWORD},Data.COLUMN_ID+"=?",new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor!=null) cursor.moveToFirst();
        Data data=new Data(cursor.getInt(cursor.getColumnIndex(Data.COLUMN_ID)),cursor.getString(cursor.getColumnIndex(Data.COLUMN_WEB)),cursor.getString(cursor.getColumnIndex(Data.COLUMN_USERNAME)),cursor.getString(cursor.getColumnIndex(Data.COLUMN_PASSWORD)));
        cursor.close();
        return data;
    }
    public List<Data> getAllPassword(){
        List<Data> dataList=new ArrayList<>();
        String query="SELECT * FROM "+Data.TABLE_NAME;
        SQLiteDatabase database=this.getWritableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        if (cursor.moveToFirst()){
            do {
                Data data=new Data();
                data.setId(cursor.getInt(cursor.getColumnIndex(Data.COLUMN_ID)));
                data.setWebsite(cursor.getString(cursor.getColumnIndex(Data.COLUMN_WEB)));
                data.setEmail(cursor.getString(cursor.getColumnIndex(Data.COLUMN_USERNAME)));
                data.setPassword(cursor.getString(cursor.getColumnIndex(Data.COLUMN_PASSWORD)));
                dataList.add(data);
            } while (cursor.moveToNext());
        }
        database.close();
        return dataList;
    }
    public int getNotesCount(){
        String query="SELECT * FROM "+Data.TABLE_NAME;
        SQLiteDatabase database=this.getReadableDatabase();
        Cursor cursor=database.rawQuery(query,null);
        int count=cursor.getCount();
        cursor.close();
        return count;
    }

    public void deleteData(Data data){
        SQLiteDatabase database=this.getReadableDatabase();
        database.delete(Data.TABLE_NAME,Data.COLUMN_ID+" =?",new String[]{String.valueOf(data.getId())});
        database.close();
    }
    public int updateData(Data data){
        SQLiteDatabase database=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Data.COLUMN_WEB,data.getWebsite());
        contentValues.put(Data.COLUMN_USERNAME,data.getEmail());
        contentValues.put(Data.COLUMN_PASSWORD,data.getPassword());
        return database.update(Data.TABLE_NAME,contentValues,Data.COLUMN_ID+"=?",new String[]{String.valueOf(data.getId())});
    }
}