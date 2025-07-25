package es.ericalfonsoponce.domain.repository.character

interface CharacterRepository {
    suspend fun getCharactersByPage(page: Int)
}