package es.ericalfonsoponce.domain.useCase.character

import es.ericalfonsoponce.domain.entity.character.CharacterShow

interface CharacterUseCase {
    suspend fun getCharactersByPage(page: Int): Result<Pair<Boolean, List<CharacterShow>>>
}