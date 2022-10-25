package com.example.pokedex_meltdown.core.data.repository

import androidx.annotation.WorkerThread
import com.example.pokedex_meltdown.core.database.PokemonDao
import com.example.pokedex_meltdown.core.database.entity.mapper.asDomain
import com.example.pokedex_meltdown.core.database.entity.mapper.asEntity
import com.example.pokedex_meltdown.core.model.Pokemon
import com.example.pokedex_meltdown.core.network.Dispatcher
import com.example.pokedex_meltdown.core.network.PokedexAppDispatchers
import com.example.pokedex_meltdown.core.network.service.PokedexClient
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val pokedexClient: PokedexClient,
    private val pokemonDao: PokemonDao,
    @Dispatcher(PokedexAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : MainRepository {

    @WorkerThread
    override fun fetchPokemonList(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow<List<Pokemon>> {
        var pokemons = pokemonDao.getPokemonList(page).asDomain()
        if(pokemons.isEmpty()) {
            /**
             * fetches a list of [Pokemon] from the network and getting [ApiResponse] asynchronously.
             * @see [suspendOnSuccess](https://github.com/skydoves/sandwich#apiresponse-extensions-for-coroutines)
             */
            val response = pokedexClient.fetchPokemonList(page = page)
            response.suspendOnSuccess {
                pokemons = data.results
                pokemons.forEach { pokemon -> pokemon.page = page }
                pokemonDao.insertPokemonList(pokemons.asEntity())
                emit(pokemonDao.getAllPokemonList(page).asDomain())
            }.onFailure {
                onError(message())
            }
        } else {
            emit(pokemonDao.getAllPokemonList(page).asDomain())
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(ioDispatcher)
}


























