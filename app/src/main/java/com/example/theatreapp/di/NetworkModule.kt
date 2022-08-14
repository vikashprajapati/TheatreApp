package com.example.theatreapp.di

import com.vikash.syncr_core.network.YoutubeApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providesYoutubeApi() : YoutubeApi = YoutubeApi()

}