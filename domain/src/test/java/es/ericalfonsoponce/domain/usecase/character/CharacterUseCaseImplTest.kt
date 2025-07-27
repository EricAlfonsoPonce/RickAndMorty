package es.ericalfonsoponce.domain.usecase.character

import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.domain.repository.character.CharacterRepository
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCase
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCaseImpl
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class CharacterUseCaseImplTest {

    private lateinit var useCase: CharacterUseCase
    private val characterRepository: CharacterRepository = mockk()

    @Before
    fun setUp() {
        useCase = CharacterUseCaseImpl(characterRepository)
    }

    @Test
    fun `getCharactersByPage is requested, it returns successfully his response`() = runBlocking {
        // GIVEN
        coEvery { characterRepository.getCharactersByPage(any()) } returns Result.success(
            Pair(true, listOf())
        )

        // WHEN
        val result = useCase.getCharactersByPage(1)

        // THEN
        coVerify(exactly = 1) { characterRepository.getCharactersByPage(any()) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `getCharactersByPage is requested, but an error occurs in api`() = runBlocking {
        // GIVEN
        coEvery { characterRepository.getCharactersByPage(any()) } returns Result.failure(Exception())

        // WHEN
        val result = useCase.getCharactersByPage(1)

        // THEN
        coVerify(exactly = 1) { characterRepository.getCharactersByPage(any()) }
        assertTrue(result.isFailure)
    }

    @Test
    fun `updateCharacter is requested, it returns successfully his response`() = runBlocking {
        // GIVEN
        coJustRun { characterRepository.updateCharacter(any()) }

        // WHEN
        val result = useCase.updateCharacter(getMockCharacterShow())

        // THEN
        coVerify(exactly = 1) { characterRepository.updateCharacter(any()) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `updateCharacter is requested, but an error occurs`() = runBlocking {
        // GIVEN
        coEvery { characterRepository.updateCharacter(any()) } returns Result.failure(Exception())

        // WHEN
        val result = useCase.updateCharacter(getMockCharacterShow())

        // THEN
        coVerify(exactly = 1) { characterRepository.updateCharacter(any()) }
        assertTrue(result.isFailure)
    }

    @Test
    fun `removeCharacter is requested, it returns successfully his response`() = runBlocking {
        // GIVEN
        coJustRun { characterRepository.removeCharacter(any()) }

        // WHEN
        val result = useCase.removeCharacter(getMockCharacterShow())

        // THEN
        coVerify(exactly = 1) { characterRepository.removeCharacter(any()) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `removeCharacter is requested, but an error occurs`() = runBlocking {
        // GIVEN
        coEvery { characterRepository.removeCharacter(any()) } returns Result.failure(Exception())

        // WHEN
        val result = useCase.removeCharacter(getMockCharacterShow())

        // THEN
        coVerify(exactly = 1) { characterRepository.removeCharacter(any()) }
        assertTrue(result.isFailure)
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