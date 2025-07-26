package es.ericalfonsoponce.data.dataSource.character.remote

import es.ericalfonsoponce.data.dataSource.character.remote.dto.CharacterResponseDto

interface CharacterRemoteDataSource {
    suspend fun getCharactersByPage(page: Int): Result<CharacterResponseDto>
}