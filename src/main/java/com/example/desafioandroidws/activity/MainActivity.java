package com.example.desafioandroidws.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.desafioandroidws.R;
import com.example.desafioandroidws.modelo.Usuario;
import com.example.desafioandroidws.service.ServiceGenerator;
import com.example.desafioandroidws.service.ServiceInterfaceUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText nomeUser;
    private TextInputLayout textInputLayout;
    Usuario usuario = new Usuario();
    ProgressDialog progress;
    public static final String TAG = "Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textInputLayout = findViewById(R.id.textInputLayout);
        nomeUser = findViewById(R.id.textNomeUser);

        nomeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textInputLayout.setErrorEnabled(false);
                textInputLayout.setError(null);
            }
        });
    }

    //metodo do button settado no design, executa a busca pelo o usuario e abre uma dialog enquanto espera a resposta do servidor
    public void buscarUsuario(View view){
        String textUser = nomeUser.getText().toString();


        if(textUser == null || textUser.equals("")){
            textInputLayout.setErrorEnabled(true);
            textInputLayout.setError("Preencha o campo parar continuar");

        } else{
            progress = new ProgressDialog(MainActivity.this);
            progress.setTitle("enviando...");
            progress.show();

            retrofitBuscarUser();
        }

    }

    public void retrofitBuscarUser(){


        String nomeLogin = nomeUser.getText().toString();

        ServiceInterfaceUser service = ServiceGenerator.createService(ServiceInterfaceUser.class);

        Call<Usuario> call = service.buscarUser(nomeLogin);//busca o usuário com base no nome passado

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                //Verifica se de sucesso na chamada.
                if (response.isSuccessful()) {

                    Usuario respostaServidor = response.body();

                    //verifica aqui se o corpo da resposta não é nulo
                    if (respostaServidor != null) {


                        //setta os valores da resposta no objeto da classe Usuario.
                        usuario.setName(respostaServidor.getName());
                        usuario.setAvatar_url(respostaServidor.getAvatar_url());
                        usuario.setPublic_repos(respostaServidor.getPublic_repos());

                        // repassar os dados para a axtivity RepositoriosActivity.
                        Intent intent = new Intent(getApplicationContext(), RepositoriosActivity.class);
                        intent.putExtra("login", nomeLogin);
                        intent.putExtra("objeto", usuario);
                        startActivity(intent);


                    } else {
                        //resposta nula, usuário não encontrado
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError("Usuário não encontrado.");
                        Toast.makeText(getApplicationContext(),"Usuario não encontrado, verifique se você digitou corretamente.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    //Erro na chamada, usuário não encontrado.
                    ResponseBody errorBody = response.errorBody();
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError("Usuário não encontrado.");
                    Toast.makeText(getApplicationContext(),"Usuario não encontrado, verifique se você digitou corretamente.", Toast.LENGTH_SHORT).show();
                    // segura os erros de requisição
                    Log.e(TAG, "Error: "+ response.errorBody());


                }

                progress.dismiss();
            }

            //Metodo de falha na chamada
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Log.e(TAG, "Erro: "+t.getMessage());
                Toast.makeText(getApplicationContext(),"Erro na chamada ao servidor", Toast.LENGTH_SHORT).show();
                progress.dismiss();
            }
        });

    }





}