package br.unicamp.appcryptics.API;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {
    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> registerUser(
        @Field("username") String username,
                @Field("email") String email,
                        @Field("senha") String senha
    );

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginUser(
            @Field("email") String email,
            @Field("senha") String senha
    );

}
