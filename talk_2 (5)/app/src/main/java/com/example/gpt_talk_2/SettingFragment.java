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

public class SettingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private SettingAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new SettingAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // 친구 목록 데이터를 어댑터에 설정
        List<Setting> settingList = getSettingList(); // 친구 목록 데이터를 가져오는 메서드
        mAdapter.setSettingList(settingList);

        return view;
    }
    // 환경설정 목록 데이터를 가져오는 메서드
    private List<Setting> getSettingList() {
        List<Setting> settingList = new ArrayList<>();

        Setting setting1 = new Setting("앱 정보");
        Setting setting2 = new Setting("업데이트");
        Setting setting3 = new Setting("알림");
        Setting setting4 = new Setting("화면");
        Setting setting5 = new Setting("테마");
        Setting setting6 = new Setting("채팅");
        Setting setting7 = new Setting("공지사항");
        Setting setting8 = new Setting("가이드");
        Setting setting9 = new Setting("기타");
        Setting setting10 = new Setting("기타");


        settingList.add(setting1);
        settingList.add(setting2);
        settingList.add(setting3);
        settingList.add(setting4);
        settingList.add(setting5);
        settingList.add(setting6);
        settingList.add(setting7);
        settingList.add(setting8);
        settingList.add(setting9);
        settingList.add(setting10);

        return settingList;
    }
}