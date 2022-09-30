package br.unicamp.appcryptics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

import org.jetbrains.annotations.Nullable;

import br.unicamp.appcryptics.databinding.ActivityCadastro2Binding;
import br.unicamp.appcryptics.databinding.ActivityEntra3Binding;

public class CadastroActivity2 extends AppCompatActivity{
    ActivityCadastro2Binding binding;  // biblioteca que permite vincular componentes do layout
    private FirebaseAuth mAuth;        // Autenticação do Firebase
    FirebaseDatabase firebaseDatabase; // Banco de dados do Firebase
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    //private  DatabaseReference reference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro2);
        binding =  ActivityCadastro2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        gsc = GoogleSignIn.getClient(this, gso);
//
//        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SignIn();
//            }
//
//        });


        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();


        getSupportActionBar().hide();

        binding.btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.txtEmail.getText().toString().isEmpty() && !binding.txtUsername.getText().toString().isEmpty() && !binding.txtSenha.getText().toString().isEmpty())
                {
                    Usuario user = new Usuario();
                    user.setUsername(binding.txtUsername.getText().toString());
                    user.setEmail(binding.txtEmail.getText().toString());
                    user.setSenha(binding.txtSenha.getText().toString());
                    mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(), binding.txtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) // se conseguiu cadastrar
                            {
                                String id = mAuth.getUid();
                                //DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                //DatabaseReference usuarios = FirebaseDatabase.getInstance().getReference();
                                // = usuarios.child("Users");
                                user.setUserId(id);
                                firebaseDatabase.getReference().child("Users").child(id).setValue(user);
//                              usuarios.setValue(user);
//
//                                reference.push();
                                Toast.makeText(CadastroActivity2.this, "Cadastrado com Sucesso!", Toast.LENGTH_LONG).show();
                            }
                            else // senão conseguiu cadastrar
                            {
                                Toast.makeText(CadastroActivity2.this, "E-mail já cadastrado", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else // se o usuario não preencheu todas as informações irá retornar esta mesnsagem
                {
                    Toast.makeText(CadastroActivity2.this, "Preencha todos os Campos para se Cadastrar", Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.txtExisteConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroActivity2.this, EntraActivity3.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtUsername.setText("");
                binding.txtEmail.setText("");
                binding.txtSenha.setText("");
            }
        });

    }

//    private void SignIn() {
//        Intent intent = gsc.getSignInIntent();
//        startActivityForResult(intent,100);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
//        super.onActivityResult(requestCode,resultCode,data);
//
//        if(requestCode == 100){
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try{
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                Log.d("TAG", "firebaseAuthWithGoogle" + account.getId());
//                firebaseAuthWithGoogle(account.getIdToken());
//
//            } catch(ApiException e) {
//                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    private void firebaseAuthWithGoogle(String idToken){
//        if (idToken !=  null) {
//            // Got an ID token from Google. Use it to authenticate
//            // with Firebase.
//            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
//            mAuth.signInWithCredential(firebaseCredential)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            if (task.isSuccessful()) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Log.d("TAG", "signInWithCredential:success");
//                                FirebaseUser user = mAuth.getCurrentUser();
//
//                                Intent intent = new Intent(CadastroActivity2.this, MainActivity.class);
//                                startActivity(intent);
//
//                                Toast.makeText(CadastroActivity2.this, "Cadastro com Google", Toast.LENGTH_SHORT).show();
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Log.w("TAG", "signInWithCredential:failure", task.getException());
//                            }
//                        }
//                    });
//        }
//    }
//

}

