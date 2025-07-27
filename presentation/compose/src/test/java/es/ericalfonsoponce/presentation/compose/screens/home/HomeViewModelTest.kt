package es.ericalfonsoponce.presentation.compose.screens.home

import app.cash.turbine.test
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
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
import kotlin.test.assertFalse

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel
    private var characterUseCase: CharacterUseCase = mockk()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(
            characterUseCase = characterUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCharactersByPage is requested, it returns successfully his response`() = runTest {
        // Given
        coEvery { characterUseCase.getCharactersByPage(any()) } returns Result.success(
            Pair(
                true,
                listOf(getMockCharacterShow())
            )
        )
        // When
        viewModel.getCharacters()

        // Then
        coVerify(exactly = 1) { characterUseCase.getCharactersByPage(any()) }
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(state.characters, listOf(getMockCharacterShow()))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getCharactersByPage is requested, but an error occurs`() = runTest {
        // Given
        coEvery { characterUseCase.getCharactersByPage(any()) } returns Result.failure(AppError.Unknown)
        // When
        viewModel.getCharacters()

        // Then
        coVerify(exactly = 1) { characterUseCase.getCharactersByPage(any()) }
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(state.error, AppError.Unknown)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteCharacter is requested, it returns successfully his response`() = runTest {
        // Given
        coJustRun { characterUseCase.removeCharacter(any()) }
        // When
        viewModel.deleteCharacter(getMockCharacterShow())

        // Then
        coVerify(exactly = 1) { characterUseCase.removeCharacter(any()) }
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(state.characters, listOf())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `deleteCharacter is requested, but an error occurs`() = runTest {
        // Given
        coEvery { characterUseCase.removeCharacter(any()) } returns Result.failure(AppError.Unknown)

        // When
        viewModel.deleteCharacter(getMockCharacterShow())

        // Then
        coVerify(exactly = 1) { characterUseCase.removeCharacter(any()) }
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertEquals(state.error, AppError.Unknown)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun getMockCharacterShow() = CharacterShow(
        id = 1,
        name = "Rick Sanchez",
        status = CharacterStatus.ALIVE,
        species = "Human",
        gender = CharacterGender.MALE,
        origin = "Earth (C-137)",
        location = "Citadel of Ricks",
        image = ""
    )
}