package com.example.desafioandroidws.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;


import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.desafioandroidws.R;
import com.example.desafioandroidws.adapter.AdapterRepositorios;
import com.example.desafioandroidws.modelo.Repositorios;
import com.example.desafioandroidws.modelo.Usuario;
import com.example.desafioandroidws.service.ServiceGenerator;
import com.example.desafioandroidws.service.ServiceInterfaceRepositorios;



import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RepositoriosActivity extends AppCompatActivity {

    private TextView nomeUsuario;
    private RecyclerView recyclerView;
    private ImageView imageUser;
    private String loginNome;

    private Button[] botoes;
    private int numeroPaginas;
    private LinearLayout linearLayout;



    public static final String TAG = "Tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repositorios);

        nomeUsuario = findViewById(R.id.textNomeUsuario);
        imageUser = findViewById(R.id.imageUser);
        recyclerView = findViewById(R.id.recyclerRepositorios);
        linearLayout = findViewById(R.id.btnLayout);


        //Recuperar dados enviados pela activity main
        Bundle dados = getIntent().getExtras();
        loginNome = dados.getString("login");
        Usuario usuario = (Usuario) dados.getSerializable("objeto");

        //Utilizando valores recuperados

        nomeUsuario.setText(usuario.getName());

        Glide.with(this)
                .asBitmap()
                .load(usuario.getAvatar_url())
                .circleCrop()
                .into(imageUser);

        //Configura recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        //Chama o metodo que busca repositorios passando como parametro o nome de login do usuário e a primeira pagina.
        retrofitBuscarRepos(loginNome,1);

        //Metodo para paginação dos repositórios
        quantidadePaginas(usuario.getPublic_repos(), loginNome);

    }

    //Metodo de buscar dos repositórios
    public void retrofitBuscarRepos(String nUser, int pagina){

        ServiceInterfaceRepositorios service = ServiceGenerator.createService(ServiceInterfaceRepositorios.class);

        Call<ArrayList<Repositorios>> call = service.buscarRepositorios(nUser, pagina); //chama o metodo de buscar os repositorios e tem como parametro o nome de login do usuário.

        call.enqueue(new Callback<ArrayList<Repositorios>>() {
            @Override
            public void onResponse(Call<ArrayList<Repositorios>> call, Response<ArrayList<Repositorios>> response) {

                //Verifica se de sucesso na chamada.
                if (response.isSuccessful()) {

                    ArrayList<Repositorios> respostaServidor = response.body();

                    //verifica aqui se o corpo da resposta não é nulo
                    if (respostaServidor != null) {
                        //Passar os dados para o adapter e setta a recyclerView.
                        AdapterRepositorios adapterRepositorios = new AdapterRepositorios(getApplicationContext(), respostaServidor);
                        recyclerView.setAdapter(adapterRepositorios);

                    } else {
                        //resposta nula significa que o usuario não tem repositorios
                        Toast.makeText(getApplicationContext(),"Este usuário não contem repositórios ainda.", Toast.LENGTH_LONG).show();
                    }

                } else {

                    Toast.makeText(getApplicationContext(),"Erro ao recuperar dados dos repositorios", Toast.LENGTH_LONG).show();
                    // segura os erros de requisição
                    ResponseBody errorBody = response.errorBody();
                    Log.e(TAG, "Error: "+ errorBody);

                }

            }

            //Metodo de falha na chamada
            @Override
            public void onFailure(Call<ArrayList<Repositorios>> call, Throwable t) {
                Log.e(TAG, "Erro: "+t.getMessage());
                Toast.makeText(getApplicationContext(),"Erro na chamada ao servidor", Toast.LENGTH_SHORT).show();

            }
        });

    }

    //fecha a activity e retorna para a activity abaixo dela (MainActivity)
    public void btnVoltar(View view){
        finish();
    }

    // Calcula a quantidade de paginas baseada na quantidade de repositorios;
    // Cria os botões de paginação referente a quantidade de paginas;
    // Configura o onclick dos botões para chamar o metodo de busca dos repositórios em determinada pagina.
    @SuppressLint("ResourceAsColor")
    public void quantidadePaginas(int quantidadeRepos, String nomeUser) {
        int resto = quantidadeRepos % 30;
        if (resto != 0) {
            resto = 1;
        }
        numeroPaginas = quantidadeRepos / 30 + resto;

        if (numeroPaginas > 1) {
            botoes = new Button[numeroPaginas];
            if (numeroPaginas <= 4) {

                HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(HorizontalScrollView.LayoutParams.WRAP_CONTENT, HorizontalScrollView.LayoutParams.WRAP_CONTENT);
                params.gravity = Gravity.CENTER;
                linearLayout.setLayoutParams(params);

            }

            for (int i = 0; i < numeroPaginas; i++) {
                //configura o visão e o texto dos botões
                botoes[i] = new Button(this);
                botoes[i].setText("" + (i + 1));
                botoes[i].setTextColor(getResources().getColor(R.color.black));

                //Configura parametos de constrints e margem dos botões
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMarginEnd(7);

                linearLayout.addView(botoes[i], lp); //adiciona os botões ao pai, linearLayout.

                //Setta o onClickListener para chamar o metodo de busca dos repositorios passando o nome de usuário e a página.
                final int j = i;
                botoes[j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        retrofitBuscarRepos(nomeUser, (j + 1));

                    }
                });
            }

        }
    }
}
