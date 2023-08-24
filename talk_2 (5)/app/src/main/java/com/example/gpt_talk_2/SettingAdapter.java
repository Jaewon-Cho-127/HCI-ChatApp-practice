package com.example.gpt_talk_2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    private List<Setting> mSettingList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Setting setting,int itemId);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSettingList(List<Setting> settingList) {
        mSettingList = settingList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Setting setting = mSettingList.get(position);

        // 아이템 뷰에 채팅방 정보 설정
        holder.nameTextView.setText(setting.getName());
    }

    @Override
    public int getItemCount() {
        return mSettingList != null ? mSettingList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        LinearLayout itemLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            nameTextView = itemView.findViewById(R.id.name);

            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Setting setting = mSettingList.get(position);
                        if (listener != null) {
                            switch (position) {
                                case 0:
                                    listener.onItemClick(setting, 0); // Pass an identifier for the 1 item
                                    break;
                                case 1:
                                    listener.onItemClick(setting, 1); // Pass an identifier for the 2 item
                                    break;
                                case 2:
                                    listener.onItemClick(setting, 2); // Pass an identifier for the 3 item
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            });
        }
    }
}

