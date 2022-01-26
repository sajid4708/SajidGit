package com.sajid.zohogitapp.datasources.remote.di
import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import androidx.work.Worker
import com.google.gson.GsonBuilder
import com.sajid.zohogitapp.BuildConfig
import com.sajid.zohogitapp.datasources.local.GitDatabase
import com.sajid.zohogitapp.datasources.remote.service.GitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalModule {
    @Singleton
    @Provides
    fun providesGitDatabase(@ApplicationContext app:Context) :GitDatabase{
     return GitDatabase.getInstance(app)
    }


    @Provides
    fun provideGitDao(gitDatabase: GitDatabase)=gitDatabase.gitLocalDao()


}