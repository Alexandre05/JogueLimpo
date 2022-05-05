package br.com.joguelimpocomosanimais.Activity;

import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.joguelimpocomosanimais.Model.Anuncios;
import br.com.joguelimpocomosanimais.Model.Usuario;
import br.com.joguelimpocomosanimais.R;

public class DetalhesAc extends AppCompatActivity {

private TextView nome,raca,ob,porte;
private Anuncios anuncioSele;
private TextView nomeU;
private Usuario usuario;
private CarouselView img;
private EditText nomeUsuario;
    private ImageSwitcher imageSwitcher;
  private FirebaseStorage storage;
    private FirebaseAuth autenticacao;
    private  FirebaseUser usuarioAtual;

    private List<Anuncios> listacioAnimais = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);
      usuarioAtual=Usuario.getUsuarioAutal();


        getSupportActionBar().setTitle("Detalhes do Pet");
       //StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        iniciarCompo();

         anuncioSele= (Anuncios) getIntent().getSerializableExtra("animalSelecionado");
          if(anuncioSele!=null) {
              nome.setText(anuncioSele.getNome());
              raca.setText(anuncioSele.getRaca());
              ob.setText(anuncioSele.getOg());
              porte.setText(anuncioSele.getPorte());
               nomeU.setText(anuncioSele.getNomePerfilU());



              // aqui pega as imagens para o carrosle
              ImageListener imageListener = new ImageListener() {
                  @Override
                  public void setImageForPosition(int position, ImageView imageView) {
                      String urlString= anuncioSele.getFotos().get(position);
                      Picasso.get().load(urlString).into(imageView);
                  }
              };
                 img.setPageCount(anuncioSele.getFotos().size());
                 img.setImageListener(imageListener);

          }



    }





    public  void entrarContato(View view){

        // intente para ir telefone diretamente
  Intent i = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel",anuncioSele.getTelefone(),null));

   startActivity(i);


    }

    public  void Chat(View v){
        FirebaseUser user= usuarioAtual;
        if (user!=null){
            Intent it = new Intent(DetalhesAc.this, Chat.class);
            startActivity(it);

        }
        else {

            Toast.makeText(DetalhesAc.this,
                    "Vc não está cadastrado!",
                    Toast.LENGTH_SHORT).show();

        }
    }
    private  void iniciarCompo(){
       img=findViewById(R.id.carouse);
          nome=findViewById(R.id.textView4);
          raca=findViewById(R.id.textView5);
          ob=findViewById(R.id.textView6);
        nomeU= findViewById(R.id.Anuncioante);
        porte=findViewById(R.id.Porte);


    }
}