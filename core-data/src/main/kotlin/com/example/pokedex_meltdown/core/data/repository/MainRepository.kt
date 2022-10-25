package com.example.pokedex_meltdown.core.data.repository

import androidx.annotation.WorkerThread
import com.example.pokedex_meltdown.core.model.Pokemon
import kotlinx.coroutines.flow.Flow


interface MainRepository {

    @WorkerThread
    fun fetchPokemonList(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ): Flow<List<Pokemon>>
}