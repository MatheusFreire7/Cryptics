package br.unicamp.appcryptics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

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

    }
}