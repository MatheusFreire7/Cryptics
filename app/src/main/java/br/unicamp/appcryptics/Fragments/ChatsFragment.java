package br.unicamp.appcryptics.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.unicamp.appcryptics.Adapter.UsersAdapter;
import br.unicamp.appcryptics.R;
import br.unicamp.appcryptics.Usuario;
import br.unicamp.appcryptics.databinding.FragmentChatsBinding;

public class ChatsFragment extends Fragment {

    public ChatsFragment() {
        // Required empty public constructor
    }

    FragmentChatsBinding binding;
    ArrayList<Usuario> listUsuario = new ArrayList<>();
    FirebaseDatabase database;
    String senderRoom = "";
    String id = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentChatsBinding.inflate(inflater,container,false);
        database = FirebaseDatabase.getInstance();

        UsersAdapter adapter = new UsersAdapter(listUsuario,getContext());
        binding.charRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.charRecyclerView.setLayoutManager(layoutManager);


//        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot)
//            {
//                listUsuario.clear();
//                String idAtual = FirebaseAuth.getInstance().getUid();
//                for(DataSnapshot dataSnapshot : snapshot.getChildren())
//                {
//                    Usuario users = dataSnapshot.getValue(Usuario.class);
//                    users.setUserId(dataSnapshot.getKey());
//                    id = dataSnapshot.getKey();
//                    senderRoom = idAtual + id;
//
//                    Query buscaConversa = database.getReference().child("chats").child(senderRoom);
//
//                    buscaConversa.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if(snapshot.exists())
//                            {
//                                Log.d("Usuario", users.getUsername());
//                                listUsuario.add(users); // adiciona a lista de usuários se a conversa foi encontrada
//                                Log.d("D", "Conversa encontrada");
//                                adapter.notifyDataSetChanged();
//                            }
//                            //else
//                            //{
////                                Log.d("IdAtual", idAtual);
////                                Log.d("SenderRoom", senderRoom);
////                                Log.d("Id", id);
////                                Log.d("D", "Conversa não encontrada");
//                            //}
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                }
//                int contador = adapter.getItemCount();
//                Log.d("Tamanho Lista", String.valueOf(contador));
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        return binding.getRoot();

    }

    @Override
    public void onStart() {
        super.onStart();

        UsersAdapter adapter = new UsersAdapter(listUsuario,getContext());
        binding.charRecyclerView.setAdapter(adapter);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                listUsuario.clear();
                String idAtual = FirebaseAuth.getInstance().getUid();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Usuario users = dataSnapshot.getValue(Usuario.class);
                    users.setUserId(dataSnapshot.getKey());
                    id = dataSnapshot.getKey();
                    senderRoom = idAtual + id;

                    Query buscaConversa = database.getReference().child("chats").child(senderRoom);

                    buscaConversa.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                Log.d("Usuario", users.getUsername());
                                listUsuario.add(users); // adiciona a lista de usuários se a conversa foi encontrada
                                Log.d("D", "Conversa encontrada");
                                adapter.notifyDataSetChanged();
                            }
                            //else
                            //{
//                                Log.d("IdAtual", idAtual);
//                                Log.d("SenderRoom", senderRoom);
//                                Log.d("Id", id);
//                                Log.d("D", "Conversa não encontrada");
                            //}
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                int contador = adapter.getItemCount();
                Log.d("Tamanho Lista", String.valueOf(contador));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}