//package com.buildsol.bottolshort.game.presentation
//
//import androidx.compose.runtime.mutableStateListOf
//import app.cash.turbine.test
//import assertk.assertThat
//import assertk.assertions.isEqualTo
//import com.buildsol.bottolshort.game.data.DefaultLevelConfig
//import com.buildsol.bottolshort.game.data.LevelConfig
//import com.buildsol.bottolshort.game.data.SettingsRepositoryImpl
//import com.buildsol.bottolshort.game.domain.model.Bottle
//import com.buildsol.bottolshort.game.domain.model.Color
//import com.buildsol.bottolshort.game.domain.repository.LevelGenerator
//import com.buildsol.bottolshort.game.presentation.action.GameAction
//import com.buildsol.bottolshort.game.presentation.event.GameEvent
//import com.buildsol.bottolshort.game.presentation.viewmodel.BottleGameViewModel
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.resetMain
//import kotlinx.coroutines.test.setMain
//import org.junit.After
//import org.junit.Before
//import org.junit.Test
//import kotlinx.coroutines.test.runTest
//
//@OptIn(ExperimentalCoroutinesApi::class)
//class BottleGameViewModelTest {
//
//    private val testDispatcher = StandardTestDispatcher()
//    private lateinit var viewModel: BottleGameViewModel
//    private lateinit var fakeGenerator: FakeLevelGenerator
//
//    @Before
//    fun setUp() {
//        Dispatchers.setMain(testDispatcher)
//        fakeGenerator = FakeLevelGenerator()
//        viewModel = BottleGameViewModel(fakeGenerator ,settings = SettingsRepositoryImpl() , androidx.lifecycle.SavedStateHandle())
//    }
//
//    @After
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `initial state loads bottles from generator`() = runTest {
//        viewModel.state.test {
//            val state = awaitItem()
//            assertThat(state.bottles.size).isEqualTo(6) // 4 filled + 2 empty
//            cancelAndConsumeRemainingEvents()
//        }
//    }
//
//    @Test
//    fun `valid pour increments move count and updates bottles`() = runTest {
//        // Set up a simple level where bottle 0 has RED on top and bottle 4 is empty.
//        fakeGenerator.bottles = listOf(
//            Bottle(id = 0, layers = mutableStateListOf<Color>(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)),
//            Bottle(id = 1, layers = mutableStateListOf(Color.RED, Color.RED, Color.RED, Color.RED)),
//            Bottle(id = 2, layers = mutableStateListOf(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN)),
//            Bottle(id = 3, layers = mutableStateListOf(Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE)),
//            Bottle(id = 4, layers = mutableStateListOf()),
//            Bottle(id = 5, layers = mutableStateListOf())
//        )
//        // Reload viewmodel to use the new fake level.
//        viewModel = BottleGameViewModel(fakeGenerator, androidx.lifecycle.SavedStateHandle())
//
//        viewModel.state.test {
//            // Skip initial emission.
//            awaitItem()
//            // Select source bottle 0.
//            viewModel.onAction(GameAction.SelectBottle(0))
//            // Select empty destination bottle 4.
//            viewModel.onAction(GameAction.SelectBottle(4))
//            val afterPour = awaitItem()
//            assertThat(afterPour.moves).isEqualTo(1)
//            // Bottle 0 should have lost its top RED layer.
//            val srcBottle = afterPour.bottles[0]
//            assertThat(srcBottle.colors.last()).isEqualTo(Color.GREEN) // assuming conversion
//            cancelAndConsumeRemainingEvents()
//        }
//    }
//
//    @Test
//    fun `win condition emits ShowWinDialog event`() = runTest {
//        // All bottles already solved.
//        fakeGenerator.bottles = listOf(
//            Bottle(id = 0, layers = mutableStateListOf(Color.RED, Color.RED, Color.RED, Color.RED)),
//            Bottle(id = 1, layers = mutableStateListOf(Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN)),
//            Bottle(id = 2, layers = mutableStateListOf(Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE)),
//            Bottle(id = 3, layers = mutableStateListOf(Color.YELLOW, Color.YELLOW, Color.YELLOW, Color.YELLOW)),
//            Bottle(id = 4, layers = mutableStateListOf()),
//            Bottle(id = 5, layers = mutableStateListOf())
//        )
//        viewModel = BottleGameViewModel(fakeGenerator, androidx.lifecycle.SavedStateHandle())
//        viewModel.events.test {
//            val event = awaitItem()
//            assertThat(event).isEqualTo(GameEvent.ShowWinDialog)
//        }
//    }
//
//    // Simple fake level generator that can be reconfigured per test.
//    private class FakeLevelGenerator : LevelGenerator {
//        var bottles: List<Bottle> = emptyList()
//        override fun generate(config: LevelConfig): List<Bottle> {
//            return bottles
//        }
//    }
//}
