package br.unicamp.appcryptics.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import br.unicamp.appcryptics.Fragments.ChatsFragment;
import br.unicamp.appcryptics.Fragments.StatusFragment;

public class FragmentAdapter extends FragmentPagerAdapter
{

    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public Fragment getItem(int position)
    {
      switch (position)
      {
          case 0 : return new ChatsFragment();
          case 1 : return new StatusFragment();
          default:return new ChatsFragment();
      }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title = null;
        if(position == 0)
        {
            title = "Conversas";
        }
        if(position == 1)
        {
            title = "Status";
        }
       return title;
    }
}
