package br.com.joguelimpocomosanimais.Activity;
import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.santalu.maskara.widget.MaskEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.joguelimpocomosanimais.Helper.ConFirebase;
import br.com.joguelimpocomosanimais.Helper.Permissoes;
import br.com.joguelimpocomosanimais.Model.Anuncios;
import br.com.joguelimpocomosanimais.Model.Usuario;
import br.com.joguelimpocomosanimais.R;
import dmax.dialog.SpotsDialog;

public class CadastrarAnimais extends AppCompatActivity
implements android.view.View.OnClickListener {
    private EditText campoNome, campoRaca,campoPorte, campoObs,campoNomeRes;
    private ImageView imagem1, imagem2;
    private Anuncios anuncios;
    private Usuario usuario;
    private AlertDialog dialog;
    private MaskEditText campoTelefone;
    private Spinner campoanimais, campocidade;
    private StorageReference storage;
    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };

    private List<String> listasFotoRe = new ArrayList<>();
    private List<String >listaURLFotos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cadastrar_animais);
        Permissoes.validarPermissoes(permissoes, this, 1);

        iniciarCampo();
        carregarSpi();
        storage = ConFirebase.getFirebaseStorage();

    }
    public void salvarAni() {
        dialog = new SpotsDialog.Builder(this)
                .setMessage("Salvando...")
                .setCancelable(false)


                .show();



                dialog.show();




       for(int i=0; i<listasFotoRe.size();i++){

String urlImagem = listasFotoRe.get(i);
int tamanhoLista = listasFotoRe.size();

 salvarFotoStorage(urlImagem,tamanhoLista,i);



       }


    }

    private  void salvarFotoStorage(String url, final int totalFotos,int contador){
// criar refencia no storage

   final StorageReference imagemAnuncio = storage
           .child ("imagens")
          .child("Anuncios")
          .child(anuncios.getIdAanuncio())
          .child("imagem"+contador);

        UploadTask uploadTask = imagemAnuncio.putFile(Uri.parse(url));
      uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
          @Override
          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
             imagemAnuncio.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                 @Override
                 public void onSuccess(Uri uri) {
String urlConverTida= uri.toString();
listaURLFotos.add(urlConverTida);

if (totalFotos==listaURLFotos.size()){
anuncios.setFotos(listaURLFotos);

anuncios.salvar();
dialog.dismiss();
finish();

}



                 }
             });
          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
    exibirMensagemErro("Falha ao fazer uplad");
    Log.e("INFO","Falha ao fazer upload:"+e.getMessage());
          }
      });

    }
    private  Anuncios ConfiAnuncio(){

        String animal =campoanimais.getSelectedItem().toString();
        String bairro =campocidade.getSelectedItem().toString();
        String nome =campoNome.getText().toString();
        String raca =campoRaca.getText().toString();
        String porte=campoPorte.getText().toString();
        String nomeCampo=campoNomeRes.getText().toString();
        String ob =campoObs.getText().toString();
      String fone=campoTelefone.getMasked();

      Anuncios anuncios = new Anuncios();
     anuncios.setAnimal(animal);
     anuncios.setBairrro (bairro);
     anuncios.setNome(nome);
     anuncios.setRaca(raca);
     anuncios.setOg(ob);
     anuncios.setPorte(porte);
     anuncios.setNomePerfilU(nomeCampo);
     anuncios.setTelefone(fone);
        return  anuncios;

    }
    public  void validarAnuncio(android.view.View view ){
       anuncios= ConfiAnuncio();

        if(listasFotoRe.size()!=0){
if(!anuncios.getAnimal().isEmpty()){
    if(!anuncios.getBairrro().isEmpty()){
        if(!anuncios.getNome().isEmpty()){
            if (!anuncios.getRaca().isEmpty()){

if (!anuncios.getOg().isEmpty()){
    if(!anuncios.getTelefone().isEmpty()) {
        if (!anuncios.getNomePerfilU().isEmpty()){



        }else {
            exibirMensagemErro("Informe nome Responsável pelas Informações.");

        }
        salvarAni();
    } else {
        exibirMensagemErro("Faltou número do Telefone");

    }

}else {
    exibirMensagemErro("Digite mais detalhes");}
            }else {
                exibirMensagemErro("Digite a raça, ou algo que a defina");
            }

        }else {
            exibirMensagemErro("Digite um nome ou apelido");
        }

    }else {

        exibirMensagemErro("Selecione a cidade");
    }



}else {

    exibirMensagemErro("Selecione Um especie");


}
        }else

        {
            exibirMensagemErro("Selecione Uma foto");
        }

    }

private  void exibirMensagemErro(String mensagem){
    Toast.makeText(this,
            mensagem,Toast.LENGTH_SHORT).show();


}


    @Override
    public void onClick( android.view.View v) {
        Log.d("onClick","onClick:"+v.getId());
        switch (v.getId()) {
            case R.id.imageCada1:

                escolherImagem(1);
                Log.d("onClick","onClick:"+v.getId());
                break;

            case R.id.imageCada2:
                escolherImagem(2);
                Log.d("onClick","onClick:"+v.getId());
                break;

        }

    }


    public void escolherImagem(int requestCode) {

        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent,requestCode );



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {
            // o erro estava no resultode
            //recupera imagem
            Uri imagemSe = data.getData();
            String caminhoIma = imagemSe.toString();
            // configura imagem no imagiView

            if (requestCode ==1) {
                imagem1.setImageURI(imagemSe);
            } else if (requestCode ==2) {
                imagem2.setImageURI(imagemSe);
            }

            listasFotoRe.add(caminhoIma);
        }


    }



   public  void carregarSpi(){

String []  animais= getResources().getStringArray(R.array.Animais);
       ArrayAdapter<String> Adapter= new ArrayAdapter<String>(
          this, android.R.layout.simple_spinner_item,animais
       );

       campoanimais.setAdapter(Adapter);
       String []  bairro= getResources().getStringArray(R.array.Bairro);
       ArrayAdapter<String> Adapter2= new ArrayAdapter<String>(
               this, android.R.layout.simple_spinner_item,bairro
       );

       campocidade.setAdapter(Adapter2);


   }
    private void iniciarCampo() {

        campoNome = findViewById(R.id.editNome);
        campoRaca=findViewById(R.id.editRaca);
        campoObs=findViewById(R.id.editObs);
        imagem1=findViewById(R.id.imageCada1);
        imagem2=findViewById(R.id.imageCada2);
        imagem1.setOnClickListener(this);
        imagem2.setOnClickListener(this);
        campoanimais=findViewById(R.id.tipoAnimal);
        campocidade=findViewById(R.id.cidade);
        campoTelefone = findViewById(R.id.fone);
        campoNomeRes=findViewById(R.id.editNomeDoador);
        campoPorte=findViewById(R.id.editPorte);
        Locale locale = new Locale("pt","BR");

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissaoResultado:grantResults){
if(permissaoResultado== PackageManager.PERMISSION_DENIED){

  alertaPermissao();

}



        }
    }
    private  void alertaPermissao(){
        AlertDialog.Builder bul = new AlertDialog.Builder(this);
        bul.setTitle("Permissões Negadas");
        bul.setMessage("Para Usar o App é necessário aceitar as permissões");
        bul.setCancelable(false);
        bul.setPositiveButton("Aceito", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



                finish();
            }
        });

AlertDialog dial = bul.create();
dial.show();
    }
}