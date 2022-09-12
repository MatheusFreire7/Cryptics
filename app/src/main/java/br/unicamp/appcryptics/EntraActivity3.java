package br.unicamp.appcryptics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.mbms.DownloadProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import br.unicamp.appcryptics.databinding.ActivityEntra3Binding;

public class EntraActivity3 extends AppCompatActivity {

    ActivityEntra3Binding binding;
    FirebaseDatabase firebaseDatabase;
    private FirebaseAuth mAuth;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entra3);
        binding = ActivityEntra3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        getSupportActionBar().hide();


        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(true)
                        .build())
                .build();

        binding.btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.txtEmail.getText().toString().isEmpty() && !binding.txtSenha.getText().toString().isEmpty())
                {
                    mAuth.signInWithEmailAndPassword(binding.txtEmail.getText().toString(), binding.txtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                Intent intent = new Intent(EntraActivity3.this, MainActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(EntraActivity3.this, "Conta Não Encontrada!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(EntraActivity3.this, "Preencha todos os Campos de Informações", Toast.LENGTH_LONG).show();
                }
            }
        });

        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(EntraActivity3.this,MainActivity.class);
            startActivity(intent);
        }

        binding.txtEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntraActivity3.this, CadastroActivity2.class);
                startActivity(intent);
            }
        });

    }

//    public class YourActivity extends AppCompatActivity {
//
//        // ...
//        private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
//        private boolean showOneTapUI = true;
//        // ...
//
//        @Override
//        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//            super.onActivityResult(requestCode, resultCode, data);
//
//            switch (requestCode) {
//                case REQ_ONE_TAP:
//                    try {
//                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
//                        String idToken = credential.getGoogleIdToken();
//                        if (idToken !=  null) {
//                            // Got an ID token from Google. Use it to authenticate
//                            // with Firebase.
//                            Log.d("TAG", "Got ID token.");
//                        }
//                    } catch (ApiException e) {
//                        // ...
//                    }
//                    break;
//            }
//        }
//    }
}