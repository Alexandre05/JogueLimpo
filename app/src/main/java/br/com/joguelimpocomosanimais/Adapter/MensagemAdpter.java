package br.com.joguelimpocomosanimais.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import br.com.joguelimpocomosanimais.Helper.ConFirebase;
import br.com.joguelimpocomosanimais.Model.Mensagem;
import br.com.joguelimpocomosanimais.R;


public class MensagemAdpter extends RecyclerView.Adapter<MensagemAdpter.MyvHolder> {

 private List<Mensagem> mensagens;
 private Context context;

 private  static final int Tipo_remetente = 0;
    private  static final int Tipo_destinatario = 1;

    public MensagemAdpter(List<Mensagem> lita, Context c) {
     this.mensagens=lita;
     this.context=c;
    }

    @NonNull
    @Override
    public MyvHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item=null;
       if(viewType==Tipo_remetente){
     item =LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_mensagem_remete, parent, false);




       }else  if (viewType==Tipo_destinatario){

           item =LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_mensagem_destino, parent, false);


       }

      return new MyvHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyvHolder holder, int position) {
        Mensagem mensagem = mensagens.get(position);
        String msg = mensagem.getMensagem();
        String img=mensagem.getImagem();

      if (img!=null){
          Uri url=Uri.parse(img);
          holder.imagem.setVisibility(View.VISIBLE);

          Glide.with(context).load(url)
                  .into(holder.imagem);

          holder.mensagem.setVisibility(View.GONE);


      }else {
          holder.mensagem.setText( msg );

          //Esconder a imagem
        holder.imagem.setVisibility(View.GONE);

      }



    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {
     Mensagem mensagem = mensagens.get(position);

     String idUsu = ConFirebase.getIdentificarUsaurio();
     if( idUsu.equals(mensagem.getIdUsuario())){
         return Tipo_remetente;


     }
           return Tipo_destinatario;
    }

    public class MyvHolder extends RecyclerView.ViewHolder{
         TextView mensagem;
         ImageView imagem;

      public MyvHolder(@NonNull View itemView) {
          super(itemView);

          mensagem = itemView.findViewById(R.id.MensagemDestino);
          imagem=itemView.findViewById(R.id.imageDestino);
      }
  }

}
