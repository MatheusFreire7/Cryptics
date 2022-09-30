package br.unicamp.appcryptics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import br.unicamp.appcryptics.Adapter.ChatAdapter;
import br.unicamp.appcryptics.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

   ActivityChatBinding binding;
   FirebaseDatabase database;
   FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

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

        binding.imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mensagem = binding.digiteMessagem.getText().toString();
                final MessageModel model = new MessageModel(enviaId,mensagem);
                model.setTimeStamp(new Date().getTime());
                binding.digiteMessagem.setText("");

                database.getReference().child("chats")
                        .child(senderRoom)
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                database.getReference().child("chats")
                                        .child(receiverRoom)
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
}