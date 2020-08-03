package cnu.proejct.WTIsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    /*
    manageDB managedb;
    SQLiteDatabase db = null;
    ListView listView1, listView2;
    ArrayAdapter adapter1, adapter2;
    Cursor  cursor;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //리스트뷰 연결
        ListView listView = (ListView)findViewById(R.id.listview);
        /*
        managedb = new manageDB(this, 1);
        db = managedb.getReadableDatabase();

        //리스트 삽입 항목 : 시간, AP정보, MAC, 인가여부
        String sql = "select * from info where id='time' ";
        cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            String pass = cursor.getString(1);
        }
        */


        List<String> list = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        list.add("1번");
        list.add("2번");
        list.add("3번 ");
        list.add("4번");
        list.add("5번");

        /*
        db.close();
        cursor.close();
        */

        //맵화면으로 이동하는 버튼
        Button gotoMapBT = (Button)findViewById(R.id.gotoMapBT);
        gotoMapBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        getApplicationContext(),
                        MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
