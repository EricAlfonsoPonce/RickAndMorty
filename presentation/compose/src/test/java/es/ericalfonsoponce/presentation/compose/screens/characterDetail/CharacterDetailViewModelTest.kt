package es.ericalfonsoponce.presentation.compose.screens.characterDetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCase
import es.ericalfonsoponce.presentation.compose.navigation.Screens
import es.ericalfonsoponce.presentation.compose.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CharacterDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val characterUseCase: CharacterUseCase = mockk()
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private lateinit var viewModel: CharacterDetailViewModel

    private val mockCharacter = CharacterShow(
        id = 1,
        name = "Rick Sanchez",
        status = CharacterStatus.ALIVE,
        species = "Human",
        gender = CharacterGender.MALE,
        origin = "Earth",
        location = "Earth",
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
    )

    @Before
    fun setUp() {
        mockkObject(Screens.CharacterDetailScreen.Companion)

        every {
            Screens.CharacterDetailScreen.Companion.from(savedStateHandle)
        } returns Screens.CharacterDetailScreen(mockCharacter)

        viewModel = CharacterDetailViewModel(characterUseCase, savedStateHandle)
    }

    @After
    fun tearDown() {
        unmockkObject(Screens.CharacterDetailScreen.Companion)
    }

    @Test
    fun initRetrievesCharacterFromNavigation() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(mockCharacter, state.character)
        }
    }

    @Test
    fun updateCharacterStatusUpdatesState() = runTest {
        viewModel.setCharacterStatus(CharacterStatus.DEAD,)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(CharacterStatus.DEAD, state.character?.status)
        }
    }

    @Test
    fun saveChangesCallsUseCaseAndNavigatesOnSuccess() = runTest {
        // GIVEN
        coEvery { characterUseCase.updateCharacter(any()) } returns Result.success(Unit)

        // WHEN
        viewModel.saveChanges()

        // THEN
        coVerify { characterUseCase.updateCharacter(any()) }

        viewModel.navEventsFlow.test {
            val event = awaitItem()
            assert(event is NavEvent.NavigateToHomeScreen)
        }
    }

    @Test
    fun saveChangesSetsErrorOnFailure() = runTest {
        // GIVEN
        val error = AppError.Unknown
        coEvery { characterUseCase.updateCharacter(any()) } returns Result.failure(error)

        // WHEN
        viewModel.saveChanges()

        // THEN
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(error, state.error)
        }
    }
}