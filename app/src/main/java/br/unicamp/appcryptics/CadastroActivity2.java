package br.unicamp.appcryptics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import br.unicamp.appcryptics.databinding.ActivityCadastro2Binding;
import br.unicamp.appcryptics.databinding.ActivityEntra3Binding;

public class CadastroActivity2 extends AppCompatActivity{


    ActivityCadastro2Binding binding;  // biblioteca que permite vincular componentes do layout
    private FirebaseAuth mAuth;        // Autenticação do Firebase
    FirebaseDatabase firebaseDatabase; // Banco de dados do Firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro2);

        binding =  ActivityCadastro2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        getSupportActionBar().hide();

        binding.btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.txtEmail.getText().toString().isEmpty() && !binding.txtUsername.getText().toString().isEmpty() && !binding.txtSenha.getText().toString().isEmpty())
                {
                    mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(), binding.txtSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) // se conseguiu cadastrar
                            {
                                Usuario user = new Usuario(binding.txtUsername.getText().toString(), binding.txtSenha.getText().toString(),binding.txtEmail.getText().toString());
                                String id = task.getResult().getUser().getUid();
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
            }
        });


    }
}