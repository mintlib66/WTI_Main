package cnu.proejct.WTIsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Recycler_device_Activity extends AppCompatActivity {
//device 목록 출력 -- 일반 리스트
    ArrayList<Device> deviceItems = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_device);


        init();
    }// end OnCreate



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

        // *** 리사이클러 뷰 ***
        // 리사이클러 뷰 아이템 클릭 시 map 경로화면으로 넘어감
        // recyclerView : 디바이스 목록
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        //정렬순서: 1순위 timestamp > 2순위 dev
        Cursor cursor = db.rawQuery("select "+ DBHelper.TIMESTAMP +", " + DBHelper.APADDRESS + ", " + DBHelper.DEVICE + ", " + DBHelper.CERTIFIED + " from "+ DBHelper.TB_NAME1  + " order by "+ DBHelper.TIMESTAMP +" asc, " + DBHelper.DEVICE + " asc", null);
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
        recyclerView = findViewById(R.id.recyclerView_device);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //어댑터 객체 연결
        Device_Adapter adapter = new Device_Adapter(getApplicationContext(), deviceItems);
        recyclerView.setAdapter(adapter);
    }
}
