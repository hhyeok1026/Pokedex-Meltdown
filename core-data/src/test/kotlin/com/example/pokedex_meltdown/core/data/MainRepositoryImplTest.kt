package com.example.pokedex_meltdown.core.data

import app.cash.turbine.test
import com.example.pokedex_meltdown.core.data.repository.MainRepositoryImpl
import com.example.pokedex_meltdown.core.database.PokemonDao
import com.example.pokedex_meltdown.core.database.entity.mapper.asEntity
import com.example.pokedex_meltdown.core.network.model.PokemonResponse
import com.example.pokedex_meltdown.core.network.service.PokedexClient
import com.example.pokedex_meltdown.core.network.service.PokedexService
import com.example.pokedex_meltdown.core.test.MainCoroutinesRule
import com.example.pokedex_meltdown.core.test.MockUtil.mockPokemonList
import com.nhaarman.mockitokotlin2.*
import com.skydoves.sandwich.ApiResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MainRepositoryImplTest {

    private lateinit var repository: MainRepositoryImpl
    private lateinit var client: PokedexClient
    private val service: PokedexService = mock()
    private val pokemonDao: PokemonDao = mock()

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        client = PokedexClient(service)
        repository = MainRepositoryImpl(client, pokemonDao, coroutinesRule.testDispatcher)
    }

    @Test
    fun fetchPokemonListFromNetworkTest() = runTest {
        val mockData =
            PokemonResponse(count = 984, next = null, previous = null, results = mockPokemonList())
        whenever(pokemonDao.getPokemonList(page_ = 0)).thenReturn(emptyList())
        whenever(pokemonDao.getAllPokemonList(page_ = 0)).thenReturn(mockData.results.asEntity())
        whenever(service.fetchPokemonList()).thenReturn(ApiResponse.of { Response.success(mockData) })

        repository.fetchPokemonList(
            page = 0,
            onStart = {},
            onComplete = {},
            onError = {}
        ).test(2.toDuration(DurationUnit.SECONDS)) {
            val expectItem = awaitItem()[0]
            Assert.assertEquals(expectItem.page, 0)
            Assert.assertEquals(expectItem.name, "bulbasaur")
            assertEquals(expectItem, mockPokemonList()[0])
            awaitComplete()
        }

        verify(pokemonDao, atLeastOnce()).getPokemonList(page_ = 0)
        verify(service, atLeastOnce()).fetchPokemonList()
        verify(pokemonDao, atLeastOnce()).insertPokemonList(mockData.results.asEntity())
        verifyNoMoreInteractions(service)
    }

    @Test
    fun fetchPokemonListFromDatabaseTest() = runTest {
        val mockData =
            PokemonResponse(count = 984, next = null, previous = null, results = mockPokemonList())
        whenever(pokemonDao.getPokemonList(page_ = 0)).thenReturn(mockData.results.asEntity())
        whenever(pokemonDao.getAllPokemonList(page_ = 0)).thenReturn(mockData.results.asEntity())

        repository.fetchPokemonList(
            page = 0,
            onStart = {},
            onComplete = {},
            onError = {}
        ).test(2.toDuration(DurationUnit.SECONDS)) {
            val expectItem = awaitItem()[0]
            Assert.assertEquals(expectItem.page, 0)
            Assert.assertEquals(expectItem.name, "bulbasaur")
            assertEquals(expectItem, mockPokemonList()[0])
            awaitComplete()
        }

        verify(pokemonDao, atLeastOnce()).getPokemonList(page_ = 0)
        verify(pokemonDao, atLeastOnce()).getAllPokemonList(page_ = 0)
    }
}