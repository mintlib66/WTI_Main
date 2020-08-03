package cnu.proejct.WTIsystem;
//추가 필요 : 본인 위치 /
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationProviderClient; //위치값 획득용
    private final LatLng mDefaultLocation = new LatLng(36.364739, 127.344349);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;

    SQLiteDatabase db = null;

/*
    //런타임 권한 요청
    private void getLocationPermission(){
        if (ContextCompat.checkSelfPermission(this.getApplicationContext() ,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        mLocationPermissionGranted = false;
        switch (requestCode){
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION : {
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //구글 맵
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //목록보기 버튼
        final Button gotoListBT = (Button)findViewById(R.id.gotoListBT);
        gotoListBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        ListActivity.class);
                startActivity(intent);
            }
        });

        //모드 선택 스피너
        Spinner spinner = (Spinner)findViewById(R.id.modeSpinner);

        ArrayAdapter modeAdapter = ArrayAdapter.createFromResource(
                this, R.array.displayMode, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(modeAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                //0 : 단말모드, 1: AP모드
                if(position == 0){
                    //목록보기 버튼 보임, 지도에 점 안보임
                    gotoListBT.setVisibility(View.VISIBLE);
                    //버튼 누르면 지도 내의 단말들  리스트 출력.
                    parent.getItemAtPosition(position);
                }
                else if(position == 1){
                    //목록보기 버튼 안보임, 지도에 AP 점으로 보임.
                    gotoListBT.setVisibility(View.INVISIBLE);
                    //AP클릭 시 해당 AP에서 감지된 목록 보여줌
                    parent.getItemAtPosition(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {

        //맵 객체 추가
        mMap = googleMap;

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);

        //** 마커 표시하는 부분 하드코딩 : 개별 메소드로 분리해서 적용시킬 것. ***/
        //마커 추가
        LatLng CNU1 = new LatLng(36.364739, 127.344349);
        LatLng CNU2 = new LatLng(36.369448, 127.344456);

        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1
                .position(CNU1)
                .title("1번 단말")
                .snippet("해당 단말의 상세정보가 들어갑니다.")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2
                .position(CNU2)
                .title("2번 단말")
                .snippet("해당 단말의 상세정보가 들어갑니다.");

        mMap.addMarker(markerOptions1).showInfoWindow();
        mMap.addMarker(markerOptions2);

        //마커클릭 이벤트 리스너
        mMap.setOnMarkerClickListener(this);

        //카메라 이동, 기본 줌 레벨 설정
        mMap.moveCamera(CameraUpdateFactory.newLatLng(CNU1));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //updateLocationUI();
       // getDeviceLocation();

    }

    // 마커클릭 이벤트  처리
    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(this, marker.getTitle() + "\n" + marker.getPosition(), Toast.LENGTH_SHORT).show();
        return true;
    }
}

