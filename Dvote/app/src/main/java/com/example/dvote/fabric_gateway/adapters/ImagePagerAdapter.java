package com.example.dvote.fabric_gateway.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dvote.fabric_gateway.fragments.ScreenSlidePageFragment;

import java.util.List;

public class ImagePagerAdapter extends FragmentStateAdapter {

    private List<Integer> imageResIds;

    public ImagePagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Integer> imageResIds) {
        super(fragmentActivity);
        this.imageResIds = imageResIds;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ScreenSlidePageFragment.newInstance(imageResIds.get(position));
    }

    @Override
    public int getItemCount() {
        return imageResIds.size();
    }
}
