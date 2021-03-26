package com.example.progivavaliacaofinal.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.progivavaliacaofinal.R;
import com.example.progivavaliacaofinal.helper.ConfiguracaoFirebase;
import com.example.progivavaliacaofinal.helper.Permissoes;
import com.example.progivavaliacaofinal.model.Anuncio;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.storage.StorageReference;

import java.util.Currency;
import java.util.Locale;

public class CadastrarAnuncioActivity extends AppCompatActivity {



    private EditText campoTitulo, campoDescricao;
    private CurrencyEditText campoValor;
   private Anuncio anuncio;
    private Spinner campoEstado,campoCategoria;
    private StorageReference storage;



    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_anuncio);

        storage= ConfiguracaoFirebase.getFirebaseStorage();

        //Validar permissões
        Permissoes.validarPermissoes(permissoes, this, 1);

        inicializarComponentes();

        carregarDadosSpinner();


    }

    public void salvarAnuncio(View view){
        String valor=campoValor.getText().toString();
        Log.d("salvar","salvarAnuncio"+valor);

    }
    private Anuncio configurarAnuncio(){
        String estado=campoEstado.getSelectedItem().toString();
        String categoria=campoCategoria.getSelectedItem().toString();
        String titulo=campoTitulo.getText().toString();
        String valor=String.valueOf(campoValor.getRawValue());
        String descricao=campoDescricao.getText().toString();

        Anuncio anuncio=new Anuncio();
        anuncio.setEstado(estado);
        anuncio.setCategoria(categoria);
        anuncio.setTitulo(titulo);
        anuncio.setValor(valor);
        anuncio.setDescricao(descricao);

        return anuncio;
    }

    public void validarDadosAnuncios(View view){
       String estado=campoEstado.getSelectedItem().toString();
        String categoria=campoCategoria.getSelectedItem().toString();
        String titulo=campoTitulo.getText().toString();
        String valor=String.valueOf(campoValor.getRawValue());
        String descricao=campoDescricao.getText().toString();
    }




     private void carregarDadosSpinner(){
         String[] estados = getResources().getStringArray(R.array.estados);
         ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                 this, android.R.layout.simple_spinner_item,
                 estados
         );
         adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
         campoEstado.setAdapter( adapter );

         //Configura spinner de categorias
         String[] categorias = getResources().getStringArray(R.array.categorias);
         ArrayAdapter<String> adapterCategoria = new ArrayAdapter<String>(
                 this, android.R.layout.simple_spinner_item,
                 categorias
         );
         adapterCategoria.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
         campoCategoria.setAdapter( adapterCategoria );

     }

    private void inicializarComponentes(){

        campoTitulo = findViewById(R.id.editTitulo);
        campoDescricao = findViewById(R.id.editDescricao);
        campoValor = findViewById(R.id.editValor);
        campoEstado=findViewById(R.id.spinnerEstado);
        campoCategoria=findViewById(R.id.spinnerCategoria);



        //Configura localidade para pt -> portugues BR -> Brasil
        Locale locale = new Locale("pt", "BR");
        campoValor.setLocale( locale );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for( int permissaoResultado : grantResults ){
            if( permissaoResultado == PackageManager.PERMISSION_DENIED){
                alertaValidacaoPermissao();
            }
        }

    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
