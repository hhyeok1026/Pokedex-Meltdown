/*
 * Designed and developed by 2022 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.pokedex_meltdown

import app.cash.turbine.test
import com.example.pokedex_meltdown.core.data.repository.DetailRepository
import com.example.pokedex_meltdown.core.data.repository.DetailRepositoryImpl
import com.example.pokedex_meltdown.core.database.PokemonInfoDao
import com.example.pokedex_meltdown.core.database.entity.mapper.asEntity
import com.example.pokedex_meltdown.core.network.service.PokedexClient
import com.example.pokedex_meltdown.core.network.service.PokedexService
import com.example.pokedex_meltdown.core.test.MainCoroutinesRule
import com.example.pokedex_meltdown.core.test.MockUtil
import com.example.pokedex_meltdown.ui.details.DetailViewModel
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

class DetailViewModelTest {

    private lateinit var viewModel: DetailViewModel
    private lateinit var detailRepository: DetailRepository
    private val pokedexService: PokedexService = mock()
    private val pokedexClient: PokedexClient = PokedexClient(pokedexService)
    private val pokemonInfoDao: PokemonInfoDao = mock()

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        detailRepository =
            DetailRepositoryImpl(pokedexClient, pokemonInfoDao, coroutinesRule.testDispatcher)
        viewModel = DetailViewModel(detailRepository, "bulbasaur")
    }

    @Test
    fun fetchPokemonInfoTest() = runTest {
        val mockData = MockUtil.mockPokemonInfo()
        whenever(pokemonInfoDao.getPokemonInfo(name_ = "bulbasaur")).thenReturn(mockData.asEntity())

        detailRepository.fetchPokemonInfo(
            name = "bulbasaur",
            onComplete = { },
            onError = { }
        ).test(2.toDuration(DurationUnit.SECONDS)) {
            val item = awaitItem()
            Assert.assertEquals(item.id, mockData.id)
            Assert.assertEquals(item.name, mockData.name)
            Assert.assertEquals(item, mockData)
            awaitComplete()
        }

        verify(pokemonInfoDao, atLeastOnce()).getPokemonInfo(name_ = "bulbasaur")
    }
}
