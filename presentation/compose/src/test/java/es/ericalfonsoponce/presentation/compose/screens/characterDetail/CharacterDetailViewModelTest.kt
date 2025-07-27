package es.ericalfonsoponce.presentation.compose.screens.characterDetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCase
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CharacterDetailViewModelTest {
    private lateinit var viewModel: CharacterDetailViewModel
    private var characterUseCase: CharacterUseCase = mockk()

    private val savedStateHandle: SavedStateHandle = SavedStateHandle(mapOf("id" to 1))
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CharacterDetailViewModel(
            characterUseCase = characterUseCase,
            savedStateHandle = savedStateHandle
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `setCharacterName is requested, character name updated correctly`() = runTest {
        val name = "Rick Sanchez"
        // When
        viewModel.setCharacterName(name)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(name, state.character?.name)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteCharacter is requested, it returns successfully his response`() = runTest {
        // Given
        coJustRun { characterUseCase.updateCharacter(any()) }
        // When
        viewModel.saveChanges()

        // Then
        coVerify(exactly = 1) { characterUseCase.updateCharacter(any()) }
        viewModel.uiState.test {
            val state = awaitItem()
            //assertEquals(state.characters, listOf())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `saveChanges is requested, but an error occurs`() = runTest {
        // Given
        coEvery { characterUseCase.updateCharacter(any()) } returns Result.failure(AppError.Unknown)
        // When
        viewModel.saveChanges()

        // Then
        coVerify(exactly = 1) { characterUseCase.updateCharacter(any()) }
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(state.error, AppError.Unknown)
            cancelAndIgnoreRemainingEvents()
        }
    }
}