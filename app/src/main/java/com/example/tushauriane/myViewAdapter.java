package com.example.tushauriane;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class myViewAdapter extends FragmentStateAdapter {

    public myViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){

            default:
                return new Community_fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

