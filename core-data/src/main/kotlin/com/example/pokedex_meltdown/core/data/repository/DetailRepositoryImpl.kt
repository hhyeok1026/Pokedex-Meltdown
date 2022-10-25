package com.example.pokedex_meltdown.core.data.repository

import androidx.annotation.WorkerThread
import com.example.pokedex_meltdown.core.database.PokemonInfoDao
import com.example.pokedex_meltdown.core.database.entity.mapper.asDomain
import com.example.pokedex_meltdown.core.database.entity.mapper.asEntity
import com.example.pokedex_meltdown.core.model.PokemonInfo
import com.example.pokedex_meltdown.core.network.Dispatcher
import com.example.pokedex_meltdown.core.network.PokedexAppDispatchers
import com.example.pokedex_meltdown.core.network.model.mapper.ErrorResponseMapper
import com.example.pokedex_meltdown.core.network.service.PokedexClient
import com.skydoves.sandwich.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val pokedexClient: PokedexClient,
    private val pokemonInfoDao: PokemonInfoDao,
    @Dispatcher(PokedexAppDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : DetailRepository {

    @WorkerThread
    override fun fetchPokemonInfo(
        name: String,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val pokemonInfo = pokemonInfoDao.getPokemonInfo(name)
        if (pokemonInfo == null) {
            /**
             * fetches a [PokemonInfo] from the network and getting [ApiResponse] asynchronously.
             * @see [suspendOnSuccess](https://github.com/skydoves/sandwich#apiresponse-extensions-for-coroutines)
             */
            val response = pokedexClient.fetchPokemonInfo(name= name)
            response.suspendOnSuccess {
                pokemonInfoDao.insertPokemonInfo(data.asEntity())
                emit(data)
            }
            // handles the case when the API request gets an error response.
            // e.g., internal server error.
            .onError {
                /** maps the [ApiResponse.Failure.Error] to the [PokemonErrorResponse] using the mapper. */
                map(ErrorResponseMapper) { onError("[Code: $code]: $message") }
            }
            .onException { onError(message) }
        } else {
            emit(pokemonInfo.asDomain())
        }
    }.onCompletion { onComplete() }.flowOn(ioDispatcher)
}