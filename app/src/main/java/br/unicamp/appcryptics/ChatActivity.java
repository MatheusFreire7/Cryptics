package br.unicamp.appcryptics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

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

import java.util.ArrayList;
import java.util.Date;

import br.unicamp.appcryptics.Adapter.ChatAdapter;
import br.unicamp.appcryptics.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

   ActivityChatBinding binding;
   FirebaseDatabase database;
   FirebaseAuth auth;
   FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        final String enviaId = auth.getUid();
        String recebeId = getIntent().getStringExtra("userId");
        String Username = getIntent().getStringExtra("Username");
        String fotoPerfil = getIntent().getStringExtra("fotoPerfil");

        binding.userName.setText(Username);
        Picasso.get().load(fotoPerfil).placeholder(R.drawable.avatar).into(binding.profileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final ArrayList<MessageModel> listaMensagem = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(listaMensagem,this,recebeId);

        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom = enviaId + recebeId;
        final String receiverRoom = recebeId + enviaId;

        database.getReference().child("chats")
                        .child(senderRoom)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            listaMensagem.clear();
                                            for(DataSnapshot snapshot1 : snapshot.getChildren())
                                            {
                                                MessageModel model = snapshot1.getValue(MessageModel.class);
                                                model.setMessageId(snapshot1.getKey());
                                                listaMensagem.add(model);
                                            }
                                            chatAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

        binding.btnSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,250);
            }
        });

        binding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String menssagem = binding.digiteMessagem.getText().toString();
                final MessageModel model = new MessageModel(enviaId,menssagem);
                model.setDataMensagem(new Date().getTime());
                binding.digiteMessagem.setText("");

                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {

                                            }
                                        });
                            }
                        });
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final String enviaId = auth.getUid();
        String recebeId = getIntent().getStringExtra("userId");

        final String senderRoom = enviaId + recebeId;
        final String receiverRoom = recebeId + enviaId;


        binding.digiteMessagem.setText("");

        if(data.getData()!= null)
        {
            Uri sFile = data.getData();
            String menssagem = binding.digiteMessagem.getText().toString();


            final StorageReference reference = storage.getReference().child("image")
                    .child(FirebaseAuth.getInstance().getUid());

            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            final MessageModel model = new MessageModel(enviaId,menssagem,uri.toString());
                            model.setDataMensagem(new Date().getTime());
                            database.getReference().child("chats").child(FirebaseAuth.getInstance().getUid())
                                    .child(senderRoom)
                                    .push()
                                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            database.getReference().child("chats")
                                                    .child(receiverRoom)
                                                    .push()
                                                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                        }
                                                    });
                                        }
                                    });
                        }
                         });

                        }
                    });
        }
    }
}