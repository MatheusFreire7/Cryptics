package br.unicamp.appcryptics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import br.unicamp.appcryptics.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.settings:
                Toast.makeText(this, "Configurações Clicada", Toast.LENGTH_LONG).show();
                break;

            case R.id.groupChat:
                Toast.makeText(this, "Chat de Grupo clicado", Toast.LENGTH_LONG).show();
                break;

            case  R.id.logout:
               mAuth.signOut();
               Intent intent = new Intent(MainActivity.this,EntraActivity3.class);
               startActivity(intent);
               break;
        }
        return super.onOptionsItemSelected(item);
    }
}