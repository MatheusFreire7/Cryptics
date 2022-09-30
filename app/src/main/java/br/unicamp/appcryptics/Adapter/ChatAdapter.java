package br.unicamp.appcryptics.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.unicamp.appcryptics.MessageModel;
import br.unicamp.appcryptics.R;

public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessageModel> listaMensagem;
    Context context;

    int ENVIA_VIEW= 1;  // constantes
    int RECEBE_VIEW = 2;

    String recId;

    public ChatAdapter(ArrayList<MessageModel> listaMensagem, Context context) {
        this.listaMensagem = listaMensagem;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> listaMensagem, Context context, String recId) {
        this.listaMensagem = listaMensagem;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ENVIA_VIEW)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_envia,parent,false);
            return new EnviaViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_recebe,parent,false);
            return new RecebeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            MessageModel messageModel = listaMensagem.get(position);

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    new AlertDialog.Builder(context)
                            .setTitle("Deletar")
                            .setMessage("Você quer deletar esta menssagem?")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                    database.getReference().child("chats").child(senderRoom)
                                            .child(messageModel.getMessageId())
                                            .setValue(null);
                                }
                            }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                    return false;
                }
            });

            if(holder.getClass() == EnviaViewHolder.class)
            {
                ((EnviaViewHolder)holder).enviaMsg.setText(messageModel.getMessage());

                Date date = new Date(messageModel.getTimeStamp());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
                String strDate = simpleDateFormat.format(date);
                ((EnviaViewHolder)holder).enviaTempo.setText(strDate.toString());
            }
            else
            {
                ((RecebeViewHolder)holder).recebeMsg.setText(messageModel.getMessage());

                Date date = new Date(messageModel.getTimeStamp());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
                String strDate = simpleDateFormat.format(date);
                ((RecebeViewHolder)holder).recebeTempo.setText(strDate.toString());
            }
    }

    @Override
    public int getItemViewType(int position)
    {
        if(listaMensagem.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return ENVIA_VIEW;
        }
        else
        {
            return RECEBE_VIEW;
        }
    }

    @Override
    public int getItemCount() {
        return listaMensagem.size();
    }

    public class RecebeViewHolder extends RecyclerView.ViewHolder{

        TextView recebeMsg, recebeTempo;


        public RecebeViewHolder(@NonNull View itemView) {
            super(itemView);

            recebeMsg = itemView.findViewById(R.id.txtRecebe);
            recebeTempo = itemView.findViewById(R.id.recebeTempo);
        }
    }

    public class EnviaViewHolder extends RecyclerView.ViewHolder{

        TextView enviaMsg, enviaTempo;

        public EnviaViewHolder(@NonNull View itemView) {
            super(itemView);

            enviaMsg = itemView.findViewById(R.id.txtEnvia);
            enviaTempo = itemView.findViewById(R.id.enviaTempo);
        }
    }
}
