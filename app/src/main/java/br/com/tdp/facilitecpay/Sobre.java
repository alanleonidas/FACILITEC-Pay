package br.com.tdp.facilitecpay;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Objects;

public class Sobre extends AppCompatActivity {
    private TextView textNumeroSerie;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        permissoes();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Informações");
        setContentView(R.layout.activity_sobre);
        textNumeroSerie = (TextView)findViewById(R.id.textNUmeroSerie);
        carregaParametro();
    }

    public void permissoes() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
            } else {
                int PERMISSAO_REQUEST = 128;
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSAO_REQUEST);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void carregaParametro(){
        //Bundle bundle = getIntent().getExtras();
        String serial;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            serial = Build.getSerial();
        } else {
            serial = Build.SERIAL;
        }
        textNumeroSerie.setText(serial);

        /*if ((bundle != null) && (bundle.containsKey("SERVER"))){
            boletim = (BoletimTecnico)bundle.getSerializable("BOLETIM");

            tvdResumo.setText(boletim.getBT_RESUMONOTA());
            tvdDetalhes.setText(boletim.getBT_DETALHESNOTA());
            tvdParametros.setText(boletim.getBT_PARAMENVOLV());
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sobre, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_item_share:
                startActivity(getCompartilhamento());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent getCompartilhamento(){
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TITLE,"Compartilhar Número de Série");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Número de Série: "+textNumeroSerie.getText());
        shareIntent.setType("text/plain");
        return shareIntent;
    }
}