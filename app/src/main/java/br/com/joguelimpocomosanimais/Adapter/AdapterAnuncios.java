package br.com.joguelimpocomosanimais.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.joguelimpocomosanimais.Model.Anuncios;
import br.com.joguelimpocomosanimais.R;


public class AdapterAnuncios extends RecyclerView.Adapter<AdapterAnuncios.MyViewHolder> {
    private List<Anuncios> anuncios;

    private Context context;

    public AdapterAnuncios(List<Anuncios> anuncios, Context context) {
        this.anuncios = anuncios;
        this.context = context;
    }

    @NonNull
        @Override
        public MyViewHolder onCreateViewHolder (@NonNull ViewGroup parent,
        int viewType){
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_anuncio, parent, false);

            return new MyViewHolder( item );
        }

        @Override
        public void onBindViewHolder (@NonNull MyViewHolder holder,int position){

            Anuncios anuncio= anuncios.get(position);
            holder.nome.setText(anuncio.getNome());
            holder.raca.setText(anuncio.getRaca());
            holder.ob.setText(anuncio.getOg());



           List<String> urlFtos = anuncio.getFotos();


  String urlCapa= urlFtos.get(0);
  Picasso.get().load(urlCapa).into(holder.foto);



        }

        @Override
        public int getItemCount () {
            return anuncios.size();
        }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome;
        TextView raca;
        TextView ob;
        TextView nomeUsuario;
        ImageView foto;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nome= itemView.findViewById(R.id.texVilNome);
            raca=itemView.findViewById(R.id.textViewRaca);
            ob=itemView.findViewById(R.id.textViewOb);
            foto=itemView.findViewById(R.id.imageAnuncios);

        }
    }
}

