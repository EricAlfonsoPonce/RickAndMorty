package es.ericalfonsoponce.data.dataSource.character.local

import androidx.sqlite.SQLiteException
import es.ericalfonsoponce.data.dataSource.character.local.dao.CharacterDao
import es.ericalfonsoponce.data.dataSource.character.local.database.CharacterDataBase
import es.ericalfonsoponce.data.dataSource.character.local.dbo.CharacterDbo
import es.ericalfonsoponce.data.dataSource.handler.local.DataBaseHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CharacterLocalDataSourceImplTest {
    private lateinit var dataSourceImpl: CharacterLocalDataSource
    private var characterDataBase: CharacterDataBase = mockk()
    private var dataBaseHandler: DataBaseHandler = mockk()
    private var characterDao: CharacterDao = mockk()

    @Before
    fun setUp() {
        every { characterDataBase.characterDao } returns characterDao
        dataSourceImpl = CharacterLocalDataSourceImpl(characterDataBase, dataBaseHandler)
    }

    @Test
    fun `getAllCharacters is requested, it returns successfully his response`() = runBlocking {
        val characterResponse: List<CharacterDbo> = mockk()
        // GIVEN
        coEvery { characterDao.getAllCharacters(any()) } returns characterResponse

        // WHEN
        val result = characterDao.getAllCharacters(0)

        // THEN
        coVerify(exactly = 1) { characterDao.getAllCharacters(any()) }
        assertEquals(characterResponse, result)
    }

    @Test
    fun `getAllCharacters is requested, but an error occurs`() = runBlocking {
        // GIVEN
        coEvery { characterDao.getAllCharacters(any()) } throws SQLiteException()
        coEvery { dataBaseHandler.load(any<suspend () -> List<CharacterDbo>>()) } returns Result.failure(
            Exception()
        )

        // WHEN
        val response = dataSourceImpl.getAllCharacters(1)

        // THEN
        assertTrue(response.isFailure)
    }

    @Test
    fun `insertCharacter is requested, it returns successfully his response`() = runBlocking {
        val resultLong: Long = 1
        val characterDbo: CharacterDbo = mockk()
        // GIVEN
        coEvery { characterDao.insertCharacter(any()) } returns resultLong

        // WHEN
        val result = characterDao.insertCharacter(characterDbo)

        // THEN
        coVerify(exactly = 1) { characterDao.insertCharacter(any()) }
        assertEquals(resultLong, result)
    }

    @Test
    fun `insertCharacter is requested, but an error occurs`() = runBlocking {
        val characterDbo: CharacterDbo = mockk()
        // GIVEN
        coEvery { characterDao.insertCharacter(any()) } throws SQLiteException()
        coEvery { dataBaseHandler.load(any<suspend () -> List<CharacterDbo>>()) } returns Result.failure(
            Exception()
        )

        // WHEN
        val response = dataSourceImpl.insertCharacter(characterDbo)

        // THEN
        assertTrue(response.isFailure)
    }

    @Test
    fun `updateCharacter is requested, it returns successfully his response`() = runBlocking {
        val rowsInserted: Int = 1
        val characterDbo: CharacterDbo = mockk()
        // GIVEN
        coEvery { characterDao.updateCharacter(any()) } returns rowsInserted

        // WHEN
        val result = characterDao.updateCharacter(characterDbo)

        // THEN
        coVerify(exactly = 1) { characterDao.updateCharacter(any()) }
        assertEquals(rowsInserted, result)
    }

    @Test
    fun `updateCharacter is requested, but an error occurs`() = runBlocking {
        val characterDbo: CharacterDbo = mockk()
        // GIVEN
        coEvery { characterDao.updateCharacter(any()) } throws SQLiteException()
        coEvery { dataBaseHandler.load(any<suspend () -> List<CharacterDbo>>()) } returns Result.failure(
            Exception()
        )

        // WHEN
        val response = dataSourceImpl.updateCharacter(characterDbo)

        // THEN
        assertTrue(response.isFailure)
    }

    @Test
    fun `removeCharacter is requested, it returns successfully his response`() = runBlocking {
        val rowsInserted: Int = 1
        val characterDbo: CharacterDbo = mockk()
        // GIVEN
        coEvery { characterDao.removeCharacter(any()) } returns rowsInserted

        // WHEN
        val result = characterDao.removeCharacter(characterDbo)

        // THEN
        coVerify(exactly = 1) { characterDao.removeCharacter(any()) }
        assertEquals(rowsInserted, result)
    }

    @Test
    fun `removeCharacter is requested, but an error occurs`() = runBlocking {
        val characterDbo: CharacterDbo = mockk()
        // GIVEN
        coEvery { characterDao.removeCharacter(any()) } throws SQLiteException()
        coEvery { dataBaseHandler.load(any<suspend () -> List<CharacterDbo>>()) } returns Result.failure(
            Exception()
        )

        // WHEN
        val response = dataSourceImpl.removeCharacter(characterDbo)

        // THEN
        assertTrue(response.isFailure)
    }
}