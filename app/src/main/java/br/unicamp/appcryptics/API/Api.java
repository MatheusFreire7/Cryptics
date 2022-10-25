package br.unicamp.appcryptics.API;

import br.unicamp.appcryptics.Usuario;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {
    //Cadastrar um usuário
    @POST("/auth/register")
    Call<Usuario> registerUser(@Body Usuario user);

    //Login de um usuário
    @POST("/auth/login")
    Call<Usuario> loginUser(@Body Usuario user);

    //Alterar o usuário
    @PUT("/auth/put/{email}")
    Call<Usuario> alterarUser(@Path("email")String email, @Body Usuario user);

    //deletar um usuário
    @DELETE("/auth/delete/{email}")
    Call<Usuario> excluirUser(@Path("email") String email);

}
