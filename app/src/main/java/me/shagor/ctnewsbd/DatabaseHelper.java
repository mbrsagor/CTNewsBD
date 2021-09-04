package me.shagor.ctnewsbd;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shagor on 7/15/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "CTNEWSAPP";

    // Table Names
    private static final String TABLE_LATEST = "latest";
    private static final String TABLE_POPULAR = "popular";
    private static final String TABLE_TOPNEWS = "topnews";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_POSTID = "postid";
    private static final String KEY_TITLE = "title";
    private static final String KEY_IMAGENAME = "image_name";
    private static final String KEY_DATE= "date";


    // Table Create Statements
    // latest table create statement
    private static final String CREATE_TABLE_LATEST = "CREATE TABLE "
            + TABLE_LATEST + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_POSTID
            + " TEXT," + KEY_TITLE + " TEXT," + KEY_IMAGENAME + " TEXT," + KEY_DATE
            + " TEXT" + ")";

    private static final String CREATE_TABLE_POPULAR = "CREATE TABLE "
            + TABLE_POPULAR + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_POSTID
            + " TEXT," + KEY_TITLE + " TEXT," + KEY_IMAGENAME + " TEXT," + KEY_DATE
            + " TEXT" + ")";

    private static final String CREATE_TABLE_TOPNEWS = "CREATE TABLE "
            + TABLE_TOPNEWS + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_POSTID
            + " TEXT," + KEY_TITLE + " TEXT," + KEY_IMAGENAME + " TEXT," + KEY_DATE
            + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_LATEST);
        db.execSQL(CREATE_TABLE_POPULAR);
        db.execSQL(CREATE_TABLE_TOPNEWS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_LATEST);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_POPULAR);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_TOPNEWS);

        // create new tables
        onCreate(db);
    }

    public  long insertData(String tablename,String postid,String title,String image_name,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_POSTID, postid);
        values.put(KEY_TITLE, title);
        values.put(KEY_IMAGENAME, image_name);
        values.put(KEY_DATE, date);

// Inserting Row
        long id= db.insert(tablename, null, values);
        return id;

    }
    public  void deleteallData(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tablename, null, null);
        db.close();
    }

    public Cursor getAllData(String table_name) {
        SQLiteDatabase mDB = this.getWritableDatabase();
        Cursor mCursor;
        mCursor = mDB.rawQuery("Select * from "+table_name, null);
        return mCursor;
    }


}

