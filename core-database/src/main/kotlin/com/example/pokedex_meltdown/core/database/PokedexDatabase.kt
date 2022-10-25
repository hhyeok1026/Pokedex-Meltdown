package com.example.pokedex_meltdown.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pokedex_meltdown.core.database.entity.PokemonEntity
import com.example.pokedex_meltdown.core.database.entity.PokemonInfoEntity

@Database(
    entities = [PokemonEntity::class, PokemonInfoEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(value = [TypeResponseConverter::class])
abstract class PokedexDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonInfoDao(): PokemonInfoDao
}