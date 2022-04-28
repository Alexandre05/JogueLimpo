package br.com.joguelimpocomosanimais.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.joguelimpocomosanimais.Model.ConversaAssunto;
import br.com.joguelimpocomosanimais.Model.Usuario;
import br.com.joguelimpocomosanimais.R;
import de.hdodenhof.circleimageview.CircleImageView;


public class ConversaAdpter extends RecyclerView.Adapter<ConversaAdpter.MyVilHolder> {

    private  List<ConversaAssunto> conversaA;
    private  Context context;
    public ConversaAdpter(List<ConversaAssunto> lista, Context c) {
this.conversaA=lista;
this.context=c;
    }


    @Override
    public MyVilHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista =LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contato, parent, false);
        return new MyVilHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVilHolder holder, int position) {
    ConversaAssunto conversaAssunto = conversaA.get(position);
    holder.ult.setText(conversaAssunto.getUltimaMensagem());

        Usuario u = conversaAssunto.getUruarioExibicao();
        holder.nome.setText(u.getNome());


        if(u.getFoto()!=null){

            Uri uri = Uri.parse(u.getFoto());
            Glide.with(context).load(uri).into(holder.foto);


        }else {


            holder.foto.setImageResource(R.drawable.c_pessoa);
        }



    }

    @Override
    public int getItemCount() {
        return conversaA.size();
    }

    public  class  MyVilHolder extends  RecyclerView.ViewHolder{

CircleImageView foto;
TextView nome,ult;

    public MyVilHolder(@NonNull View itemView) {
        super(itemView);

        foto = itemView.findViewById(R.id.imageContato);
        nome=itemView.findViewById(R.id.nomeContato);
        ult=itemView.findViewById(R.id.ultimaContato);
    }
}

}
