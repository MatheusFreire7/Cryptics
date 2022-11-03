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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.unicamp.appcryptics.API.RetrofitClient;
import br.unicamp.appcryptics.databinding.ActivityCadastro2Binding;
import br.unicamp.appcryptics.databinding.ActivityEntra3Binding;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastroActivity2 extends AppCompatActivity{
    ActivityCadastro2Binding binding;  // biblioteca que permite vincular componentes do layout
    private FirebaseAuth mAuth;        // Autenticação do Firebase
    FirebaseDatabase firebaseDatabase; // Banco de dados do Firebase
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    private  DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro2);
        binding = ActivityCadastro2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        getSupportActionBar().hide();

        binding.btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Validações
                if(binding.txtEmail.getText().toString().isEmpty())
                    Toast.makeText(CadastroActivity2.this, "Preencha o campo e-mail", Toast.LENGTH_SHORT).show();

                if(!validarEmail(binding.txtEmail.getText().toString()))
                {
                    Toast.makeText(CadastroActivity2.this, "Digite um e-mail válido", Toast.LENGTH_SHORT).show();
                }

                if(binding.txtUsername.getText().toString().isEmpty())
                    Toast.makeText(CadastroActivity2.this, "Preencha o campo Username", Toast.LENGTH_SHORT).show();

                if(binding.txtSenha.getText().toString().isEmpty())
                    Toast.makeText(CadastroActivity2.this, "Preencha o campo senha", Toast.LENGTH_SHORT).show();

                if(binding.txtSenha.getText().toString().length() < 6)
                    Toast.makeText(CadastroActivity2.this, "Digite uma senha com pelo menos 6 digitos", Toast.LENGTH_SHORT).show();


                if(!binding.txtEmail.getText().toString().isEmpty() && !binding.txtUsername.getText().toString().isEmpty() && !binding.txtSenha.getText().toString().isEmpty())
                {
                    String username = binding.txtUsername.getText().toString().trim();
                    String email = binding.txtEmail.getText().toString().trim();
                    String senha = binding.txtSenha.getText().toString().trim();
                    Usuario user = new Usuario(username,email,senha);
                    Call<Usuario> call = RetrofitClient
                            .getInstance()
                            .getAPI()
                            .registerUser(user);

                    call.enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            Usuario registerResponse = response.body();
                            Gson gson = new GsonBuilder().create();
                            String jsonRespostaNode = gson.toJson(registerResponse);
                            if(response.isSuccessful()){
                                mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(), binding.txtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) // se conseguiu cadastrar
                                        {
                                            String id = mAuth.getUid();
                                            user.setUserId(id);
                                            firebaseDatabase.getReference().child("Users").child(id).setValue(user);
                                        }
                                        else // senão conseguiu cadastrar
                                        {
                                            Toast.makeText(CadastroActivity2.this, "Erro no cadastro no Firebase", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                                Toast.makeText(CadastroActivity2.this, "Cadastrado com Sucesso!", Toast.LENGTH_LONG).show();
                            }else{

                                Toast.makeText(CadastroActivity2.this,  "Erro no cadastro", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            Toast.makeText(CadastroActivity2.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
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

        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

    }

    private void registerUser(){
        String username = binding.txtUsername.getText().toString().trim();
        String email = binding.txtEmail.getText().toString().trim();
        String senha = binding.txtSenha.getText().toString().trim();
        Usuario user = new Usuario(username,email,senha);
        Call<Usuario> call = RetrofitClient
                .getInstance()
                .getAPI()
                .registerUser(user);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Usuario registerResponse = response.body();
                Gson gson = new GsonBuilder().create();
                String jsonRespostaNode = gson.toJson(registerResponse);
                if(response.isSuccessful()){
                    Toast.makeText(CadastroActivity2.this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CadastroActivity2.this, "Erro no Cadastro", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(CadastroActivity2.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    final Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

    //Verificação de email
    boolean validarEmail(String email){
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
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
                Toast.makeText(this, "Falha no Cadastro da Conta Google", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken){
        if (idToken !=  null) {
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("TAG", "signInWithCredential:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Usuario users = new Usuario();
                                users.setUserId(user.getUid());
                                users.setUsername(user.getDisplayName());
                                users.setFotoPerfil(user.getPhotoUrl().toString());
                                firebaseDatabase.getReference().child("Users").child(user.getUid()).setValue(users);
                                Intent intent = new Intent(CadastroActivity2.this, MainActivity.class);
                                startActivity(intent);

                                Toast.makeText(CadastroActivity2.this, "Cadastro com Google", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w("TAG", "signInWithCredential:failure", task.getException());
                            }
                        }
                    });
        }
    }


}

