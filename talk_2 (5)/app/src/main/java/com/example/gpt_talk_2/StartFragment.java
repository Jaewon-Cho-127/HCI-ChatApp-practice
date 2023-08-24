package com.example.gpt_talk_2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
public class StartFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_start, container, false);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);

        Button button = rootView.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 두 번째 프래그먼트로 이동하는 코드 작성
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                FriendFragment friendFragment = new FriendFragment();
                fragmentTransaction.replace(R.id.main_frame, friendFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                // 시작 버튼 누르면 툴바와 바텀 네비게이션 보임
                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        // 이 프래그먼트에서 툴바와 바텀 네비게이션 숨김
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        bottomNavigationView.setVisibility(View.GONE);

        return rootView;
    }
}