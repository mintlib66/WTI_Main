package cnu.proejct.WTIsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

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

}


