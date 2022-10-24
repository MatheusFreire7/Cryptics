package br.unicamp.appcryptics.API;

import br.unicamp.appcryptics.Usuario;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @POST("/auth/register")
    Call<Usuario> registerUser(@Body Usuario user);

    @POST("/auth/login")
    Call<Usuario> loginUser(@Body Usuario user);

}
