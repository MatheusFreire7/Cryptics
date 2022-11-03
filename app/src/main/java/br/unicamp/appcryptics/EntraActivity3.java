package br.unicamp.appcryptics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.Nullable;

import br.unicamp.appcryptics.API.LoginResponse;
import br.unicamp.appcryptics.API.RetrofitClient;
import br.unicamp.appcryptics.databinding.ActivityEntra3Binding;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntraActivity3 extends AppCompatActivity {

    ActivityEntra3Binding binding;       // biblioteca que permite vincular componentes do layout
    FirebaseDatabase firebaseDatabase;   // Banco de dados do Firebase
    private FirebaseAuth mAuth;          // Autenticação do Firebase
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entra3);
        binding = ActivityEntra3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        getSupportActionBar().hide();

        binding.btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.txtEmail.getText().toString().isEmpty() && !binding.txtSenha.getText().toString().isEmpty())
                {
                    String email = binding.txtEmail.getText().toString().trim();
                    String senha = binding.txtSenha.getText().toString().trim();
                    Usuario user = new Usuario(email,senha);
                    Call<Usuario> call = RetrofitClient
                            .getInstance()
                            .getAPI()
                            .loginUser(user);

                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            Usuario loginResponse = response.body();
                            Gson gson = new GsonBuilder().create();
                            String jsonRespostaNode = gson.toJson(loginResponse);
                            if(response.isSuccessful()){
                                mAuth.signInWithEmailAndPassword(binding.txtEmail.getText().toString(), binding.txtSenha.getText().toString());
                                Intent intent = new Intent(EntraActivity3.this, MainActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(EntraActivity3.this, "Usuário não encontrado", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Toast.makeText(EntraActivity3.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else
                {
                    Toast.makeText(EntraActivity3.this, "Preencha todos os Campos de Informações", Toast.LENGTH_LONG).show();
                }
            }
        });

//        if(mAuth.getCurrentUser()!=null)
//        {
//            Intent intent = new Intent(EntraActivity3.this,MainActivity.class);
//            startActivity(intent);
//        }

        binding.txtEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EntraActivity3.this, CadastroActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtEmail.setText("");
                binding.txtSenha.setText("");
            }
        });

        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

    }

    private void userLogin() {
        String email = binding.txtEmail.getText().toString().trim();
        String senha = binding.txtSenha.getText().toString().trim();
        Usuario user = new Usuario(email,senha);
        Call<Usuario> call = RetrofitClient
                .getInstance()
                .getAPI()
                .loginUser(user);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Usuario loginResponse = response.body();
                Gson gson = new GsonBuilder().create();
                String jsonRespostaNode = gson.toJson(loginResponse);
                if(response.isSuccessful()){
                    Intent intent = new Intent(EntraActivity3.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(EntraActivity3.this, "Usuário não encontrado", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(EntraActivity3.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void SignIn() {
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent,100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 100){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());

            } catch(ApiException e) {
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken){
        if (idToken !=  null) {
            // Got an ID token from Google. Use it to authenticate
            // with Firebase.
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Intent intent = new Intent(EntraActivity3.this, MainActivity.class);
                                startActivity(intent);

                                Toast.makeText(EntraActivity3.this, "Login com Google realizado com sucesso", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithCredential:failure", task.getException());
                            }
                        }
                    });
        }
    }


}