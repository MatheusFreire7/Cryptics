package br.unicamp.appcryptics;

public class Usuario
{
    String fotoPerfil, username, email, senha, userId, ultimaMensagem,status;

    public Usuario()
    {

    }

    public Usuario(String fotoPerfil, String username, String email, String senha, String userId, String ultimaMensagem, String status) {
        this.fotoPerfil = fotoPerfil;
        this.username = username;
        this.email = email;
        this.senha = senha;
        this.userId = userId;
        this.ultimaMensagem = ultimaMensagem;
        this.status = status;
    }

    public Usuario(String username, String senha, String email) {
        this.username = username;
        this.email = email;
        this.senha = senha;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public void setUltimaMensagem(String ultimaMensagem) {
        this.ultimaMensagem = ultimaMensagem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
