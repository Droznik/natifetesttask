package com.example.giphyfornatife.di

import android.content.Context
import androidx.room.Room
import com.example.giphyfornatife.data.api.ApiKeyInterceptor
import com.example.giphyfornatife.data.api.GiphyApiImpl
import com.example.giphyfornatife.data.database.GifDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Named("BASE_URL")
    fun provideBaseUrl(): String = "https://api.giphy.com/v1/"

    @Provides
    @Named("API_KEY")
    fun provideApiKey(): String = "ezMp1se8HgpdEaUp3bJDRNryJg0oweAg"

    @Provides
    fun provideInterceptor(@Named("API_KEY") apiKey: String): Interceptor =
        ApiKeyInterceptor(apiKey)

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, @Named("BASE_URL") BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): GiphyApiImpl = retrofit.create(GiphyApiImpl::class.java)

    @Provides
    @Singleton
    fun provideDb(@ApplicationContext appContext: Context): GifDb =
        Room.databaseBuilder(appContext, GifDb::class.java, "deleted_gifs_database")
            .fallbackToDestructiveMigration()
            .build()

}