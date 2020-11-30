package cnu.proejct.WTIsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

//DB 구성 어떻게 할지?

public class databaseManager extends AppCompatActivity {
    //device 목록 출력 -- 일반 리스트
    ArrayList<Device> deviceItems = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_manage);

        //dbSetting();
        init();
    }// end OnCreate

    //DB 구성 - 테스트 데이터 db에 삽입
    private void dbSetting(){
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        //테스트 데이터

        for(int i=1; i<10; i++) {
            String write_timestamp = String.format("timestamp %d",i);
            String write_apAdress = String.format("AP%d",i);
            String write_device = String.format("device %d",i);
            String write_certified = String.format("certified %d",i);

            dbInsert(db, DBHelper.TB_NAME1, write_timestamp, write_apAdress, write_device, write_certified);
        }
        db.close();
    }

    //db에 값 집어넣음
    void dbInsert(SQLiteDatabase db, String tableName, String timestamp, String apAdress, String device, String certified) {
        Log.d("[db] ", "Insert Data " + timestamp);

        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.TIMESTAMP, timestamp);
        contentValues.put(DBHelper.APADDRESS, apAdress);
        contentValues.put(DBHelper.DEVICE, device);
        contentValues.put(DBHelper.CERTIFIED, certified);

        // 리턴값: 생성된 데이터의 id
        db.insert(tableName, null, contentValues);
    }

    //화면 구성 - 버튼 연결, db값 불러와 뷰에 집어넣기
    private void init(){
        // --- 헤더 ---
        // gotoMapBT : 맵화면으로 돌아가는 버튼
        Button gotoMapBT = findViewById(R.id.gotoMapBT);
        gotoMapBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //테이블 선택 버튼들
        Button tbWtiBT = findViewById(R.id.tb_wti_btn);
        tbWtiBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDB(DBHelper.TB_NAME1);
            }
        });

        Button tbMapBT = findViewById(R.id.tb_map_btn);
        tbMapBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDB(DBHelper.TB_NAME2);
            }
        });

        // --- 리사이클러 뷰 ---
        // 리사이클러 뷰 아이템 클릭 시 map 경로화면으로 넘어감
        // recyclerView : 디바이스 목록
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select "+ DBHelper.TIMESTAMP +", " + DBHelper.APADDRESS + ", " + DBHelper.DEVICE + ", " + DBHelper.CERTIFIED + " from "+ DBHelper.TB_NAME1  + " order by _id asc", null);
        String read_timestamp= null;
        String read_apAdress= null;
        String read_device= null;
        String read_certified= null;

        Device recyclerItem ;

        while(cursor.moveToNext()){
            read_timestamp = cursor.getString(0);
            read_apAdress = cursor.getString(1);
            read_device = cursor.getString(2);
            read_certified = cursor.getString(3);

            recyclerItem = new Device(read_timestamp, read_apAdress, read_device, read_certified);
            deviceItems.add(recyclerItem);
            Log.d("[db] ", "select Data " + read_timestamp);
        }
        db.close();


        //xml연결, 레이아웃 매니저 지정
        recyclerView = findViewById(R.id.recyclerView_db);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //어댑터 객체 연결
        db_Adapter adapter = new db_Adapter(getApplicationContext(), deviceItems);
        recyclerView.setAdapter(adapter);
    }

    public void updateDB(String tableName){
        //내용 삭제
        deviceItems.clear();

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
        Device recyclerItem ;

        String read_timestamp= null;
        String read_apAdress= null;
        String read_device= null;
        String read_certified= null;

        if(tableName.equals(DBHelper.TB_NAME1)){
            deviceItems.add(new Device("TIMESTAMP", "APADDRESS", "DEVICE", "CERTIFIED"));
            cursor = db.rawQuery("select "+ DBHelper.TIMESTAMP +", " + DBHelper.APADDRESS + ", " + DBHelper.DEVICE + ", " + DBHelper.CERTIFIED + " from "+ tableName  + " order by "+ DBHelper.TIMESTAMP +" asc", null);

            while(cursor.moveToNext()){
                read_timestamp = cursor.getString(0);
                read_apAdress = cursor.getString(1);
                read_device = cursor.getString(2);
                read_certified = cursor.getString(3);

                recyclerItem = new Device(read_timestamp, read_apAdress, read_device, read_certified);
                deviceItems.add(recyclerItem);
            }
            Log.d("[db] ", "select Data " + DBHelper.TB_NAME1);

        }else if(tableName.equals(DBHelper.TB_NAME2)){
            deviceItems.add(new Device("APADDRESS", "LAT", "LNG", " "));
            cursor = db.rawQuery("select "+ DBHelper.APADDRESS +", " + DBHelper.LAT + ", " + DBHelper.LNG + " from "+ tableName  + " order by "+ DBHelper.APADDRESS + " asc", null);

            while(cursor.moveToNext()){
                read_timestamp = cursor.getString(0);
                read_apAdress = String.valueOf(cursor.getDouble(1));
                read_device = String.valueOf(cursor.getDouble(2));
                read_certified = " ";

                recyclerItem = new Device(read_timestamp, read_apAdress, read_device, read_certified);
                deviceItems.add(recyclerItem);

            }
            Log.d("[db] ", "select Data " + DBHelper.TB_NAME2);
        }

        db.close();

        Device_Adapter adapter = new Device_Adapter(getApplicationContext(), deviceItems);
        recyclerView.setAdapter(adapter);
    }

    public void deleteData(){
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        db.close();
    }
}
