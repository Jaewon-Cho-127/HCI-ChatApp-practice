package com.example.gpt_talk_2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MoreFragment extends Fragment {

    private LinearLayout linearLayout1;
    private LinearLayout linearLayout2;
    private LinearLayout linearLayout3;
    private LinearLayout linearLayout4;
    private LinearLayout linearLayout5;
    private LinearLayout linearLayout6;
    // 여러 개의 커스텀 버튼을 추가로 선언하실 수 있습니다.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        linearLayout1 = view.findViewById(R.id.linearLayout1);
        linearLayout2 = view.findViewById(R.id.linearLayout2);
        linearLayout3 = view.findViewById(R.id.linearLayout3);
        linearLayout4 = view.findViewById(R.id.linearLayout4);
        linearLayout5 = view.findViewById(R.id.linearLayout5);
        linearLayout6 = view.findViewById(R.id.linearLayout6);

        linearLayout1.setOnClickListener(new View.OnClickListener() {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.main_frame, new ChatFragment());
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.main_frame, new FriendFragment());
            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.main_frame, new SearchFragment());
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.main_frame, new PlusFragment());
            }
        });
        linearLayout5.setOnClickListener(new View.OnClickListener() {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            @Override
            public void onClick(View v) {
                transaction.replace(R.id.main_frame, new SettingFragment());
            }
        });
        linearLayout6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finishAffinity();
            }
        });

        return view;
    }
}

