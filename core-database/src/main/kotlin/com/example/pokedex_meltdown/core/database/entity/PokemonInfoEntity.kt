package com.example.pokedex_meltdown.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokedex_meltdown.core.model.PokemonInfo

@Entity
data class PokemonInfoEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val experience: Int,
    val types: List<PokemonInfo.TypeResponse>,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val speed: Int,
    val exp: Int
)