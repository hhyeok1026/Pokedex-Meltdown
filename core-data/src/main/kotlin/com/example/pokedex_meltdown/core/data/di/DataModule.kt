package com.example.pokedex_meltdown.core.data.di

import com.example.pokedex_meltdown.core.data.repository.DetailRepository
import com.example.pokedex_meltdown.core.data.repository.DetailRepositoryImpl
import com.example.pokedex_meltdown.core.data.repository.MainRepository
import com.example.pokedex_meltdown.core.data.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsMainRepository(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository

    @Binds
    fun bindDetailRepository(
        detailRepositoryImpl: DetailRepositoryImpl
    ): DetailRepository
}