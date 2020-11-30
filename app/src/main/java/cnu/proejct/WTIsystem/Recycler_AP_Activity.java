package cnu.proejct.WTIsystem;

//해당 AP에 감지된 device 목록 출력 -- 시간 기준 Expanded List 형태.

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Recycler_AP_Activity extends AppCompatActivity{
      private RecyclerView recyclerView;


      @Override
      protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.recycler_ap);

            init_BackButton();
            init_RecyclerView();

      }

      //*** 초기 레이아웃 구성 부분 ***

      private void init_BackButton(){
            //맵화면으로 이동하는 버튼-- finish로 해당 액티비티 종료
            Button gotoMapBT2 = (Button) findViewById(R.id.gotoMapBT2);
            gotoMapBT2.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                        finish();
                  }
            });
      }

      private void init_RecyclerView(){
            //리스트-- 리사이클러뷰
            recyclerView = (RecyclerView) findViewById(R.id.recyclerView_ap);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            List<AP_Adapter.Item> data = new ArrayList<>();  // 데이터를 담을 List

            selectRecord_map(data); //db 가져와서 리스트에 넣기

            recyclerView.setAdapter(new AP_Adapter(getApplicationContext(), data));
      }



      //*** 리사이클러뷰+상단 텍스트뷰에 데이터 넣는 부분 ***
      //DB값 가져와서 리스트에 넣기 -- timestamp순으로 정렬해서 가져옴
      public void selectRecord_map(List<AP_Adapter.Item> data){
            //전달 값 받아옴
            Intent intent = getIntent();
            String text_intent_AP_name = intent.getExtras().getString("AP_name");

            TextView apNameTitle = findViewById(R.id.tv_ap_name);
            apNameTitle.setText(text_intent_AP_name);

            DBHelper dbHelper = DBHelper.getInstance(this);

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            String sql = "select * from "+ DBHelper.TB_NAME1 +
                    " where "+ DBHelper.APADDRESS + " = '" + text_intent_AP_name +"'" +
                    " order by " + DBHelper.TIMESTAMP + " asc";
            Cursor cursor = db.rawQuery(sql,null);

            String timestamp = null; String apAddress = null; String deviceMac = null; String certified = null;
            List<Device> items;
            items = new ArrayList<>();

            cursor.moveToFirst();

            while(true){

                  //getColumnIndexOrThrow : 해당 칼럼의 인덱스를 가져옴.
                  //getString : 입력받은 칼럼인덱스에 해당하는 칼럼의 값을 String으로 반환함.
                  //timestamp: 현재 커서 기준을 이걸로 판별함
                  timestamp = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.TIMESTAMP));
                  apAddress = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.APADDRESS));
                  deviceMac = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DEVICE));
                  certified = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CERTIFIED));


                  //이번 커서가 마지막 레코드인지 확인
                  if(cursor.isLast() == false){

                        //마지막 레코드가 아닌경우 다음으로 이동
                        cursor.moveToNext();
                        String next = (cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.TIMESTAMP)));

                        //타임스탬프 값이 이전커서의 값과 달라지는 경우
                        if( !timestamp.equals(next) ) {
                              items.add(new Device(timestamp, apAddress, deviceMac, certified)); //items은 Device타입의 리스트 객체.
                              addItemGroup(data, items); //data는 AP_Adapter.Item타입의 리스트 객체. 그중에 Device타입의 변수?있음
                              items = new ArrayList<>();
                        }else if(cursor != null) // 이전커서값과 같은 경우
                              items.add(new Device(timestamp, apAddress, deviceMac, certified));

                        Log.v("＊LOG ", "now: "+ timestamp + ", next: "+next + ", cursor.Last() :"+ cursor.isLast());

                  }else{
                        Log.v("*** LOG - final record", "group:"+timestamp);
                        //마지막 레코드인 경우 그룹 추가 후 종료
                        items.add(new Device(timestamp, apAddress, deviceMac, certified));
                        addItemGroup(data, items);
                        break;
                  }

            }

      }


      //시간대별 그룹을 하나의 header+여러 child
      public void addItemGroup(List<AP_Adapter.Item> listName, List<Device> items_device){
            AP_Adapter.Item groupName = new AP_Adapter.Item(AP_Adapter.HEADER, items_device.get(0));
            groupName.invisibleChildren = new ArrayList<>();

            //child넣기
            for(int i=0; i<items_device.size(); i++){
                  AP_Adapter.Item expItem = new AP_Adapter.Item(AP_Adapter.CHILD, items_device.get(i));
                  expItem.setItemDevice(items_device.get(i));
                  groupName.invisibleChildren.add(expItem);
            }
            listName.add(groupName);
      }

}
