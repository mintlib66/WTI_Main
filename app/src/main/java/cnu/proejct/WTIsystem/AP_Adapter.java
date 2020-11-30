package cnu.proejct.WTIsystem;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AP_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
      public static final int HEADER = 0;
      public static final int CHILD = 1;

      //private ArrayList<Device> items = new ArrayList<Device>();

      static Context mContext;
      private static List<Item> data;

      //생성자
      public AP_Adapter(Context mContext, List<Item> data) {
            this.mContext = mContext;
            this.data = data;
      }

      //뷰홀더 생성
      @NonNull
      @Override
      public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.ap_item, parent, false);

            View view = null;
            Context context = parent.getContext();
            switch (viewType) {
                  case HEADER:
                        LayoutInflater inflaterHeader = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        view = inflaterHeader.inflate(R.layout.ap_header, parent, false);
                        ListHeaderViewHolder header = new ListHeaderViewHolder(view);
                        return header;
                  case CHILD:
                        LayoutInflater inflaterChild = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        view = inflaterChild.inflate(R.layout.ap_item, parent, false);
                        ListChildViewHolder child = new ListChildViewHolder(view);
                        return child;

            }
            return null;
      }

      //뷰홀더 바인드 -- 리스트에 실질적 값 배정
      @Override
      public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            final Item item = data.get(position);
            switch (item.type) {
                  case HEADER:
                        final ListHeaderViewHolder itemController = (ListHeaderViewHolder) holder;
                        itemController.refferalItem = item;
                        itemController.header_title.setText(item.getTimeTitle());
                        if (item.invisibleChildren == null) {
                              itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                        } else {
                              itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                        }
                        itemController.btn_expand_toggle.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                    if (item.invisibleChildren == null) {
                                          item.invisibleChildren = new ArrayList<Item>();
                                          int count = 0;
                                          int pos = data.indexOf(itemController.refferalItem);
                                          while (data.size() > pos + 1 && data.get(pos + 1).type == CHILD) {
                                                item.invisibleChildren.add(data.remove(pos + 1));
                                                count++;
                                          }
                                          notifyItemRangeRemoved(pos + 1, count);
                                          itemController.btn_expand_toggle.setImageResource(R.drawable.circle_plus);
                                    } else {
                                          int pos = data.indexOf(itemController.refferalItem);
                                          int index = pos + 1;
                                          for (Item i : item.invisibleChildren) {
                                                data.add(index, i);
                                                index++;
                                          }
                                          notifyItemRangeInserted(pos + 1, index - pos - 1);
                                          itemController.btn_expand_toggle.setImageResource(R.drawable.circle_minus);
                                          item.invisibleChildren = null;
                                    }
                              }
                        });
                        break;
                  case CHILD:
                        final ListChildViewHolder itemController1 = (ListChildViewHolder) holder;
                        itemController1.refferalItem = item;
                        itemController1.child_timestamp.setText(item.itemDevice.getTimestamp());
                        itemController1.child_apAddress.setText(item.itemDevice.getApAddress());
                        itemController1.child_deviceMAC.setText(item.itemDevice.getDeviceMAC());
                        itemController1.child_IsCertified.setText(item.itemDevice.getIsCertified());
                        break;
            }
      }

      @Override
      public int getItemViewType(int position) {
            return data.get(position).type;
      }

      //리스트에 들어있는 아이템 개수 세기
      @Override
      public int getItemCount() {
            return data.size();
      }

      //뷰홀더 클래스 - header
      private static class ListHeaderViewHolder extends RecyclerView.ViewHolder {
            public TextView header_title;
            public ImageView btn_expand_toggle;
            public Item refferalItem;

            public ListHeaderViewHolder(View itemView) {
                  super(itemView);
                  header_title = (TextView) itemView.findViewById(R.id.header_title);
                  btn_expand_toggle = (ImageView) itemView.findViewById(R.id.btn_expand_toggle);
            }
      }
      //뷰홀더 클래스 - child
      private static class ListChildViewHolder extends RecyclerView.ViewHolder {
            public TextView child_timestamp;
            public TextView child_apAddress;
            public TextView child_deviceMAC;
            public TextView child_IsCertified;
            public Item refferalItem;

            public ListChildViewHolder(View itemView) {
                  super(itemView);
                  child_timestamp = (TextView) itemView.findViewById(R.id.tv_time);
                  child_apAddress = (TextView) itemView.findViewById(R.id.tv_apAdrs);
                  child_deviceMAC = (TextView) itemView.findViewById(R.id.tv_device);
                  child_IsCertified = (TextView) itemView.findViewById(R.id.tv_certified);

                  //child에만 intent 클릭이벤트
                  itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                              int pos = getAdapterPosition();
                              if (pos != RecyclerView.NO_POSITION){
                                    itemClickEvent(mContext, pos);
                                    Log.d("RecyclerView click", "position = " + pos);
                              }
                        }
                  });
            }
      }

      //리스트 child 아이템 전용 - intent 클릭이벤트용 리스너
      public static void itemClickEvent(Context mContext, int pos){
            //클릭이벤트 : 클릭 시 다음화면으로 넘어가고 값넘김
            Device devInfo = AP_Adapter.data.get(pos).getItemDevice();

            Intent intent = new Intent(mContext, Device_Result.class);
            intent.putExtra("tv_time", devInfo.getTimestamp());
            intent.putExtra("tv_apAdrs", devInfo.getApAddress());
            intent.putExtra("tv_device", devInfo.getDeviceMAC());
            intent.putExtra("tv_certified", devInfo.getIsCertified());
            mContext.startActivity(intent);


      }

      //Item 클래스
      public static class Item {
            public int type; //header or child
            public Device itemDevice; // timestamp
            public List<Item> invisibleChildren;

            public Item() {
            }

            public Item(int type, Device itemDevice) {
                  this.type = type;
                  this.itemDevice = itemDevice;
            }

            public void setItemDevice(Device itemDevice){
                  this.itemDevice = itemDevice;
            }
            public String getTimeTitle(){
                  return itemDevice.getTimestamp();
            }
            public Device getItemDevice(){
                  return itemDevice;
            }
      }


}


