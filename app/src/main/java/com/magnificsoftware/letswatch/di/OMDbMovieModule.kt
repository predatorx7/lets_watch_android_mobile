package com.magnificsoftware.letswatch.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.magnificsoftware.letswatch.api.OMDbService
import com.magnificsoftware.letswatch.util.network_livedata.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OMDbMovieModule {

    /*
    * Provides mock data from OMDb APIs for UI development when APIs are not ready
    */
    @Singleton
    @Provides
    // @ViewModelScoped
    fun provideOMDbService(): OMDbService {
        val client = OkHttpClient()

        return Retrofit.Builder()
            .baseUrl(OMDbService.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()
            .create(OMDbService::class.java)
    }
}