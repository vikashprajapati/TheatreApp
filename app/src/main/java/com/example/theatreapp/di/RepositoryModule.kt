package com.example.theatreapp.di

import android.content.Context
import com.vikash.syncr_core.data.repository.YoutubeRepository
import com.vikash.syncr_core.network.NetworkDataSource
import com.vikash.syncr_core.network.YoutubeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun providesYoutubeRepository(
        @ApplicationContext context: Context
    ) : YoutubeRepository{
        return YoutubeRepository(NetworkDataSource(YoutubeApi()), context)
    }

}