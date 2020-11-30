package cnu.proejct.WTIsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

//메인 액티비티
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private GoogleMap mMap;
    Button gotoListBT;
    Button dbManageBT;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //구글 맵
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initButton();
        initButton2();
        dbManageBT.setVisibility(View.INVISIBLE);
        //initSpinner();

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        //맵 객체 추가
        mMap = googleMap;

        //UI 세팅
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);

        //db세팅
        //init_tb2_dbSetting(); init_tb1_dbSetting(); init_tb1_dbSetting2();

        LatLng centerPoint = selectRecord_map();

        //마커클릭 이벤트 리스너
        mMap.setOnMarkerClickListener(this);

        //카메라 이동, 기본 줌 레벨 설정
        mMap.moveCamera(CameraUpdateFactory.newLatLng(centerPoint));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //updateLocationUI();
       // getDeviceLocation();
    }

    //------------- map create fn-------------------//
    //목록보기 버튼
    public void initButton(){
        gotoListBT = (Button)findViewById(R.id.gotoListBT);
        gotoListBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main_dev = new Intent(
                        getApplicationContext(),
                        Recycler_device_Activity.class);
                startActivity(intent_main_dev);
            }
        });
    }

    //db관리 버튼
    public void initButton2(){
        dbManageBT = (Button)findViewById(R.id.dbManageBT);
        dbManageBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_main_db = new Intent(
                        getApplicationContext(),
                        databaseManager.class);
                startActivity(intent_main_db);
            }
        });
    }

    //스피너 버튼
    /*
    public void initSpinner(){
        //모드 선택 스피너 생성
        spinner = (Spinner)findViewById(R.id.modeSpinner);
        //스피너 어댑터 연결
        ArrayAdapter modeAdapter = ArrayAdapter.createFromResource(
                this, R.array.displayMode, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(modeAdapter);

        //스피너 모드에 따른 이벤트 처리
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                //0 : 단말모드, 1: AP모드
                if(position == 0){
                    //목록보기 버튼 보임,  버튼 누르면 지도 내의 단말들  리스트 출력.
                    gotoListBT.setVisibility(View.VISIBLE);
                    parent.getItemAtPosition(position);

                    //markerOptions1.visible(false);
                }
                else if(position == 1){
                    //목록보기 버튼 안보임
                    gotoListBT.setVisibility(View.INVISIBLE);
                    parent.getItemAtPosition(position);

                    //지도에 AP 점으로 보임.
                    //AP클릭 시 해당 AP에서 감지된 목록 보여줌
                    //markerOptions1.visible(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });
    }
    */
    //------------- map ready fn-------------------//

    //*** 테스트 데이터 구성하는 부분 ***
    //테스트용 데이터 DB 생성
    public void init_tb2_dbSetting(){
        Context context = getApplicationContext();
        inserRecord_map(context, "AP1", 36.364739, 127.344349);
        inserRecord_map(context, "AP2", 36.369448, 127.344456);
        inserRecord_map(context, "AP3", 36.367255, 127.344480);
        inserRecord_map(context, "AP4", 36.370255, 127.343680);
        inserRecord_map(context, "AP5", 36.380255, 127.342680);
        inserRecord_map(context, "AP6", 36.357255, 127.344080);
    }

    //레코드 insert용
    public void inserRecord_map(Context context, String apAddress, double lat, double lng){
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.APADDRESS, apAddress);
        values.put(DBHelper.LAT, lat);
        values.put(DBHelper.LNG, lng);

        //중복체크
        //Cursor curChk = db.rawQuery("SELECT * FROM "+DBHelper.TB_NAME2+" WHERE "+  + "='"+apAddress+"' AND date="+date+";", null);
        db.insert(DBHelper.TB_NAME2, null, values);
    }

    //DB 구성 - 테스트 데이터 db에 삽입
    private void init_tb1_dbSetting(){
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        //테스트 데이터
        for(int i=1; i<7; i++) {
            String write_timestamp = String.format("0%d:00",i+1);
            String write_apAdress = String.format("AP%d",i);
            String write_device = String.format("dev%d",i+1);
            String write_certified = String.format("인가단말");

            dbInsert(db, DBHelper.TB_NAME1, write_timestamp, write_apAdress, write_device, write_certified);
        }

        for(int i=1; i<7; i++) {
            String write_timestamp = String.format("%d:00",i+10);
            String write_apAdress = String.format("AP%d",i);
            String write_device = String.format("dev%d",i+2);
            String write_certified = String.format("인가단말");

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


    //테스트용 데이터 DB 생성
    public void init_tb1_dbSetting2(){
        Context context = getApplicationContext();
        SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();

        for(int i=0; i<7; i++) {
            String num = Integer.toString(i);
            String num2 = Integer.toString(i+1);
            String num3 = Integer.toString(i+2);
            inserRecord_wti(db, context, "09:00", "AP"+num, "dev"+num2, "인가단말");
            inserRecord_wti(db, context, "12:00", "AP"+num, "dev"+num, "인가단말");
            inserRecord_wti(db, context, "14:00", "AP"+num, "dev"+num3, "인가단말");
            inserRecord_wti(db, context, "17:00", "AP"+num, "dev"+num, "인가단말");
        }

        db.close();
    }

    //레코드 추가
    public void inserRecord_wti(SQLiteDatabase db, Context context, String timestamp, String apAddress, String deviceMAC, String isCertified){

        //column name을 key로해서 테이블에 삽일할 값의 집합 생성
        ContentValues values = new ContentValues();
        values.put(DBHelper.TIMESTAMP, timestamp);
        values.put(DBHelper.APADDRESS, apAddress);
        values.put(DBHelper.DEVICE, deviceMAC);
        values.put(DBHelper.CERTIFIED, isCertified);

        db.insert(DBHelper.TB_NAME1, null, values);
    }

    //--------------------------------------------------------------//



    //DB값 가져와서 맵에 AP마커 표시
    public LatLng selectRecord_map(){
        DBHelper dbHelper = DBHelper.getInstance(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.TB_NAME2, null, null, null, null, null, null);

        String title = null; double lat = 0; double lng = 0;

        while(cursor.moveToNext()){
            title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.APADDRESS));
            lat = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.LAT));
            lng = cursor.getDouble(cursor.getColumnIndexOrThrow(DBHelper.LNG));

            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions
                    .visible(true)
                    .position(new LatLng(lat, lng))
                    .title(title)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            mMap.addMarker(markerOptions);
            //마커클릭 이벤트 처리
        }
        db.close();
        return new LatLng(lat, lng);
    }

    //맵에 AP 마커 표시
    public void makeAPMarkersOnMap(List<LatLng> latLngs, List<String> titles){
        //리스트의 마커 수만큼 표시
        for(int i = 0; i<latLngs.size(); i++){
            MarkerOptions markerOptions = new MarkerOptions();

            markerOptions
                    .visible(true)
                    .position(latLngs.get(i))
                    .title(titles.get(i))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

            mMap.addMarker(markerOptions);
        }
    }

    //마커클릭 이벤트 처리
    @Override
    public boolean onMarkerClick(Marker marker) {
        //마커 클릭시 리사이클러 AP 뷰 화면으로 넘어가도록 할 것
        Intent intent_main_ap = new Intent(getApplicationContext(), Recycler_AP_Activity.class);
        intent_main_ap.putExtra("AP_name", marker.getTitle());

        startActivityForResult(intent_main_ap, 101);
        return true;
    }

    //
    private void updateLocationUI(){
        if (mMap == null) {
            return;
        }
        try{
            if(true){

            }
        }catch (SecurityException e){

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (mMap != null) {
            super.onSaveInstanceState(outState);
        }
    }
}//end class
