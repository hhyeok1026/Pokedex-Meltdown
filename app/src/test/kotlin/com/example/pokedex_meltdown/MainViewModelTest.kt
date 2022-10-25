package com.example.pokedex_meltdown

import app.cash.turbine.test
import com.example.pokedex_meltdown.core.data.repository.MainRepository
import com.example.pokedex_meltdown.core.data.repository.MainRepositoryImpl
import com.example.pokedex_meltdown.core.database.PokemonDao
import com.example.pokedex_meltdown.core.database.entity.mapper.asEntity
import com.example.pokedex_meltdown.core.network.service.PokedexClient
import com.example.pokedex_meltdown.core.network.service.PokedexService
import com.example.pokedex_meltdown.core.test.MainCoroutinesRule
import com.example.pokedex_meltdown.core.test.MockUtil
import com.example.pokedex_meltdown.ui.main.MainViewModel
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.DurationUnit
import kotlin.time.toDuration


class MainViewModelTest {

    private lateinit var viewModel: MainViewModel
    private lateinit var mainRepository: MainRepository
    private val pokedexService: PokedexService = mock()
    private val pokdexClient: PokedexClient = PokedexClient(pokedexService)
    private val pokemonDao: PokemonDao = mock()

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        mainRepository = MainRepositoryImpl(pokdexClient, pokemonDao, coroutinesRule.testDispatcher)
        viewModel = MainViewModel(mainRepository)
    }

    @Test
    fun fetchPokemonListTest() = runTest {
        val mockData = MockUtil.mockPokemonList()
        whenever(pokemonDao.getPokemonList(page_ = 0)).thenReturn(mockData.asEntity())
        whenever(pokemonDao.getAllPokemonList(page_ = 0)).thenReturn(mockData.asEntity())

        mainRepository.fetchPokemonList(
            page = 0,
            onStart = {},
            onComplete = {},
            onError = {}
        ).test(2.toDuration(DurationUnit.SECONDS)) {
            val item = awaitItem()
            Assert.assertEquals(item[0].page, 0)
            Assert.assertEquals(item[0].name, "bulbasaur")
            Assert.assertEquals(item, MockUtil.mockPokemonList())
            awaitComplete()
        }

        viewModel.fetchNextPokemonList()

        verify(pokemonDao, atLeastOnce()).getPokemonList(page_ = 0)
    }
}