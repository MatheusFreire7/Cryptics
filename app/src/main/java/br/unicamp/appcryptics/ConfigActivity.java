package br.unicamp.appcryptics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import br.unicamp.appcryptics.databinding.ActivityConfigBinding;

public class ConfigActivity extends AppCompatActivity {

    ActivityConfigBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfigBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

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

        binding.Imgvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        binding.btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.txtSobre.getText().toString() != " " && binding.txtUsername.getText().toString() != " " )
                {
                    String sobre = binding.txtSobre.getText().toString();
                    String username = binding.txtUsername.getText().toString();

                    HashMap<String,Object> obj = new HashMap<>();
                    obj.put("username", username);
                    obj.put("sobre", sobre);

                    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                            .updateChildren(obj);

                    Toast.makeText(ConfigActivity.this, "Foi salvo com sucesso", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(ConfigActivity.this, "Preencha ambos os Campos", Toast.LENGTH_SHORT).show();

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

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,250);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData()!= null)
        {
            Uri sFile = data.getData();
            binding.profileImage.setImageURI(sFile);

            final StorageReference reference = storage.getReference().child("fotoPerfil")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("fotoPerfil").setValue(uri.toString());

                        }
                    });
                }
            });
        }
    }
}