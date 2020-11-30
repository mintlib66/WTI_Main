package cnu.proejct.WTIsystem;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Iterator;
import java.util.List;

public class Device_Result extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
      //변수들 선언
      private GoogleMap mMap;
      TextView tv_time; TextView tv_apAdrs; TextView tv_device; TextView tv_certified;
      TextView tv_detail;
      String selectedDevice = "";
      String selectedCertified = "";
      List<LatLng> latLng = new ArrayList<LatLng>();
      List<String> title = new ArrayList<String>(); //title에 AP 표시
      List<String> snippet = new ArrayList<String>(); //snippet에 시간 표시
      List<String> deviceInfo = new ArrayList<String>();;
      List<String> certifiedInfo = new ArrayList<String>();;
      //List<Device> wti_devices = new ArrayList<>();

      //클릭안해도 타이틀 뜨게하려면 어케 해야하냐

      @Override
      protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.map_device);

            initLayout(); //레이아웃 초기화
            initIntentData(); //Device_Adapter에서 보낸 인텐트값 받음, 해당 값으로 초기 위치 설정
            getDeviceRoute(selectedDevice); //db검색해서 지도에 경로 표시세팅
      }

      @Override
      public void onMapReady(final GoogleMap googleMap) {
            //맵 객체 추가
            mMap = googleMap;

            //UI 세팅
            UiSettings uiSettings = mMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setMyLocationButtonEnabled(true);

            //맵에 마커 배치
            makeAPMarkersOnMap(latLng, title, snippet);

            //마커클릭 이벤트 리스너
            mMap.setOnMarkerClickListener(this);

            //카메라 이동, 기본 줌 레벨 설정
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng.get(0)));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
      }

      //-----------------------------//

      //***레이아웃 관련 초기화 -- 맵, 하단 텍스트뷰 그룹
      void initLayout(){
            //맵 파트
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
            mapFragment.getMapAsync((OnMapReadyCallback) this);

            //텍스트뷰 xml 연결
            tv_time = findViewById(R.id.textView1);
            tv_apAdrs = findViewById(R.id.textView2);
            tv_device = findViewById(R.id.textView3);
            tv_certified = findViewById(R.id.textView4);
            tv_detail = findViewById(R.id.textView_detail);
      }

      //***intent값 관련 초기화 -- 전달 값 받아오기 + db검색
      void initIntentData(){
            //전달 값 받아옴
            Intent intent = getIntent();
            String contractTime = intent.getExtras().getString("tv_time");
            String text_intent_apAdress = intent.getExtras().getString("tv_apAdrs");
            selectedDevice = intent.getExtras().getString("tv_device");
            selectedCertified = intent.getExtras().getString("tv_certified");

            //텍스트뷰에 초기값 넣음
            tv_time.setText(contractTime);
            tv_apAdrs.setText(text_intent_apAdress);
            tv_device.setText(selectedDevice);
            tv_certified.setText(selectedCertified);


            //makeMarkerUsingDB : 현재 입력 ap값에 해당하는 latlng값 db에서 검색해서 출력.
            //DB열고 apAdress값으로 tb_wti조회해서 매칭값 가져옴
            LatLng detail_latlng = makeMarkerUsingDB(text_intent_apAdress);
            latLng.add(detail_latlng);

            String detailstr = "위도: " + (detail_latlng.latitude) + ", 경도: " + (detail_latlng.longitude);
            tv_detail.setText(detailstr);

            //맵 마커 정보값 세팅용 리스트에 값 추가
            title.add(text_intent_apAdress);
            snippet.add(contractTime);
            deviceInfo.add(selectedDevice);
            certifiedInfo.add(selectedCertified);
      }

      // ap조건으로 DB 검색 - 위도경도 리턴
      public LatLng makeMarkerUsingDB(String apAddress){
            DBHelper dbHelper = DBHelper.getInstance(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String sql = "select * from "+ DBHelper.TB_NAME2 +" where "+ DBHelper.APADDRESS + " = '" + apAddress +"'";
            Cursor cursor = db.rawQuery(sql,null);
            double lat = 0; double lng = 0;

            while(cursor.moveToNext()){
                  lat = cursor.getDouble(2);
                  lng = cursor.getDouble(3);
            }
            db.close();
            return new LatLng(lat, lng);
      }

      //*** db검색해서 해당 단말 루트 db를 리스트에 저장. 이 때 중복값은 표시 x
      public void getDeviceRoute(String selectedDevice){
            DBHelper dbHelper = DBHelper.getInstance(this);
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String ap;

            String sql = "select * from "+ DBHelper.TB_NAME1 +
                    " where "+ DBHelper.DEVICE + " = '" + selectedDevice +"'" +
                    " order by " + DBHelper.TIMESTAMP + " asc";
            Cursor cursor = db.rawQuery(sql,null);

            while(cursor.moveToNext()){
                  //해당하는 ap와 timestamp -> title, snippet에
                  ap = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.APADDRESS));
                  title.add(ap);
                  snippet.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.TIMESTAMP)));

                  deviceInfo.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DEVICE)));
                  certifiedInfo.add(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CERTIFIED)));

                  //위도 경도 ->latlng에
                  latLng.add(makeMarkerUsingDB(ap));
            }
            db.close();

      }


      //맵에 마커 배치- 리스트 스팟 전부
      //스니펫에 다른 정보들도 표기
      public void makeAPMarkersOnMap(List<LatLng> latLngs, List<String> titles, List<String> snippet){
            for(int i = latLngs.size()-1; i>=0; i--){
                  MarkerOptions markerOptions = new MarkerOptions();

                  markerOptions
                          .visible(true)
                          .position(latLngs.get(i))
                          .title(titles.get(i))
                          .snippet(snippet.get(i))
                          .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                  Marker marker = mMap.addMarker(markerOptions);
                  marker.showInfoWindow(); //마지막에 나오는게 intent 마커

            }
      }

      //마커 클릭 이벤트 처리 -- 마커 클릭 시 하단 텍스트뷰가 클릭값 기준으로 바뀌는거
      @Override
      public boolean onMarkerClick(Marker marker) {
            //하단 텍스트뷰...
            String contractTime = marker.getSnippet();
            String apAddress = marker.getTitle();

            //텍스트뷰에 값 넣음
            tv_time.setText(contractTime);
            tv_apAdrs.setText(apAddress);

            //기본적으로 device값은 고정(device기준으로 루트 뜨니까)
            tv_device.setText(selectedDevice);
            //인가여부도 고정
            tv_certified.setText(selectedCertified);

            //한 AP에서 여러 번 감지된 경우 어느 시간들에서 감지되었는지.
            //단말 이동경로 정보를 출력하면 좋은데 스크롤뷰?


            //makeMarkerUsingDB : 현재 입력 ap값에 해당하는 latlng값 db에서 검색해서 출력.
            //DB열고 apAdress값으로 tb_wti조회해서 매칭값 가져옴
            LatLng detail_latlng = makeMarkerUsingDB(apAddress);
            latLng.add(detail_latlng);

            String detailstr = "위도: " + (detail_latlng.latitude) + ", 경도: " + (detail_latlng.longitude);
            tv_detail.setText(detailstr);

            return false;
      }

}
