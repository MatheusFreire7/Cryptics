package br.unicamp.appcryptics.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.15.6:3000/auth/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getAPI(){
        return retrofit.create(Api.class);
    }


}
