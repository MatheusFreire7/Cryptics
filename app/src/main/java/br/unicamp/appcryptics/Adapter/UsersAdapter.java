package br.unicamp.appcryptics.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.unicamp.appcryptics.ChatActivity;
import br.unicamp.appcryptics.R;
import br.unicamp.appcryptics.Usuario;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.viewHolder>
{

    ArrayList<Usuario> listUsuario; //arraylist de usuarios
    Context context;

    public UsersAdapter(ArrayList<Usuario> listUsuario, Context context) {
        this.listUsuario = listUsuario;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position)
    {
        Usuario users = listUsuario.get(position);
        Picasso.get().load(users.getFotoPerfil()).placeholder(R.drawable.avatar3).into(holder.image);
        holder.username.setText(users.getUsername());

        FirebaseDatabase.getInstance().getReference().child("chats")
                        .child(FirebaseAuth.getInstance().getUid() + users.getUserId())
                                .orderByChild("timeStamp")
                                        .limitToLast(1)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.hasChildren()){
                                                            for(DataSnapshot snapshot1: snapshot.getChildren())
                                                            {
                                                                holder.ultimaMenssagem.setText(snapshot1.child("message").getValue().toString());
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userId", users.getUserId());
                intent.putExtra("fotoPerfil", users.getFotoPerfil());
                intent.putExtra("Username", users.getUsername());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return listUsuario.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView username, ultimaMenssagem;


        public viewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.perfil_imagem);
            username = itemView.findViewById(R.id.userNameList);
            ultimaMenssagem = itemView.findViewById(R.id.ultimaMenssagem);

        }
    }
}
