package cnu.proejct.WTIsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
      private static DBHelper dbInstance;

      private static final String DB_NAME = "wtiDB.db" ;
      private static final int DB_VERSION = 6;

      public static final String TB_NAME1 = "tb_wti";
      public static final String TB_NAME2 = "tb_map";

      public static final String TIMESTAMP = "timestamp";
      public static final String APADDRESS = "apAddress";
      public static final String DEVICE = "device";
      public static final String CERTIFIED = "certified";
      public static final String LAT = "lat";
      public static final String LNG = "lng";

      public static final String[] ALL_COLUMNS_WTI = {TIMESTAMP, APADDRESS, DEVICE, CERTIFIED};
      public static final String[] ALL_COLUMNS_MAP = {APADDRESS, LAT, LNG};

      //db생성자
      public DBHelper(@Nullable Context context) {
            super(context, DB_NAME, null, DB_VERSION);
      }

      //데이터 가져올때 쓰는거
      public static DBHelper getInstance(Context context){
            if (dbInstance == null){
                  dbInstance = new DBHelper(context);
            }
            return dbInstance;
      }

      //db 내 테이블생성
      @Override
      public void onCreate(SQLiteDatabase db) {
            String wtiSQL = "CREATE TABLE " + TB_NAME1 +
                    "(_id integer primary key autoincrement,"+
                    TIMESTAMP + " text," +
                    APADDRESS + " text,"+
                    DEVICE + " text," +
                    CERTIFIED + " text" +
                    ")";
            db.execSQL(wtiSQL);

            wtiSQL = "CREATE TABLE " + TB_NAME2 +
                    "(_id integer primary key autoincrement,"+
                    APADDRESS + " text," +
                    LAT + " double," +
                    LNG + " double" +
                    ")";
            db.execSQL(wtiSQL);
      }

      //db 버전 업그레이드
      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TB_NAME1);
            db.execSQL("DROP TABLE IF EXISTS " + TB_NAME2);
            onCreate(db);
      }
}
