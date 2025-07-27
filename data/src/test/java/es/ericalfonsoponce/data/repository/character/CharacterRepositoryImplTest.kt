package es.ericalfonsoponce.data.repository.character

import es.ericalfonsoponce.data.dataSource.character.local.CharacterLocalDataSource
import es.ericalfonsoponce.data.dataSource.character.remote.CharacterRemoteDataSource
import es.ericalfonsoponce.data.dataSource.character.remote.dto.CharacterDto
import es.ericalfonsoponce.data.dataSource.character.remote.dto.CharacterLocationDto
import es.ericalfonsoponce.data.dataSource.character.remote.dto.CharacterOriginDto
import es.ericalfonsoponce.data.dataSource.character.remote.dto.CharacterResponseDto
import es.ericalfonsoponce.data.dataSource.character.remote.dto.InfoDto
import es.ericalfonsoponce.data.dataSource.handler.local.dbo.DataBaseError
import es.ericalfonsoponce.data.handler.ErrorHandler
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.domain.repository.character.CharacterRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class CharacterRepositoryImplTest {

    private lateinit var repository: CharacterRepository
    private var remoteDataSource: CharacterRemoteDataSource = mockk()
    private var localDataSource: CharacterLocalDataSource = mockk()
    private var errorRemoteHandler: ErrorHandler = mockk()
    private var errorLocalHandler: ErrorHandler = mockk()

    @Before
    fun setUp() {
        repository = CharacterRepositoryImpl(
            remoteDataSource = remoteDataSource,
            localDataSource = localDataSource,
            errorRemoteHandler = errorRemoteHandler,
            errorLocalHandler = errorLocalHandler
        )
    }

    @Test
    fun `getCharactersByPage is requested, it returns successfully his response`() = runBlocking {
        // GIVEN
        coEvery { remoteDataSource.getCharactersByPage(any()) } returns Result.success(
            getMockCharacterResponseDto()
        )
        coEvery { localDataSource.insertCharacter(any()) } returns Result.success(1)
        coEvery { localDataSource.getAllCharacters(any()) } returns Result.success(listOf())

        // WHEN
        val result = repository.getCharactersByPage(1)

        // THEN
        coVerify(exactly = 1) { remoteDataSource.getCharactersByPage(any()) }
        coVerify(exactly = 1) { localDataSource.insertCharacter(any()) }
        coVerify(exactly = 1) { localDataSource.getAllCharacters(any()) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `getCharactersByPage is requested, but an error occurs in api`() = runBlocking {
        // GIVEN
        coEvery { remoteDataSource.getCharactersByPage(any()) } returns Result.failure(Exception())
        coEvery { errorRemoteHandler.handle(any()) } returns AppError.Unknown

        // WHEN
        val result = repository.getCharactersByPage(1)

        // THEN
        coVerify(exactly = 1) { remoteDataSource.getCharactersByPage(any()) }
        assertTrue(result.isFailure)
    }

    @Test
    fun `getCharactersByPage is requested, but an error occurs in local`() = runBlocking {
        // GIVEN
        coEvery { remoteDataSource.getCharactersByPage(any()) } returns Result.success(
            getMockCharacterResponseDto()
        )
        coEvery { localDataSource.insertCharacter(any()) } returns Result.failure(Exception())
        coEvery { errorLocalHandler.handle(any()) } returns DataBaseError.Unknown

        // WHEN
        val result = repository.getCharactersByPage(1)

        // THEN
        coVerify(exactly = 1) { remoteDataSource.getCharactersByPage(any()) }
        coVerify(exactly = 1) { localDataSource.insertCharacter(any()) }
        assertTrue(result.isFailure)
    }

    @Test
    fun `updateCharacter is requested, it returns successfully his response`() = runBlocking {
        // GIVEN
        coEvery { localDataSource.updateCharacter(any()) } returns Result.success(1)

        // WHEN
        val result = repository.updateCharacter(getMockCharacterShow())

        // THEN
        coVerify(exactly = 1) { localDataSource.updateCharacter(any()) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `updateCharacter is requested, but an error occurs`() = runBlocking {
        // GIVEN
        coEvery { errorLocalHandler.handle(any()) } returns DataBaseError.Unknown
        coEvery { localDataSource.updateCharacter(any()) } returns Result.failure(Exception())

        // WHEN
        val result = repository.updateCharacter(getMockCharacterShow())

        // THEN
        coVerify(exactly = 1) { localDataSource.updateCharacter(any()) }
        assertTrue(result.isFailure)
    }

    @Test
    fun `removeCharacter is requested, it returns successfully his response`() = runBlocking {
        // GIVEN
        coEvery { localDataSource.removeCharacter(any()) } returns Result.success(1)

        // WHEN
        val result = repository.removeCharacter(getMockCharacterShow())

        // THEN
        coVerify(exactly = 1) { localDataSource.removeCharacter(any()) }
        assertTrue(result.isSuccess)
    }

    @Test
    fun `removeCharacter is requested, but an error occurs`() = runBlocking {
        // GIVEN
        coEvery { errorLocalHandler.handle(any()) } returns DataBaseError.Unknown
        coEvery { localDataSource.removeCharacter(any()) } returns Result.failure(Exception())

        // WHEN
        val result = repository.removeCharacter(getMockCharacterShow())

        // THEN
        coVerify(exactly = 1) { localDataSource.removeCharacter(any()) }
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

    private fun getMockCharacterDto() = CharacterDto(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        gender = "Male",
        origin = CharacterOriginDto("Earth (C-137)", ""),
        location = CharacterLocationDto("Citadel of Ricks", ""),
        image = ""
    )

    private fun getMockCharacterResponseDto() = CharacterResponseDto(
        info = InfoDto(1, 1, "", ""),
        results = listOf(getMockCharacterDto())
    )
}