package com.example.gpt_talk_2;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {

    private List<Friend> mFriendList;
    private List<Friend> mFilteredList;
    public FriendAdapter(List<Friend> friendList) {
        mFriendList = friendList;
        mFilteredList = new ArrayList<>(friendList);
    }
    private FriendAdapter.OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Friend friend,int itemId);
    }
    public void setOnItemClickListener(FriendAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setFriendList(List<Friend> friendList) {
        mFriendList = friendList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Friend friend = mFriendList.get(position);

        // 아이템 뷰에 친구 정보 설정
        holder.name.setText(friend.getName());
        holder.message.setText(friend.getMessage());
    }

    @Override
    public int getItemCount() {
        return mFriendList != null ? mFriendList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView message;
        LinearLayout itemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Friend friend = mFriendList.get(position);
                        if (listener != null) {
                            switch (position) {
                                case 0:
                                    listener.onItemClick(friend, 0); // Pass an identifier for the 1 item
                                    break;
                                case 1:
                                    listener.onItemClick(friend, 1); // Pass an identifier for the 2 item
                                    break;
                                case 2:
                                    listener.onItemClick(friend, 2); // Pass an identifier for the 3 item
                                    break;
                                case 3:
                                    listener.onItemClick(friend, 3); // Pass an identifier for the 4 item
                                    break;
                                case 4:
                                    listener.onItemClick(friend, 4); // Pass an identifier for the 5 item
                                    break;
                                case 5:
                                    listener.onItemClick(friend, 5); // Pass an identifier for the 6 item
                                    break;
                                default:
                                    listener.onItemClick(friend, 0); // Pass 0 or any default identifier for other items
                                    break;
                            }
                        }
                    }
                }
            });

        }
    }
    public List<Friend> getFriendList() {
        return mFriendList;
    }
}
