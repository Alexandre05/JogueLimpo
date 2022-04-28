package br.com.joguelimpocomosanimais.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.joguelimpocomosanimais.Activity.Conversas;
import br.com.joguelimpocomosanimais.Adapter.AdpterContatos;
import br.com.joguelimpocomosanimais.Helper.ConFirebase;
import br.com.joguelimpocomosanimais.Helper.RecyclerItemClickListener;
import br.com.joguelimpocomosanimais.Model.Usuario;
import br.com.joguelimpocomosanimais.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContatosT01#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContatosT01 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private  RecyclerView r;
    private AdpterContatos adapter;
    private ArrayList<Usuario> listaContatos= new ArrayList<>();
    private DatabaseReference usuarioRef;
    private ValueEventListener valueEventListener;
    private FirebaseUser user;

    public ContatosT01() {
        // Required empty public constructor
    }


    public static ContatosT01 newInstance(String param1, String param2) {
        ContatosT01 fragment = new ContatosT01();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_teste01, container, false);

       user= ConFirebase.getUsuarioAtaul();
        adapter= new AdpterContatos(listaContatos,getActivity());


        // configurar recyclerview
        r = view.findViewById(R.id.recyListaContatos);
        usuarioRef= ConFirebase.getFirebaseDatabase().child("Perfil");
        RecyclerView.LayoutManager layoutManager =  new LinearLayoutManager(getActivity());
        r.setLayoutManager(layoutManager);
        r.setHasFixedSize(true);
        r.setAdapter( adapter  );

       r.addOnItemTouchListener(
               new RecyclerItemClickListener(
                       getActivity(),
                       r, new RecyclerItemClickListener.OnItemClickListener() {
                   @Override
                   public void onItemClick(View view, int position) {
                       Usuario usuarioSele = listaContatos.get(position);
                       Intent intent= new Intent(getActivity(), Conversas.class);
                       intent.putExtra("chatContato",usuarioSele);
                       startActivity(intent);
                   }

                   @Override
                   public void onLongItemClick(View view, int position) {

                   }

                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   }
               }
               )
       );
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        listaContatos.clear();
        recuparContatos();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuarioRef.removeEventListener(valueEventListener);
    }





    public  void recuparContatos() {
        valueEventListener = usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dados : snapshot.getChildren()) {


                    Usuario usuario = dados.getValue(Usuario.class);
                    String emiaUsuario = user.getEmail();
                   if(!emiaUsuario.equals(usuario.getEmail())){

                       listaContatos.add(usuario);

                   }

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}