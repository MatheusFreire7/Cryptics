package br.unicamp.appcryptics.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import br.unicamp.appcryptics.API.RetrofitClient;
import br.unicamp.appcryptics.ConfigActivity;
import br.unicamp.appcryptics.EntraActivity3;
import br.unicamp.appcryptics.MainActivity;
import br.unicamp.appcryptics.R;
import br.unicamp.appcryptics.Usuario;
import br.unicamp.appcryptics.databinding.ActivityConfigBinding;
import br.unicamp.appcryptics.databinding.FragmentConfigBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConfigFragment extends Fragment {

    FragmentConfigBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    public ConfigFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentConfigBinding.inflate(getLayoutInflater());
        binding.getRoot();


        storage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    Usuario users = dataSnapshot.getValue(Usuario.class);
                    users.setUserId(dataSnapshot.getKey());

                    if(users.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                    {
                        if(dataSnapshot.child("username").exists())
                            binding.txtUsername.setText(dataSnapshot.child("username").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot2)
            {
                for(DataSnapshot dataSnapshot2 : snapshot2.getChildren())
                {
                    Usuario users = dataSnapshot2.getValue(Usuario.class);
                    users.setUserId(dataSnapshot2.getKey());

                    if(users.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                    {
                        if(dataSnapshot2.child("sobre").exists())
                            binding.txtSobre.setText(dataSnapshot2.child("sobre").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot3)
            {
                for(DataSnapshot dataSnapshot3 : snapshot3.getChildren())
                {
                    Usuario users = dataSnapshot3.getValue(Usuario.class);
                    users.setUserId(dataSnapshot3.getKey());

                    if(users.getUserId().equals(FirebaseAuth.getInstance().getUid()))
                    {
                        if(dataSnapshot3.child("email").exists())
                            binding.txtEmail.setText(dataSnapshot3.child("email").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        binding.btnAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.txtSobre.getText().toString() != " " && binding.txtUsername.getText().toString() != " " )
                {
                    String email = binding.txtEmail.getText().toString();
                    String emailAntigo = binding.txtEmailAntigo.getText().toString(); // chave primária do usuário
                    String sobre = binding.txtSobre.getText().toString();
                    String username = binding.txtUsername.getText().toString();
                    String senha = binding.txtSenha.getText().toString();


                    Usuario user = new Usuario(username,email,senha);
                    Call<Usuario> call = RetrofitClient
                            .getInstance()
                            .getAPI()
                            .alterarUser(emailAntigo,user);

                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            Usuario loginResponse = response.body();
                            Gson gson = new GsonBuilder().create();
                            String jsonRespostaNode = gson.toJson(loginResponse);
                            if(response.isSuccessful()){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setCancelable(true);
                                    builder.setTitle("Alteração do Usuário");
                                    builder.setMessage("A alteração do Usuário Foi realizado com sucessi");
                                    builder.show();

                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setCancelable(true);
                                builder.setTitle("Alteração do Usuário");
                                builder.setMessage("A alteração do Usuário não foi realizada");
                                builder.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setCancelable(true);
                            builder.setTitle("Alteração do Usuário");
                            builder.setMessage(t.getMessage().toString());
                            builder.show();
                        }
                    });

                    HashMap<String,Object> obj = new HashMap<>();
                    obj.put("email", email);
                    obj.put("username", username);
                    obj.put("senha", senha);
                    obj.put("sobre", sobre);

                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(obj);


                }
                binding.txtSenha.setText(""); // apaga a senha para funcionar como uma proteção
            }
        });

        database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Usuario users = snapshot.getValue(Usuario.class);
                        Picasso.get()
                                .load(users.getFotoPerfil())
                                .placeholder(R.drawable.avatar)
                                .into(binding.profileImage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        return binding.getRoot();
    }

}