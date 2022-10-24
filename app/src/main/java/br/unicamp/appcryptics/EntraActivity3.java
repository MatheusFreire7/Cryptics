package br.unicamp.appcryptics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entra3);
        binding = ActivityEntra3Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

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


}