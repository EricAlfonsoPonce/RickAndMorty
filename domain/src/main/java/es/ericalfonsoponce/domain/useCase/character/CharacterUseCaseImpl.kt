package es.ericalfonsoponce.domain.useCase.character

import es.ericalfonsoponce.domain.repository.character.CharacterRepository
import javax.inject.Inject

class CharacterUseCaseImpl @Inject constructor(
    characterRepository: CharacterRepository
): CharacterUseCase{

}