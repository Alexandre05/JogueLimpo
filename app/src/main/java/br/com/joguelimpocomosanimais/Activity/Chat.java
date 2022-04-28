package br.com.joguelimpocomosanimais.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;


import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

import br.com.joguelimpocomosanimais.Fragment.ContatosT01;
import br.com.joguelimpocomosanimais.Fragment.ConversaFragment;
import br.com.joguelimpocomosanimais.Model.Anuncios;
import br.com.joguelimpocomosanimais.Model.Notificacao;
import br.com.joguelimpocomosanimais.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Chat extends AppCompatActivity {
    private Retrofit retrofit;
    private String baseUrl;
    private TextView nome;
    private  ViewPager viewPager;
    private SmartTabLayout smartTabLayout;
    private List<Anuncios> listaciContatoVenda = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        viewPager= findViewById(R.id.viewPager);
        smartTabLayout= findViewById(R.id.viewPagerTab);
        baseUrl="https://fcm.googleapis.com/fcm/";
        retrofit= new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Chat J.L.A");

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(

                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Conversas", ConversaFragment.class)
                        .add("Contatos", ContatosT01.class)
                .create()

        );

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        SmartTabLayout view=findViewById(R.id.viewPagerTab);
        view.setViewPager(viewPager);


    }



}