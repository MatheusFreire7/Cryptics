package br.unicamp.appcryptics.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

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