package com.learning.whatsapp.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.learning.whatsapp.Fragments.CallFragment;
import com.learning.whatsapp.Fragments.ChatFragment;
import com.learning.whatsapp.Fragments.StatusFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {            //<------------(1) create fragmentAdapter
    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {                         //<-------------(2)
        switch(position){
            case 0: return  new ChatFragment();
            case 1: return  new StatusFragment();
            case 2: return  new CallFragment();
            default: return new ChatFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {                                  //<-------------(3)
                                                                                        //Set Title of the fragment
        String title = null;
        if(position==0){
            title = "CHATS";
        }
        if(position==1){
            title = "STATUS";
        }
        if(position==2){
            title = "CALLS";
        }
        
        return title;
    }
}
