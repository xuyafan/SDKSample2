package com.okokkid.net;


import com.okokkid.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {

    //获得Api
    public static Api getApi() {
        return ApiServiceHolder.API_SERVICE;
    }

    //使用私有静态内部类的静态实例实现的单例模式
    private static final class ApiServiceHolder {
        private static final Api API_SERVICE =
                RetrofitHolder.RETROFIT.create(Api.class);
    }

    //使用私有静态内部类的静态实例实现的单例模式
    private static class RetrofitHolder {
        private static final Retrofit RETROFIT = new Retrofit.Builder()
                //指定基本URL
                .baseUrl(Api.API_SERVER_URL)
                //添加Gson解析器
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //设置OkHttpClient为Client
                .client(OKHttpHolder.OK_HTTP_CLIENT)
                .build();
    }

    //使用私有静态内部类的静态实例实现的单例模式
    private static final class OKHttpHolder {
        private static final OkHttpClient.Builder BUILDER = new OkHttpClient.Builder();
        private static final OkHttpClient OK_HTTP_CLIENT = addInterceptor()
                //设置连接超时时间为5s
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();

        private static OkHttpClient.Builder addInterceptor() {
            if (BuildConfig.DEBUG) {
                // 添加Log信息拦截器，Level为BODY模式
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置 Debug Log 模式
                BUILDER.addInterceptor(loggingInterceptor);
            }
            return BUILDER;
        }
    }
}