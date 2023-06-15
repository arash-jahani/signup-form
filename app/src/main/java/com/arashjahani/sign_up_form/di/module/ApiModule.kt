package com.arashjahani.sign_up_form.di.module

import android.content.Context
import com.arashjahani.sig_up_form.BuildConfig
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.arashjahani.sign_up_form.dao.api.ApiService
import com.arashjahani.sign_up_form.dao.api.interceptor.FakeInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    @Named("okhttp_client")
    fun providesOKHttpClient(context: Context): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        var mOkHttpClient = OkHttpClient.Builder()

        mOkHttpClient.apply {
            addInterceptor(httpLoggingInterceptor)
            addInterceptor(FakeInterceptor())
            retryOnConnectionFailure(true)
            readTimeout(30, TimeUnit.SECONDS)
            connectTimeout(20, TimeUnit.SECONDS)
        }


        return mOkHttpClient.build()
    }


    @Provides
    @Singleton
    @Named("retrofit")
    fun providesApiRetrofit(@Named("okhttp_client")okHttpClient: OkHttpClient, gsonBuilder: GsonBuilder): Retrofit {
        var retrofit = Retrofit.Builder()

        retrofit.apply {
            baseUrl("http://signup-form.com")
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
        }

        return retrofit.build()
    }




    @Provides
    @Singleton
    fun providesApiService(@Named("retrofit")retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Provides
    @Singleton
    fun providesGsonBuilder(): GsonBuilder {
        val gsonBuilder = GsonBuilder()

        return gsonBuilder
    }


}