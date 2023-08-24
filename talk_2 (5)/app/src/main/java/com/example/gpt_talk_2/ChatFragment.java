package com.example.gpt_talk_2;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ChatAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // 채팅방 목록 데이터를 어댑터에 설정
        List<Chat> chatList = getChatList(); // 채팅방 목록 데이터를 가져오는 메서드
        mAdapter.setChatList(chatList);

        setupRecyclerView();

        return view;
    }

    // 채팅방 목록 데이터를 가져오는 메서드
    private List<Chat> getChatList() {
        List<Chat> chatList = new ArrayList<>();

        Chat chat1 = new Chat("이정우", "ㅋㅋㅋ");
        Chat chat2 = new Chat("조재원", "응");
        Chat chat3 = new Chat("이호진", "어제 보내준 보성녹차 잘 받았어");

        chatList.add(chat1);
        chatList.add(chat2);
        chatList.add(chat3);

        return chatList;
    }
    private void setupRecyclerView() {
        mAdapter.setOnItemClickListener(new ChatAdapter.OnItemClickListener() {
            AppCompatActivity activity = (AppCompatActivity) requireActivity();
            ActionBar actionBar = activity.getSupportActionBar();
            Intent intent = new Intent(getActivity().getApplicationContext(),EnterActivity.class);
            @Override
            public void onItemClick(Chat chat,int itemId) {
                switch (itemId) {
                    case 0:
                        intent.putExtra("chatname", "이정우");
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("chatname", "조재원");
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("chatname", "이호진");
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}