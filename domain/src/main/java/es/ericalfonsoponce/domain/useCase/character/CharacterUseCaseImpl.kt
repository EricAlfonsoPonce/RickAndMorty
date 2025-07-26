package es.ericalfonsoponce.domain.useCase.character

import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.repository.character.CharacterRepository
import javax.inject.Inject

class CharacterUseCaseImpl @Inject constructor(
    private val characterRepository: CharacterRepository
) : CharacterUseCase {
    override suspend fun getCharactersByPage(page: Int): Result<Pair<Boolean, List<CharacterShow>>> {
        return characterRepository.getCharactersByPage(page)
    }
}