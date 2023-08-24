package com.example.gpt_talk_2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.gpt_talk_2.EnterActivity;
import com.example.gpt_talk_2.FriendFragment;
import com.example.gpt_talk_2.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile4Fragment extends Fragment {
    private Button Button2;
    private Button Button3;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile4, container, false);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);

        Button2 = view.findViewById(R.id.button2);
        Button3 = view.findViewById(R.id.button3);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        bottomNavigationView.setVisibility(View.GONE);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        ActionBar actionBar = activity.getSupportActionBar();

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);

        Button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                FriendFragment friendFragment = new FriendFragment();
                fragmentTransaction.replace(R.id.main_frame, friendFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                bottomNavigationView.setVisibility(View.VISIBLE);
            }
        });
        Button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EnterActivity.class);
                intent.putExtra("chatname", "이학진");
                startActivity(intent);
            }
        });
    }
}