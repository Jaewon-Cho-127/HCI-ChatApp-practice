package com.example.gpt_talk_2;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.gpt_talk_2.databinding.ActivityMain2Binding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    private List<Friend> friendList;
    private FriendFragment friendFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        friendList = new ArrayList<>();
        friendFragment = new FriendFragment();

        FriendFragment friendFragment = new FriendFragment();
        friendFragment.setFriendList(friendList);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("친구"); // 원하는 제목으로 변경

        //처음화면
        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new StartFragment()).commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_item1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FriendFragment()).commit();
                        setTitle("친구");
                        return true;
                    case R.id.navigation_item2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new ChatFragment()).commit();
                        setTitle("채팅방");
                        return true;
                    case R.id.navigation_item3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MoreFragment()).commit();
                        setTitle("더보기");
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        switch (item.getItemId()){
            case android.R.id.home:{
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FriendFragment()).commit();
                setTitle("친구");
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                bottomNavigationView.setVisibility(View.VISIBLE);
                return true;
            }
        }

        if (id == R.id.search) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new SearchFragment()).commit();
            setTitle("친구 검색");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            return true;
        } else if (id == R.id.plus) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new PlusFragment()).commit();
            return true;
        } else if (id == R.id.setting) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new SettingFragment()).commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // FriendFragment에 새로운 친구 추가
    public void addFriend(Friend friend) {
        friendList.add(friend);
        if (friendFragment != null) {
            friendFragment.updateFriendList();
        }
    }
}
