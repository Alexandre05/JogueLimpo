package br.com.joguelimpocomosanimais.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.joguelimpocomosanimais.Adapter.MensagemAdpter;
import br.com.joguelimpocomosanimais.Helper.Base64Custon;
import br.com.joguelimpocomosanimais.Helper.ConFirebase;
import br.com.joguelimpocomosanimais.Helper.Permissoes;
import br.com.joguelimpocomosanimais.Model.ConversaAssunto;
import br.com.joguelimpocomosanimais.Model.Mensagem;
import br.com.joguelimpocomosanimais.Model.Usuario;
import br.com.joguelimpocomosanimais.R;

public class Conversas extends AppCompatActivity {
    private ImageView fotoI;
    private TextView nome;
    private TextView ultima;

  private Usuario usuarioDestinatario;
  private EditText editmensagem;
    private RecyclerView recyclerViewM;
private MensagemAdpter adpter;
private StorageReference storege;
private ChildEventListener mensagensChildEventListener;
private DatabaseReference mensageRefe;
private ImageView imaC;
    private String idUsuarioRemente;
    private String idUsuarioDestinatario;
private DatabaseReference database;
    private List<Mensagem> mensagens =new ArrayList<>();
    private static final int seleCame = 100;
    private static final int seleGale = 200;
    private String[] permissoes = new String[]{

            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        fotoI= findViewById(R.id.imageFotoConversa);
        imaC=findViewById(R.id.imageCameraC);
        nome = findViewById(R.id.textConversa);

        recyclerViewM= findViewById(R.id.RyclameMensagem);

        Permissoes.validarPermissoes(permissoes, this, 1);

        editmensagem= findViewById(R.id.EscreveMensagem);
  idUsuarioRemente = ConFirebase.getIdentificarUsaurio();



        if(bundle !=null){
usuarioDestinatario= (Usuario) bundle.getSerializable("chatContato");
        nome.setText(usuarioDestinatario.getNome());

         String foto = usuarioDestinatario.getFoto();

if(foto !=null){
Uri url= Uri.parse(usuarioDestinatario.getFoto());

   Glide.with(Conversas.this)
           .load(url)
           .into(fotoI);

}else {

fotoI.setImageResource(R.drawable.c_pessoa);

}
idUsuarioDestinatario= Base64Custon.codificarBase64(usuarioDestinatario.getEmail());




        }
  adpter= new MensagemAdpter(mensagens,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewM.setLayoutManager(layoutManager);
        recyclerViewM.setHasFixedSize(true);
        recyclerViewM.setAdapter(adpter);
      storege= ConFirebase.getFirebaseStorage();
        database= ConFirebase.getFirebaseDatabase();
        mensageRefe= database .child("mensagens")
                .child(idUsuarioRemente)
                .child(idUsuarioDestinatario);


        imaC.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, seleCame);





            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            Bitmap imagem = null;

            try {
                  switch (requestCode){
                      case seleCame:

                          imagem = (Bitmap) data.getExtras().get("data");
                         break;

                  }
                 if(imagem !=null){

                     // recupera dados da imagem no para o firebase

                     ByteArrayOutputStream baos = new ByteArrayOutputStream();
                     imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                     byte[] dadosImagem = baos.toByteArray();


                     String nomeImagem= UUID.randomUUID().toString();
          final StorageReference imageRef= storege.child("imagens")
                   .child("fotos")
                   .child(idUsuarioRemente)
                   .child(nomeImagem+"jpeg");
                     UploadTask uploadTask=imageRef.putBytes(dadosImagem);
uploadTask.addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        //Log.d("erro","Erro ao fazer upload");
        Toast.makeText(Conversas.this,
                "Erro ao fazer upload da imagem",
                Toast.LENGTH_SHORT).show();
    }
}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
    @Override
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


        imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                 Uri dowloadrl = task.getResult();
               Mensagem mensagem = new Mensagem();
               mensagem.setIdUsuario(idUsuarioRemente);
               mensagem.setMensagem(".jpeg");
               mensagem.setImagem(dowloadrl.toString());


               salvarMensagem(idUsuarioRemente,idUsuarioDestinatario,mensagem);
                salvarMensagem(idUsuarioDestinatario,idUsuarioRemente,mensagem);
                Toast.makeText(Conversas.this,
                        "Sucesso ao enviar  imagem",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
});


                 }

            }catch (Exception e){

                  e.printStackTrace();
            }

        }

    }

    public  void Enviar(View view){
  String textomensagem = editmensagem.getText().toString();
  if(! textomensagem.isEmpty()){
      Mensagem msg = new Mensagem();
     msg.setIdUsuario(idUsuarioRemente);
     msg.setMensagem(textomensagem);
     // salvar mensagem remetente
     salvarMensagem(idUsuarioRemente,idUsuarioDestinatario,msg);

     // salvar mensage destino

      salvarMensagem(idUsuarioDestinatario,idUsuarioRemente,msg);

      // salvar conversa
     salvarConversa(msg);



    }else{

      Toast.makeText(Conversas.this,
              "Digite sua Mensagem!!"
              ,Toast.LENGTH_LONG).show();

  }




    }

    private void salvarConversa(Mensagem msg){

        ConversaAssunto coverReme = new ConversaAssunto();
coverReme.setIdRemetente(idUsuarioRemente);
coverReme.setIdDestinatario(idUsuarioDestinatario);
 coverReme.setUltimaMensagem(msg.getMensagem());
 coverReme.setUruarioExibicao(usuarioDestinatario);
 coverReme.salvar();
    }
    private void salvarMensagem(String idRemete, String idDestino,Mensagem mensagem){

        DatabaseReference databaseReference = ConFirebase.getFirebaseDatabase();
          DatabaseReference mensaRefe = databaseReference.child("mensagens");
       mensaRefe.child(idRemete)
                .child(idDestino)
                .push()
                .setValue(mensagem);

      editmensagem.setText("");


    }


    @Override
    protected void onStart() {
        super.onStart();
        recuperMensagem();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mensageRefe.removeEventListener(mensagensChildEventListener);
        mensagens.clear();

    }

    private  void  recuperMensagem(){

mensagensChildEventListener =mensageRefe.addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Mensagem mensagem = snapshot.getValue(Mensagem.class);
        mensagens.add(mensagem);
        adpter.notifyDataSetChanged();
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});



    }
}