package com.developments.ar.feedbox;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;



public class Sql_db extends SQLiteOpenHelper {

    private Object folder;

    public Sql_db(Context context) {
        super(context, "Feedbox", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Details = "Create table feedbox (f_id INTEGER DEFAULT 1, fcode Varchar(10), f_icon Varchar(50), f_title Varchar(30), f_pos INTEGER DEFAULT 1, f_siz INTEGER DEFAULT 1, f_theme INTEGER DEFAULT 1, f_cont varchar(2000))";
        db.execSQL(Details);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS feedbox");
        onCreate(db);
    }

    public void initial_db(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "insert into feedbox (f_id,fcode,f_icon,f_title,f_pos,f_siz,f_theme,f_cont) values(1,null,null,null,null,null,null,null)";
        db.execSQL(sql);
    }

    public String[] get_app_details() {
        String[] content = new String[7];
        String  selectQuery = "select * from feedbox where f_id=1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("anssss", "up");

        if (cursor.moveToFirst()) {
            Log.d("anssss","if");
            do {
                content[0]=cursor.getString(1);
                content[1]=cursor.getString(2);
                content[2]=cursor.getString(3);
                content[3]=cursor.getString(4);
                content[4]=cursor.getString(5);
                content[5]=cursor.getString(6);
                content[6]=cursor.getString(7);
                Log.d("anssss"," "+content[0]+"  "+content[1]+" "+content[2]+"  "+content[3]+"  "+content[4]+"  "+content[5]+"  "+content[6]+"  ");
            }
            while (cursor.moveToNext());
        }
        return content;
    }

    public void update_app_details(String fcode,String f_icon,String f_title,int f_pos,int f_siz,int f_theme,String f_cont) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE feedbox set fcode ='"+fcode+"', f_icon ='"+f_icon+"', f_title ='"+f_title+"', f_pos ="+f_pos+", f_theme ="+f_theme+", f_cont ='"+f_cont+"', f_siz ="+f_siz+" where f_id=1";
        db.execSQL(sql);
    }

    public void create_table(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS feedtable");
//        if (cont!="") {
//            String[] re_arry1 = cont.split("\\$");
//            int count=1;
//            for (String ans : re_arry1) {
//                String[] re_arry2=ans.split("\\|");
//                String opt=re_arry2[0];
//                String name=re_arry2[1];
//                String f2=re_arry2[2];
//                String f3=re_arry2[3];
//                String f4=re_arry2[4];
//                String f0="field"+count;
//                Log.d("ok", "obj= {'id':'" + f0 + "','opt':'" + opt + "','f1':'" + name + "','f2':'" + f2 + "','f3':'" + f3 + "','f4':'" + f4 + "'};");
//                count++;
//            }
//        }
//        String[] re_arry1 = cont.split("\\$");
//        String tab_column="No INTEGER PRIMARY KEY AUTOINCREMENT ,";
//        int count=1;
//        for (String ans : re_arry1) {
//            String[] re_arry2=ans.split("\\|");
//            if(!re_arry2[0].equals("5") && !re_arry2[0].equals("8"))
//            {
//                tab_column+="col"+count+" varchar(100),";
//                count++;
//            }
//
//
//        }
//        tab_column=tab_column.substring(0,tab_column.length()-1);
//        Log.d("found", tab_column + "");

        String sql3 = "create table feedtable (cont varchar(2000))";
        db.execSQL(sql3);
    }

    public void insert_into_table(String cont){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "insert into feedtable values ('"+cont+"')";
        db.execSQL(sql);
    }

    public String get_offline_count(){
        String ans = null;
        String  selectQuery = "select count(*) from feedtable";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            do {
                ans=cursor.getString(0);
            }
            while (cursor.moveToNext());
        }
        return ans;
    }

    public ArrayList get_offline_entry() {

        ArrayList arrayList=new ArrayList();
        String  selectQuery = "select * from feedtable";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("anssss", "up");

        if (cursor.moveToFirst()) {
            Log.d("anssss","if");
            do {
                //content[0]=cursor.getString(1);
                arrayList.add(cursor.getString(0));
//                content[1]=cursor.getString(2);
//                content[2]=cursor.getString(3);
//                content[3]=cursor.getString(4);
//                content[4]=cursor.getString(5);
//                content[5]=cursor.getString(6);
//                content[6]=cursor.getString(7);
//                Log.d("anssss"," "+content[0]+"  "+content[1]+" "+content[2]+"  "+content[3]+"  "+content[4]+"  "+content[5]+"  "+content[6]+"  ");
            }
            while (cursor.moveToNext());
        }
        return arrayList;
    }


}