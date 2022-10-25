package com.example.pokedex_meltdown.core.data

import app.cash.turbine.test
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.example.pokedex_meltdown.core.data.repository.DetailRepositoryImpl
import com.example.pokedex_meltdown.core.database.PokemonInfoDao
import com.example.pokedex_meltdown.core.database.entity.mapper.asEntity
import com.example.pokedex_meltdown.core.network.service.PokedexClient
import com.example.pokedex_meltdown.core.network.service.PokedexService
import com.example.pokedex_meltdown.core.test.MainCoroutinesRule
import com.example.pokedex_meltdown.core.test.MockUtil.mockPokemonInfo
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class DetailRepositoryTest {

    private lateinit var repository: DetailRepositoryImpl
    private lateinit var client: PokedexClient
    private var service: PokedexService = mock()
    private var pokemonInfoDao: PokemonInfoDao = mock()

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        client = PokedexClient(service)
        repository = DetailRepositoryImpl(client, pokemonInfoDao, coroutinesRule.testDispatcher)
    }

    @Test
    fun fetchPokemonInfoFromNetworkTest() = runTest {
        val mockData = mockPokemonInfo()
        whenever(pokemonInfoDao.getPokemonInfo(name_ = "bulbasaur")).thenReturn(null)
        whenever(service.fetchPokemonInfo(name = "bulbasaur")).thenReturn(
            ApiResponse.of {
                Response.success(
                    mockData
                )
            }
        )

        repository.fetchPokemonInfo(name = "bulbasaur", onComplete = {}, onError = {}).test {
            val expectItem = awaitItem()
            assertEquals(expectItem.id, mockData.id)
            assertEquals(expectItem.name, mockData.name)
            assertEquals(expectItem, mockData)
            awaitComplete()
        }

        verify(pokemonInfoDao, atLeastOnce()).getPokemonInfo(name_ = "bulbasaur")
        verify(service, atLeastOnce()).fetchPokemonInfo(name = "bulbasaur")
        verify(pokemonInfoDao, atLeastOnce()).insertPokemonInfo(mockData.asEntity())
        verifyNoMoreInteractions(service)
    }

    @Test
    fun fetchPokemonInfoFromDatabaseTest() = runTest {
        val mockData = mockPokemonInfo()
        whenever(pokemonInfoDao.getPokemonInfo(name_ = "bulbasaur")).thenReturn(mockData.asEntity())
        whenever(service.fetchPokemonInfo(name = "bulbasaur")).thenReturn(
            ApiResponse.of {
                Response.success(
                    mockData
                )
            }
        )

        repository.fetchPokemonInfo(
            name = "bulbasaur",
            onComplete = {},
            onError = {}
        ).test(5.toDuration(DurationUnit.SECONDS)) {
            val expectItem = awaitItem()
            assertEquals(expectItem.id, mockData.id)
            assertEquals(expectItem.name, mockData.name)
            assertEquals(expectItem, mockData)
            awaitComplete()
        }

        verify(pokemonInfoDao, atLeastOnce()).getPokemonInfo(name_ = "bulbasaur")
        verifyNoMoreInteractions(service)
    }
}






























