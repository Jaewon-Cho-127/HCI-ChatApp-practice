package com.example.gpt_talk_2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<Chat> mChatList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Chat chat,int itemId);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setChatList(List<Chat> chatList) {
        mChatList = chatList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Chat chat = mChatList.get(position);

        // 아이템 뷰에 채팅방 정보 설정
        holder.nameTextView.setText(chat.getName());
        holder.messageTextView.setText(chat.getMessage());
    }

    @Override
    public int getItemCount() {
        return mChatList != null ? mChatList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView messageTextView;
        LinearLayout itemLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            nameTextView = itemView.findViewById(R.id.name);
            messageTextView = itemView.findViewById(R.id.message);

            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Chat chat = mChatList.get(position);
                        if (listener != null) {
                            switch (position) {
                                case 0:
                                    listener.onItemClick(chat, 0); // Pass an identifier for the 1 item
                                    break;
                                case 1:
                                    listener.onItemClick(chat, 1); // Pass an identifier for the 2 item
                                    break;
                                case 2:
                                    listener.onItemClick(chat, 2); // Pass an identifier for the 3 item
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
