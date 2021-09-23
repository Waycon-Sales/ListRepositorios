package com.example.desafioandroidws.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.desafioandroidws.R;
import com.example.desafioandroidws.modelo.Repositorios;

import java.util.ArrayList;




public class AdapterRepositorios extends RecyclerView.Adapter<AdapterRepositorios.MyViewHolder> {
    private ArrayList<Repositorios> repositorios;
    private final Context contexto;

    public AdapterRepositorios(Context context, ArrayList<Repositorios> listaRepos){
        this.contexto = context;
        this.repositorios = listaRepos;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemRepositorio = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_repositorios,parent,false);

        return new MyViewHolder(itemRepositorio);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Repositorios repos =  repositorios.get(position);
        holder.nomeRepos.setText(repos.getName());


        holder.btnVisitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Abre o link do repositorio no navegador
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(repos.getHtml_url()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                contexto.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return repositorios.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nomeRepos;
        Button btnVisitar;
        public MyViewHolder(View itemView){
            super(itemView);
            nomeRepos = itemView.findViewById(R.id.textNomeRepos);
            btnVisitar = itemView.findViewById(R.id.btnVisitar);

        }
    }
}
