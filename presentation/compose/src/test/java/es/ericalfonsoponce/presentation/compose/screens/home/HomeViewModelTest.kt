package es.ericalfonsoponce.presentation.compose.screens.home

import app.cash.turbine.test
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCase
import es.ericalfonsoponce.presentation.compose.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val characterUseCase: CharacterUseCase = mockk()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        coEvery { characterUseCase.getCharactersByPage(1) } returns Result.success(Pair(true, emptyList()))
    }

    @Test
    fun `when init, then loads characters successfully`() = runTest {
        // GIVEN
        val characters = listOf(mockk<CharacterShow>(), mockk<CharacterShow>())
        coEvery { characterUseCase.getCharactersByPage(1) } returns Result.success(Pair(true, characters))

        // WHEN
        viewModel = HomeViewModel(characterUseCase)

        // THEN
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(characters, state.characters)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `when loadNextPage, then fetches next page and appends characters`() = runTest {
        // GIVEN
        val initialCharacters = listOf(mockk<CharacterShow>())
        val nextCharacters = listOf(mockk<CharacterShow>())

        coEvery { characterUseCase.getCharactersByPage(1) } returns Result.success(Pair(true, initialCharacters))
        viewModel = HomeViewModel(characterUseCase)

        coEvery { characterUseCase.getCharactersByPage(2) } returns Result.success(Pair(false, nextCharacters))

        // WHEN
        viewModel.loadNextPage()

        // THEN
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(initialCharacters + nextCharacters, state.characters)
            assertFalse(state.isLoadingPagination)
        }
    }

    @Test
    fun `when deleteCharacter, then removes character from state`() = runTest {
        // GIVEN
        val characterToDelete = mockk<CharacterShow>()
        val initialList = mutableListOf(characterToDelete)

        coEvery { characterUseCase.getCharactersByPage(1) } returns Result.success(Pair(true, initialList))
        coEvery { characterUseCase.removeCharacter(characterToDelete) } returns Result.success(Unit)

        viewModel = HomeViewModel(characterUseCase)

        // WHEN
        viewModel.deleteCharacter(characterToDelete)

        // THEN
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.characters.isEmpty())
        }
        coVerify { characterUseCase.removeCharacter(characterToDelete) }
    }

    @Test
    fun `when error loading characters, then updates state with error`() = runTest {
        // GIVEN
        val error = AppError.NoInternet
        coEvery { characterUseCase.getCharactersByPage(1) } returns Result.failure(error)

        // WHEN
        viewModel = HomeViewModel(characterUseCase)

        // THEN
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(error, state.error)
        }
    }

    @Test
    fun `onCharacterUpdated updates specific character in list`() = runTest {
        // GIVEN
        val char1 = CharacterShow(id = 1, name = "Rick", status = CharacterStatus.ALIVE, species = "Human", gender = CharacterGender.MALE, origin = "Earth", location = "Earth", image = "")
        val char2 = CharacterShow(id = 2, name = "Morty", status = CharacterStatus.ALIVE, species = "Human", gender = CharacterGender.MALE, origin = "Earth", location = "Earth", image = "")
        val initialList = listOf(char1, char2)

        coEvery { characterUseCase.getCharactersByPage(1) } returns Result.success(Pair(true, initialList))
        viewModel = HomeViewModel(characterUseCase)

        val updatedChar1 = char1.copy(status = CharacterStatus.DEAD)

        // WHEN
        viewModel.onCharacterUpdated(updatedChar1)

        // THEN
        viewModel.uiState.test {
            val state = awaitItem()
            val itemInList = state.characters.find { it.id == 1 }
            assertEquals(CharacterStatus.DEAD, itemInList?.status)
            assertEquals(CharacterStatus.ALIVE, state.characters.find { it.id == 2 }?.status)
        }
    }

    @Test
    fun `resetError clears error from state`() = runTest {
        // GIVEN:
        val error = AppError.Unknown
        coEvery { characterUseCase.getCharactersByPage(1) } returns Result.failure(error)
        viewModel = HomeViewModel(characterUseCase)

        viewModel.uiState.test {
            assertEquals(error, awaitItem().error)
        }

        // WHEN
        viewModel.resetError()

        // THEN
        viewModel.uiState.test {
            assertEquals(null, awaitItem().error)
        }
    }
}