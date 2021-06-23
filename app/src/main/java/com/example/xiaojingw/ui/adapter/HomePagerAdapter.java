package com.example.xiaojingw.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.xiaojingw.model.doment.Category;
import com.example.xiaojingw.ui.fragment.HomePacketFragment;

import java.util.ArrayList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private List<Category.DataBean> categorys = new ArrayList<>();

    public HomePagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return categorys.get(position).getTitle();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Category.DataBean dataBean = categorys.get(position);
        HomePacketFragment homePacketFragment = HomePacketFragment.newInstance(dataBean);
        return homePacketFragment;
    }

    @Override
    public int getCount() {
        return categorys.size();
    }

    public void setTitleData(Category category) {
        categorys.clear();
        List<Category.DataBean> data = category.getData();
        categorys.addAll(data);
        notifyDataSetChanged();
    }
}
