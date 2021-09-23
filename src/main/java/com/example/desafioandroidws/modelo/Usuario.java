package com.example.desafioandroidws.modelo;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String name; //Nome de usuario
    private String avatar_url; //Imagem de perfil
    private int public_repos; //Quantidade de repositorios publicos

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public int getPublic_repos() {
        return public_repos;
    }

    public void setPublic_repos(int public_repos) {
        this.public_repos = public_repos;
    }
}
