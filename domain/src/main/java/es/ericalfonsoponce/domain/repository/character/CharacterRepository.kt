package es.ericalfonsoponce.domain.repository.character

import es.ericalfonsoponce.domain.entity.character.CharacterShow

interface CharacterRepository {
    suspend fun getCharactersByPage(page: Int): Result<Pair<Boolean, List<CharacterShow>>>
    suspend fun updateCharacter(character: CharacterShow): Result<Unit>
    suspend fun removeCharacter(character: CharacterShow): Result<Unit>
}