package com.example.pokedex_meltdown.core.database.di

import android.app.Application
import androidx.room.Room
import com.example.pokedex_meltdown.core.database.PokedexDatabase
import com.example.pokedex_meltdown.core.database.PokemonDao
import com.example.pokedex_meltdown.core.database.PokemonInfoDao
import com.example.pokedex_meltdown.core.database.TypeResponseConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application,
        typeResponseConverter: TypeResponseConverter
    ): PokedexDatabase {
        return Room
            .databaseBuilder(application, PokedexDatabase::class.java, "Pokedex.db")
            .fallbackToDestructiveMigration()
            .addTypeConverter(typeResponseConverter)
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonDao(appDatabase: PokedexDatabase): PokemonDao {
        return appDatabase.pokemonDao()
    }

    @Provides
    @Singleton
    fun providePokemonInfoDao(appDatabase: PokedexDatabase): PokemonInfoDao {
        return appDatabase.pokemonInfoDao()
    }

    @Provides
    @Singleton
    fun provideTypeResponseConverter(moshi: Moshi): TypeResponseConverter {
        return TypeResponseConverter(moshi)
    }
}
























