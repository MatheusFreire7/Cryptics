package br.unicamp.appcryptics.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.unicamp.appcryptics.Adapter.UsersAdapter;
import br.unicamp.appcryptics.R;
import br.unicamp.appcryptics.Usuario;
import br.unicamp.appcryptics.databinding.FragmentChatsBinding;
import br.unicamp.appcryptics.databinding.FragmentUsersBinding;


public class UsersFragment extends Fragment {


    public UsersFragment() {

    }

    FragmentUsersBinding binding;
    ArrayList<Usuario> listUsuario = new ArrayList<>();
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater,container,false);
        database = FirebaseDatabase.getInstance();

        UsersAdapter adapter = new UsersAdapter(listUsuario,getContext());
        binding.charRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.charRecyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                listUsuario.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Usuario users = dataSnapshot.getValue(Usuario.class);
                    users.setUserId(dataSnapshot.getKey());

                    if(!users.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                    {
                        listUsuario.add(users);
                    }
                }
                adapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();


    }
}