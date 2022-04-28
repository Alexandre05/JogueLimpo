package br.com.joguelimpocomosanimais.Model;



import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

import br.com.joguelimpocomosanimais.Helper.ConFirebase;


public class Anuncios  implements Serializable {
  private String idAanuncio;

  private  String animal;
  private String bairrro;
  private String nome;
  private String raca;
  private String og;
  private String porte;

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    private String telefone;
 private  String nomePerfilU;
    public String getTelefone() {
        return telefone;
    }

    public String getNomePerfilU() {
        return nomePerfilU;
    }

    public void setNomePerfilU(String nomePerfilU) {
        this.nomePerfilU = nomePerfilU;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }



  private List<String> fotos;



  public Anuncios() {
    DatabaseReference anuncioRefe=
            ConFirebase.getFirebaseDatabase()
            .child("anuncios") ;

    setIdAanuncio(anuncioRefe.push().getKey());
  }
  public  void  salvar(){
    String idUsuario= ConFirebase.getIdUsuario();
    DatabaseReference anuncioRefe= ConFirebase.getFirebaseDatabase()

            .child("anuncios");
    anuncioRefe.child(idUsuario)
            .child(getIdAanuncio())
            .setValue(this);

  salvarAnuncioPublico();

  }

  public  void remover(){
    String idiUsuario= ConFirebase.getIdUsuario();
    DatabaseReference anuncioRefe= ConFirebase.getFirebaseDatabase()
            .child("anuncios")
            .child(idiUsuario)

            .child(getIdAanuncio());

    anuncioRefe.removeValue();
    removerAPu();
  }
// DELERA OS ANUNCIOS PARA TODOS
  public  void removerAPu(){
      DatabaseReference anuncioRefe= ConFirebase.getFirebaseDatabase()
              .child("Anuncios_Publicos")
      .child(getAnimal())
              .child(getBairrro())
              .child(getIdAanuncio());

    anuncioRefe.removeValue();


  }

// SALVA ANUNCIOS PARA TODOS
  public  void  salvarAnuncioPublico(){
   //String idUsuario = ConFirebase.getIdUsuario();
    DatabaseReference anuncioRefe= ConFirebase.getFirebaseDatabase()
            .child("Anuncios_Publicos");
    anuncioRefe.child(getAnimal())
            .child(getBairrro())
            .child(getIdAanuncio())
            .setValue(this);
  }


  public String getIdAanuncio() {
    return idAanuncio;
  }

  public void setIdAanuncio(String idAanuncio) {
    this.idAanuncio = idAanuncio;
  }

  public String getAnimal() {
    return animal;
  }

  public void setAnimal(String animal) {
    this.animal = animal;
  }

  public String getBairrro() {
    return bairrro;
  }

  public void setBairrro(String bairrro) {
    this.bairrro = bairrro;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getRaca() {
    return raca;
  }

  public void setRaca(String raca) {
    this.raca = raca;
  }

  public String getOg() {
    return og;
  }

  public void setOg(String og) {
    this.og = og;
  }

  public List<String> getFotos() {
    return fotos;
  }

  public void setFotos(List<String> fotos) {
    this.fotos = fotos;
  }
}
