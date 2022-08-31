package com.magnificsoftware.letswatch.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import com.magnificsoftware.letswatch.api.LetsWatchAsyncService
import com.magnificsoftware.letswatch.api.LetsWatchService
import com.magnificsoftware.letswatch.commons.ApiUrls
import com.magnificsoftware.letswatch.data.LetsWatchDb
import com.magnificsoftware.letswatch.data.UserDao
import com.magnificsoftware.letswatch.data.WatchlistDao
import com.magnificsoftware.letswatch.util.async_future.FutureDataCallAdapterFactory
import com.magnificsoftware.letswatch.util.network_livedata.LiveDataCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    companion object {
        private const val dbVersion = "v1"
        private const val LetsWatchDbName = "letsWatch.$dbVersion.db"
    }

    @Singleton
    @Provides
    fun provideLetsWatchService(): LetsWatchService {
        val client = OkHttpClient()

        return Retrofit.Builder()
            .baseUrl(ApiUrls.main)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()
            .create(LetsWatchService::class.java)
    }

    @Singleton
    @Provides
    fun provideLetsWatchAuthService(): LetsWatchAsyncService {
        val client = OkHttpClient()

        return Retrofit.Builder()
            .baseUrl(ApiUrls.main)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(FutureDataCallAdapterFactory())
            .client(client)
            .build()
            .create(LetsWatchAsyncService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext appContext: Context): LetsWatchDb {
        return Room
            .databaseBuilder(appContext, LetsWatchDb::class.java, LetsWatchDbName)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(db: LetsWatchDb): UserDao {
        return db.userDao()
    }

    @Singleton
    @Provides
    fun provideWatchlistDao(db: LetsWatchDb): WatchlistDao {
        return db.watchlistDao()
    }
}