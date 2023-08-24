package com.example.gpt_talk_2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
public class FriendFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private FriendAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new FriendAdapter(getFriendList());
        mRecyclerView.setAdapter(mAdapter);

        // 친구 목록 데이터를 어댑터에 설정
        List<Friend> friendList = getFriendList(); // 친구 목록 데이터를 가져오는 메서드
        mAdapter.setFriendList(friendList);

        setupRecyclerView();

        return view;
    }
    // 친구 목록 데이터를 가져오는 메서드
    private List<Friend> getFriendList() {
        List<Friend> friendList = new ArrayList<>();

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
    private void setupRecyclerView() {
        mAdapter.setOnItemClickListener(new FriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Friend friend, int itemId) {
                // 아이템 식별자에 따라 프래그먼트 전환을 처리합니다.
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
                transaction.addToBackStack(null); // 이전 프래그먼트로 돌아갈 수 있도록 백스택에 추가합니다.
                transaction.commit();
            }
        });
    }
    public void addFriend(Friend friend) {
        // 현재 친구 목록에 새로운 친구 추가
        mAdapter.getFriendList().add(friend);
        mAdapter.notifyDataSetChanged();
    }
    public void setFriendList(List<Friend> friendList) {
        if (mAdapter != null) {
            mAdapter.setFriendList(friendList);
        }
    }
    public void updateFriendList() {
        List<Friend> friendList = getFriendList();
        mAdapter.setFriendList(friendList);
    }
}