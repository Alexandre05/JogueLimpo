package br.com.joguelimpocomosanimais.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import br.com.joguelimpocomosanimais.Activity.Conversas;
import br.com.joguelimpocomosanimais.Adapter.ConversaAdpter;
import br.com.joguelimpocomosanimais.Helper.ConFirebase;
import br.com.joguelimpocomosanimais.Helper.RecyclerItemClickListener;
import br.com.joguelimpocomosanimais.Model.ConversaAssunto;
import br.com.joguelimpocomosanimais.R;


public class ConversaFragment extends Fragment {

    private RecyclerView recyclerViewC;
    private List<ConversaAssunto> listasConversas = new ArrayList<>();
    private ConversaAdpter adpter;
    private DatabaseReference data;
    private DatabaseReference conversaRef;
    private ChildEventListener childEventListenerConversas;

    public ConversaFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversa, container, false);
        recyclerViewC = view.findViewById(R.id.recyListaCon);

        adpter = new ConversaAdpter(listasConversas, getActivity());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewC.setLayoutManager(layoutManager);
        recyclerViewC.setHasFixedSize(true);
        recyclerViewC.setAdapter(adpter);


  recyclerViewC.addOnItemTouchListener(new RecyclerItemClickListener(
          getActivity(),
          recyclerViewC,
          new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                  ConversaAssunto conversaAssunto= listasConversas.get(position);

                  Intent i = new Intent(getActivity(), Conversas.class);
                  i.putExtra("chatContato",conversaAssunto.getUruarioExibicao());
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

        String ideUsarui = ConFirebase.getIdentificarUsaurio();
        data = ConFirebase.getFirebaseDatabase();
        conversaRef = data.child("conversas")
                .child(ideUsarui);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recuparConversar();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversaRef.removeEventListener(childEventListenerConversas);
        listasConversas.clear();


    }

    public void recuparConversar(){

        childEventListenerConversas =  conversaRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                // Recuperar conversas
                ConversaAssunto conversa = dataSnapshot.getValue( ConversaAssunto.class );
                listasConversas.add( conversa );
                adpter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}