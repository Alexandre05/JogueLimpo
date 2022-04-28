package br.com.joguelimpocomosanimais.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import br.com.joguelimpocomosanimais.R;


public class TelaSplach extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar tolbar= findViewById(R.id.toolbar);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_tela_splach);
        Handler handle = new Handler();

        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarLogin();
            }
        },2000);



    }

    private void mostrarLogin() {

        Intent intent = new Intent(TelaSplach.this, Animais.class);
        startActivity(intent);
        finish();
    }
}
