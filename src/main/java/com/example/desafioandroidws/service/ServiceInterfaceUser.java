package com.example.desafioandroidws.service;

import com.example.desafioandroidws.modelo.Usuario;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ServiceInterfaceUser {
    //busca o usuário com base no nome.
    @GET ("{nomeUser}")
    Call<Usuario> buscarUser(@Path("nomeUser") String nomeUser);
}
