package cnu.proejct.WTIsystem;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//데이터 받아오는거만 씀 - query
public class DataProvider extends ContentProvider {
      public static final String AUTHORITY = "cnu.project.WTIsystem";
      public static final String BASE_PATH = "WTI_list";
      public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

      private static final int AP_TB = 1;
      private static final int MAP_TB = 2;
      private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
      private SQLiteDatabase database;

      @Override
      public boolean onCreate() {
            return false;
      }

      @Nullable
      @Override
      public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
            //조회
            Cursor cursor;

            switch (uriMatcher.match(uri)) {
                  case AP_TB:
                        cursor = database.query(DBHelper.TB_NAME1, DBHelper.ALL_COLUMNS_WTI, selection, selectionArgs, null, null,DBHelper.TIMESTAMP + " ASC");
                        break;
                  case MAP_TB:
                        cursor = database.query(DBHelper.TB_NAME2, DBHelper.ALL_COLUMNS_MAP, selection, selectionArgs, null, null,DBHelper.APADDRESS + " ASC");
                        break;

                  default:
                        throw new IllegalArgumentException("알 수 없는 URI : " + uri);
            }
            cursor.setNotificationUri(getContext().getContentResolver(), uri);

            return cursor;
      }

      @Nullable
      @Override
      public String getType(@NonNull Uri uri) {
            return null;
      }

      @Nullable
      @Override
      public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
            return null;
      }

      @Override
      public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
            return 0;
      }

      @Override
      public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
            return 0;
      }
}
