package com.sajid.zohogitapp.datasources.local.di
import android.content.Context
import com.sajid.zohogitapp.datasources.local.GitDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
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