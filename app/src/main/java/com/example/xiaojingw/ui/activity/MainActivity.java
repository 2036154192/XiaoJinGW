package com.example.xiaojingw.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.xiaojingw.R;
import com.example.xiaojingw.base.BaseActivity;
import com.example.xiaojingw.base.BaseFragment;
import com.example.xiaojingw.ui.fragment.HomeFragment;
import com.example.xiaojingw.ui.fragment.RedPacketFragment;
import com.example.xiaojingw.ui.fragment.SearchFragment;
import com.example.xiaojingw.ui.fragment.SelectedFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements IMainActivity{


    private static final String TAG = "MainActivity";
    private HomeFragment homeFragment;
    private RedPacketFragment redPacketFragment;
    private SearchFragment searchFragment;
    private SelectedFragment selectedFragment;
    private FragmentManager fragmentManager;
    private BottomNavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragments();
        initListener();
    }

    //跳转到搜索界面
    @Override
    public void switch2Search(){
        //切换页面
        //切换tab
        mNavigationView.setSelectedItemId(R.id.search);
    }

    private void initFragments() {
        homeFragment = new HomeFragment();
        redPacketFragment = new RedPacketFragment();
        searchFragment = new SearchFragment();
        selectedFragment = new SelectedFragment();
        mNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentManager = getSupportFragmentManager();
        switchFragment(homeFragment);
    }

    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d(TAG, "item_-------: "+item.getItemId());
                if (item.getItemId() == R.id.homes){
                    switchFragment(homeFragment);
                }else if (item.getItemId() == R.id.selecteds){
                    switchFragment(selectedFragment);
                }else if (item.getItemId() == R.id.red_packet){
                    switchFragment(redPacketFragment);
                }else {
                    switchFragment(searchFragment);
                }
                return true;
            }
        });
    }

    private BaseFragment lastOneFragment = null;

    private void switchFragment(BaseFragment targetFragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (!targetFragment.isAdded()){
            transaction.add(R.id.main_page_container,targetFragment);
        }else {
            transaction.show(targetFragment);
        }
        if (lastOneFragment != null && lastOneFragment != targetFragment){
            transaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
        transaction.commit();
    }

}