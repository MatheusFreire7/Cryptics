package br.unicamp.appcryptics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.mbms.DownloadProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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


}