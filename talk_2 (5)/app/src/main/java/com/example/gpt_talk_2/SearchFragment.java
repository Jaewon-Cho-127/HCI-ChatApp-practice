package com.example.gpt_talk_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private EditText searchEditText;
    private RecyclerView searchRecyclerView;
    private FriendAdapter searchAdapter;
    private List<Friend> friendList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchEditText = view.findViewById(R.id.searchEditText);
        searchRecyclerView = view.findViewById(R.id.searchRecyclerView);

        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        friendList = getFriendList(); // 친구 목록 데이터를 가져오는 메서드

        searchAdapter = new FriendAdapter(friendList);
        searchRecyclerView.setAdapter(searchAdapter);

        setupSearch();
        setupItemClick();

        return view;
    }
    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                filterFriendList(query);
            }
        });
    }
    private void filterFriendList(String query) {
        List<Friend> filteredList = new ArrayList<>();
        for (Friend friend : friendList) {
            if (friend.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(friend);
            }
        }
        searchAdapter.setFriendList(filteredList);
    }

    private void setupItemClick() {
        searchAdapter.setOnItemClickListener(new FriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Friend friend, int itemId) {
                // 아이템 클릭 이벤트 처리
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                switch (itemId) {
                    case 0:
                        transaction.replace(R.id.main_frame, new Profile1Fragment());
                        break;
                    case 1:
                        transaction.replace(R.id.main_frame, new Profile2Fragment());
                        break;
                    case 2:
                        transaction.replace(R.id.main_frame, new Profile3Fragment());
                        break;
                    case 3:
                        transaction.replace(R.id.main_frame, new Profile4Fragment());
                        break;
                    case 4:
                        transaction.replace(R.id.main_frame, new Profile5Fragment());
                        break;
                    case 5:
                        transaction.replace(R.id.main_frame, new Profile6Fragment());
                        break;
                    case 6:
                        transaction.replace(R.id.main_frame, new Profile7Fragment());
                        break;
                    case 7:
                        transaction.replace(R.id.main_frame, new Profile8Fragment());
                        break;
                    case 8:
                        transaction.replace(R.id.main_frame, new Profile9Fragment());
                        break;
                    default:
                        break;
                }
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

    // 친구 목록 데이터를 가져오는 메서드
    private List<Friend> getFriendList() {
        List<Friend> friendList = new ArrayList<>();

        // Friend 객체 추가 작업
        Friend friend1 = new Friend("이정우", "빨리 종강하고싶다");
        Friend friend2 = new Friend("이호진", "도망가자 - 선우정아 ");
        Friend friend3 = new Friend("조재원", "집 가고싶다");
        Friend friend4 = new Friend("이학진", "나는 잘생겼다");
        Friend friend5 = new Friend("김보성", "보성녹차");
        Friend friend6 = new Friend("김태영", "천하를 가진 남자 ");
        Friend friend7 = new Friend("권상우", "3AM(Prod.AndyWave");
        Friend friend8 = new Friend("이정민", "멍청이");
        Friend friend9 = new Friend("김한솔", "바보");

        friendList.add(friend1);
        friendList.add(friend2);
        friendList.add(friend3);
        friendList.add(friend4);
        friendList.add(friend5);
        friendList.add(friend6);
        friendList.add(friend7);
        friendList.add(friend8);
        friendList.add(friend9);

        return friendList;
    }
}



