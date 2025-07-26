package es.ericalfonsoponce.data.dataSource.character.remote.api

import es.ericalfonsoponce.data.dataSource.character.remote.dto.CharacterResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {
    @GET("character/")
    suspend fun getCharactersByPage(
        @Query("page") page: Int
    ): Response<CharacterResponseDto>
}