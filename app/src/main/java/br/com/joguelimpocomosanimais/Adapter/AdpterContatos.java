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

import br.com.joguelimpocomosanimais.Model.Usuario;
import br.com.joguelimpocomosanimais.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class AdpterContatos extends RecyclerView.Adapter<AdpterContatos.MyVilHolder> {
 private List<Usuario> contatos;
 private Context context;
    public AdpterContatos(List<Usuario> listaContatos, Context c) {
   this.contatos=listaContatos;
   this.context=c;

    }

    @NonNull
    @Override
    public MyVilHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista =LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contato, parent, false);
        return new MyVilHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVilHolder holder, int position) {
     Usuario usuario = contatos.get(position);
holder.nome.setText(usuario.getNome());

if(usuario.getFoto()!=null){
    Uri uri = Uri.parse(usuario.getFoto());
    Glide.with(context).load(uri).into(holder.foto);

}else {



}


    }

    @Override
    public int getItemCount() {
        return contatos.size();
    }

    public  class MyVilHolder extends RecyclerView.ViewHolder{
        CircleImageView foto;
        TextView nome;

        public MyVilHolder(@NonNull View itemView) {
            super(itemView);
            foto=itemView.findViewById(R.id.imageContato);
            nome= itemView.findViewById(R.id.nomeContato);
        }
    }
}
