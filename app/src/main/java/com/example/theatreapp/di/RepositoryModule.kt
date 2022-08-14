package com.example.theatreapp.di

import android.content.Context
import com.vikash.syncr_core.data.repository.YoutubeRepository
import com.vikash.syncr_core.network.NetworkDataSource
import com.vikash.syncr_core.network.YoutubeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun providesYoutubeRepository(context: Context, youtubeApi: YoutubeApi) : YoutubeRepository{
        return YoutubeRepository(NetworkDataSource(youtubeApi), context)
    }

}