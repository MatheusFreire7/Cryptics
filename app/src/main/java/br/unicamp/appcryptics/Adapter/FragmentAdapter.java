package br.unicamp.appcryptics.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import br.unicamp.appcryptics.Fragments.ChatsFragment;
import br.unicamp.appcryptics.Fragments.ConfigFragment;
import br.unicamp.appcryptics.Fragments.UsersFragment;

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
          case 1 : return new UsersFragment();
          case 2: return new ConfigFragment();
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
            title = "Usuarios";
        }
        if(position == 2)
        {
            title = "Perfil"; // Tela de configurações do perfil do User
        }
       return title;
    }
}
