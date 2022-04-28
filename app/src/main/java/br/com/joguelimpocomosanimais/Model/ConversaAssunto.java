package br.com.joguelimpocomosanimais.Model;

import com.google.firebase.database.DatabaseReference;

import br.com.joguelimpocomosanimais.Helper.ConFirebase;


public class ConversaAssunto {

private  String idRemetente;
private String idDestinatario;

private String ultimaMensagem;
private Usuario uruarioExibicao;

    public Usuario getUruarioExibicao() {
        return uruarioExibicao;
    }

    public void setUruarioExibicao(Usuario uruarioExibicao) {
        this.uruarioExibicao = uruarioExibicao;
    }

    public  void salvar(){
    DatabaseReference databaseReference=
            ConFirebase.getFirebaseDatabase();
    DatabaseReference conversaRef=
            databaseReference.child("conversas");
    conversaRef.child(this.getIdRemetente())
            .child(this.getIdDestinatario())
            .setValue(this);



}
    public String getIdRemetente() {
        return idRemetente;
    }

    public void setIdRemetente(String idRemetente) {
        this.idRemetente = idRemetente;
    }

    public String getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(String idDestinatario) {
        this.idDestinatario = idDestinatario;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }



    public ConversaAssunto() {
    }
}
