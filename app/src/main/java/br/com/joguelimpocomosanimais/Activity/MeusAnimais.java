package br.com.joguelimpocomosanimais.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.joguelimpocomosanimais.Adapter.AdapterAnuncios;
import br.com.joguelimpocomosanimais.Helper.ConFirebase;
import br.com.joguelimpocomosanimais.Helper.RecyclerItemClickListener;
import br.com.joguelimpocomosanimais.Model.Anuncios;
import br.com.joguelimpocomosanimais.Model.Usuario;
import br.com.joguelimpocomosanimais.R;
import br.com.joguelimpocomosanimais.databinding.ActivityMeusAnimais2Binding;

public class MeusAnimais extends AppCompatActivity {
    private static final int SELECAO_CAMERA  = 100;
    private static final int SELECAO_GALERIA = 200;
    private AppBarConfiguration appBarConfiguration;
    private ActivityMeusAnimais2Binding binding;
    private DatabaseReference anunciosUsuarioRef;
    private RecyclerView recycleAnuncios;
    private AlertDialog alert;

  private Usuario usuario;
    private List <Anuncios> anuncios= new ArrayList<>();
    private AdapterAnuncios adapterAnuncios;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMeusAnimais2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


   anunciosUsuarioRef = ConFirebase.getFirebaseDatabase()
         // aqui pega e mostra os meus anuncios
           .child("anuncios")

         .child(ConFirebase.getIdUsuario());


        inicializarCompo();
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


// efento de clique
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
          startActivity(new Intent(getApplicationContext(),CadastrarAnimais.class));
            }
        });

        binding.cameraU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                if ( i.resolveActivity(getPackageManager()) != null ) {
                    startActivityForResult(i, SELECAO_GALERIA);
                }


            }
        });

        binding.fotoU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        recycleAnuncios.setLayoutManager( new LinearLayoutManager(this));
        recycleAnuncios.setHasFixedSize(true);
      adapterAnuncios = new  AdapterAnuncios(anuncios,this);
   recycleAnuncios.setAdapter(adapterAnuncios);

   recuperarAnucnis();
   recycleAnuncios.addOnItemTouchListener(

           new RecyclerItemClickListener(
                   this,
                   recycleAnuncios,
                   new RecyclerItemClickListener.OnItemClickListener() {
                       @Override
                       public void onItemClick(View view, int position) {
                       }
                       @Override
                       public void onLongItemClick(View view, int position) {
                           Anuncios anunciosSelecionado = anuncios.get(position);
                           anunciosSelecionado.remover();
                           adapterAnuncios.notifyDataSetChanged();
                       }

                       @Override
                       public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                       }
                   }
           )
   );


    }


    private void recuperarAnucnis() {
        alert= new AlertDialog.Builder(this)
                .setMessage("Recuperando Meus Pets...")
                .setCancelable(false)
                .show();
        alert.show();


        anunciosUsuarioRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange( DataSnapshot dataSnapshot) {
        anuncios.clear();
        for (DataSnapshot ds: dataSnapshot.getChildren()){

anuncios.add(ds.getValue(Anuncios.class));

        }
        Collections.reverse(anuncios);
        adapterAnuncios.notifyDataSetChanged();
     alert.dismiss();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

    }

    private void inicializarCompo() {

        recycleAnuncios = findViewById(R.id.reclyAnim);


    }


}