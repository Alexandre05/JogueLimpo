package br.com.joguelimpocomosanimais.Activity;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
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
import br.com.joguelimpocomosanimais.R;
import dmax.dialog.SpotsDialog;

public class Animais extends AppCompatActivity {
  private FirebaseAuth autenticacao;
  private RecyclerView recyclerViewPu;
  private EditText nomeUsuario;
  private Button animais,bairoo;
  private DatabaseReference anunciosRef;
    private List <Anuncios> listacioAnimais = new ArrayList<>();
    private AdapterAnuncios adapterAnuncios;
    private AlertDialog alertDialog;
    private String filtroAni="";
    private boolean filtrandoPorBairro=false;
    private  String filtroBairro="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_animais);
        Toolbar tolbar= findViewById(R.id.toolbar);



inicializarCompo();


       autenticacao= ConFirebase.getReferenciaAutencicacao();
        anunciosRef= ConFirebase.getFirebaseDatabase()

                .child("Anuncios_Publicos");
       //autenticacao.signOut();


        recyclerViewPu.setLayoutManager( new LinearLayoutManager(this));
        recyclerViewPu.setHasFixedSize(true);
        adapterAnuncios = new  AdapterAnuncios(listacioAnimais,this);
        recyclerViewPu.setAdapter(adapterAnuncios);

        recuperaAnuncioPublicos();

  recyclerViewPu.addOnItemTouchListener( new RecyclerItemClickListener(
          this, recyclerViewPu,
          new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                  Anuncios anunciosSelecio = listacioAnimais.get(position);
Intent i = new Intent(Animais.this,DetalhesAc.class);
i.putExtra("animalSelecionado",anunciosSelecio);
startActivity(i);
              }

              @Override
              public void onLongItemClick(View view, int position) {

              }

              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              }
          }


  ));

    }
    public  void filtrarBairro(View view){
        if (filtrandoPorBairro == true) {
            AlertDialog.Builder dialoAnimalRece = new AlertDialog.Builder(this);
            dialoAnimalRece.setTitle("Filtre Por Bairro!!");
            View viwSpi = getLayoutInflater().inflate(R.layout.dialo_spiner,null);
            final  Spinner spinnerBairro = viwSpi.findViewById(R.id.spinnerFiltroAnimal);
            String []  animais= getResources().getStringArray(R.array.Bairro);
            ArrayAdapter<String> Adapter= new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item,animais
            );

            spinnerBairro.setAdapter(Adapter);
            String []  bairro= getResources().getStringArray(R.array.Bairro);
            ArrayAdapter<String> Adapter2= new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item,bairro
            );


            dialoAnimalRece.setView(viwSpi);


            dialoAnimalRece.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    filtroBairro=spinnerBairro.getSelectedItem().toString();
                    recuperaAnuncioPorBairro();



                }
            });

            dialoAnimalRece.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = dialoAnimalRece.create();
            dialog.show();


        }else {
            Toast.makeText(this,"Escolha primeiro um animal",
            Toast.LENGTH_SHORT).show();


        }



    }
    public  void filtrarPorAnimal(View view){
        AlertDialog.Builder dialoAnimalRece = new AlertDialog.Builder(this);
        dialoAnimalRece.setTitle("Escolha o Tipo de Animal");
        // configura spiner
        View viwSpi = getLayoutInflater().inflate(R.layout.dialo_spiner,null);
        Spinner spinner = viwSpi.findViewById(R.id.spinnerFiltroAnimal);

        String []  animais= getResources().getStringArray(R.array.Animais);
        ArrayAdapter<String> Adapter= new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,animais
        );

        spinner.setAdapter(Adapter);
        String []  bairro= getResources().getStringArray(R.array.Bairro);
        ArrayAdapter<String> Adapter2= new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,bairro
        );


        dialoAnimalRece.setView(viwSpi);


        dialoAnimalRece.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                filtroAni=spinner.getSelectedItem().toString();
                recuperaAnuncioAnimal();
                filtrandoPorBairro=true;
            }
        });

        dialoAnimalRece.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = dialoAnimalRece.create();
        dialog.show();


    }

    public  void recuperaAnuncioAnimal(){

        alertDialog = new SpotsDialog.Builder(this)

                .setMessage("Recuperando  Pets...")                .setCancelable(false)


                .show();
        listacioAnimais.clear();
    anunciosRef=ConFirebase.getFirebaseDatabase()
            .child("Anuncios_Publicos")

            .child(filtroAni);

    anunciosRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {

            for(DataSnapshot bairrro: snapshot.getChildren()) {

                for (DataSnapshot anuncios : bairrro.getChildren()) {

                    Anuncios anuncio = anuncios.getValue(Anuncios.class);
                    listacioAnimais.add(anuncio);
                }
            }
            Collections.reverse(listacioAnimais);
            adapterAnuncios.notifyDataSetChanged();
            alertDialog.dismiss();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });



    }

    public  void recuperaAnuncioPorBairro(){

        alertDialog = new SpotsDialog.Builder(this)

                .setMessage("Recuperando  Bairro")
                .setCancelable(false)


                .show();



        alertDialog.show();

        anunciosRef=ConFirebase.getFirebaseDatabase()
                .child("Anuncios_Publicos")
                .child(filtroAni)
        .child(filtroBairro);
        anunciosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listacioAnimais.clear();
                for (DataSnapshot anuncios : snapshot.getChildren()) {

                    Anuncios anuncio = anuncios.getValue(Anuncios.class);

                    listacioAnimais.add(anuncio);
                }
                Collections.reverse(listacioAnimais);
                adapterAnuncios.notifyDataSetChanged();
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void recuperaAnuncioPublicos(){
        alertDialog = new SpotsDialog.Builder(this)

                .setMessage("Recuperando  Pets...")
                .setCancelable(false)
                .show();
        alertDialog.show();


listacioAnimais.clear();
anunciosRef.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for( DataSnapshot animal:  snapshot.getChildren()){
       for(DataSnapshot bairrro: animal.getChildren()){

            for(DataSnapshot anuncios: bairrro.getChildren()){

                  Anuncios  anuncio = anuncios.getValue(Anuncios.class);

                  listacioAnimais.add(anuncio);


            }
       }



        }
        Collections.reverse(listacioAnimais);
        adapterAnuncios.notifyDataSetChanged();
        alertDialog.dismiss();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
       if(autenticacao.getCurrentUser()==null){
         menu.setGroupVisible(R.id.group_deslogado,true);

       }else{
           menu.setGroupVisible(R.id.group_logado,true);


       }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_cadastrar:
                startActivity(new Intent(getApplicationContext(), Login.class));

                break;

            case  R.id.menu_sair:
                autenticacao.signOut();
                invalidateOptionsMenu();
                break;

            case R.id.menu_perfil:
startActivity(new Intent(getApplicationContext(),Perfil.class));

                break;
            case R.id.menu_meusanimais:
                startActivity(new Intent(getApplicationContext(),MeusAnimais.class));

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void inicializarCompo() {

        recyclerViewPu = findViewById(R.id.ryclePublico);


    }
}