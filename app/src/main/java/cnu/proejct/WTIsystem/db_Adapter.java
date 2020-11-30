package cnu.proejct.WTIsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//ListActivity를 지금.. RecyclerView로 변경 후 연동

public class db_Adapter extends RecyclerView.Adapter<db_Adapter.ViewHolder>{
      private ArrayList<Device> items = new ArrayList<Device>();
      Context mContext;

      //생성자
      public db_Adapter(Context mContext, ArrayList<Device> items){
            this.mContext = mContext;
            this.items = items;
      }

      //뷰홀더 클래스
      //뷰홀더 : 레이아웃에 해당하는 뷰를 담아두는 객체. 필요하면 재사용.
      public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView tv1;
            private TextView tv2;
            private TextView tv3;
            private TextView tv4;

            public ViewHolder(View itemView){
                  super(itemView);

                  //xml 뷰파일 내의 아이템 id연결
                  tv1 = itemView.findViewById(R.id.tv_time);
                  tv2 = itemView.findViewById(R.id.tv_apAdrs);
                  tv3 = itemView.findViewById(R.id.tv_device);
                  tv4 = itemView.findViewById(R.id.tv_certified);

                  itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              //아이템 클릭 이벤트
                              int pos = getAdapterPosition();
                              if (pos != RecyclerView.NO_POSITION) {
                                    //itemClickEvent(mContext, pos);
                              }
                        }
                  });
            }
      }

      //뷰홀더 객체 생성해서 리턴
      @NonNull
      @Override
      public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.ap_item, parent, false);
            return new db_Adapter.ViewHolder(itemView);
      }

      //바인딩 -- 뷰에 데이터 표시함
      @Override
      public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Device item = items.get(position);
            holder.tv1.setText(item.getTimestamp());
            holder.tv2.setText(item.getApAddress());
            holder.tv3.setText(item.getDeviceMAC());
            holder.tv4.setText(item.getIsCertified());
      }

      //데이터 개수 리턴
      @Override
      public int getItemCount() {
            return items.size();
      }

      //클릭이벤트-> 삭제 다일로그 뜸
      //리사이클러뷰에서는 Context를 사용할 수 없음 -> 전역변수로 만들어서 사용
      /*
      public void itemClickEvent(Context mContext, int pos){
            // 정보값 전달
            String tv_time = items.get(pos).getTimestamp();
            String tv_apAdrs = items.get(pos).getApAddress();
            String tv_device = items.get(pos).getDeviceMAC();
            String tv_certified = items.get(pos).getIsCertified();

            AlertDialog.Builder dlg = new AlertDialog.Builder(this); //...........컨텍스트 짜증나
            dlg.setTitle("알림"); //제목
            dlg.setMessage(tv_time+", "+tv_apAdrs+", "+tv_device+", "+tv_certified+" 데이터를 삭제하시겠습니까?"); // 메시지

            // 버튼 클릭시 동작
            dlg.setPositiveButton("확인",new DialogInterface.OnClickListener(){
                  public void onClick(DialogInterface dialog, int which) {
                        Log.d("[db] ", "delete Data ");
                  }
            });
            dlg.setNegativeButton("취소", null);

            dlg.show();
      }

       */
}


