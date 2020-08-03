package cnu.proejct.WTIsystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//아직 진행중

public class manageDB extends SQLiteOpenHelper {
    static final String DB_name = "WTI.db" ;
    static final String table_name = "AP";
    static final int db_version = 1;

    public manageDB(Context context, int version){
        super(context, DB_name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE tableName (name TEXT, info TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS tableName");
        onCreate(db);
    }
}
