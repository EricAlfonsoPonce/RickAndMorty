package es.ericalfonsoponce.data.dataSource.character.remote

import es.ericalfonsoponce.data.dataSource.character.remote.api.CharacterApi
import es.ericalfonsoponce.data.dataSource.character.remote.dto.CharacterResponseDto
import es.ericalfonsoponce.data.dataSource.handler.remote.ApiHandler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CharacterRemoteDataSourceImplTest {
    private lateinit var dataSourceImpl: CharacterRemoteDataSource
    private var characterApi: CharacterApi = mockk()

    private var apiHandler: ApiHandler = mockk()

    @Before
    fun setUp() {
        dataSourceImpl = CharacterRemoteDataSourceImpl(characterApi, apiHandler)
    }

    @Test
    fun `getCharactersByPage is requested, it returns successfully his response`() = runBlocking {
        val characterResponse: CharacterResponseDto = mockk()
        // GIVEN
        coEvery {
            apiHandler.load(any<suspend () -> Response<CharacterResponseDto>>())
        } coAnswers {
            val call = firstArg<suspend () -> Response<CharacterResponseDto>>()

            runBlocking { Result.success(call.invoke().body() ?: characterResponse) }
        }
        // WHEN
        coEvery { characterApi.getCharactersByPage(any()).body() } returns characterResponse

        val result = dataSourceImpl.getCharactersByPage(1)

        // THEN
        coVerify(exactly = 1) { characterApi.getCharactersByPage(any()) }
        assertEquals(Result.success(characterResponse), result)
    }

    @Test
    fun `getCharactersByPage is requested, but an error occurs`() = runBlocking {
        // GIVEN
        coEvery { characterApi.getCharactersByPage(any()) } returns Response.error(
            404,
            "".toResponseBody()
        )
        coEvery { apiHandler.load(any<suspend () -> Response<CharacterResponseDto>>()) } returns Result.failure(
            Exception()
        )

        // WHEN
        val response = dataSourceImpl.getCharactersByPage(1)

        // THEN
        assertTrue(response.isFailure)
    }
}