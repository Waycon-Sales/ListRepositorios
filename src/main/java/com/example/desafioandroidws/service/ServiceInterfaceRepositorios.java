package com.example.desafioandroidws.service;

import com.example.desafioandroidws.modelo.Repositorios;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceInterfaceRepositorios {
    //busca repositorios com base no nome do usuario e na pagina.
    @GET("{nomeUser}/repos?")
    Call<ArrayList<Repositorios>> buscarRepositorios(@Path("nomeUser") String nomeUser ,@Query("page") int page);

}
