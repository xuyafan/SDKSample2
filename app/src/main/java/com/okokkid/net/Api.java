package com.okokkid.net;


import com.okokkid.vo.LoginVO;
import com.okokkid.vo.RegisterVO;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Api {

    //baseUrl
    String API_SERVER_URL = "https://api.okokkid.com";


    @POST("/ali/sms/register/{phone}")
    Observable<String> sendRegisterSms(@Path("phone") String phone);

    @POST("/ali/sms/login/{phone}")
    Observable<String> sendLoginSms(@Path("phone") String phone);

    @GET("/auth/exist/phone/{number}")
    Observable<String> exist(@Path("number") String number);

    @POST("/auth/create/phone")
    Observable<String> register(@Body RegisterVO body);

    @POST("/auth/login")
    Observable<String> login(@Body LoginVO body);


}
