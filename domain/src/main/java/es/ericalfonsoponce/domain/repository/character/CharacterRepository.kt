package es.ericalfonsoponce.domain.repository.character

import es.ericalfonsoponce.domain.entity.character.CharacterShow

interface CharacterRepository {
    suspend fun getCharactersByPage(page: Int): Result<Pair<Boolean, List<CharacterShow>>>
}