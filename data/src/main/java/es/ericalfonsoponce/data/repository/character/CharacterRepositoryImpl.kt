package es.ericalfonsoponce.data.repository.character

import es.ericalfonsoponce.data.dataSource.character.remote.CharacterRemoteDataSource
import es.ericalfonsoponce.data.dataSource.character.remote.dto.CharacterDto
import es.ericalfonsoponce.data.handler.ErrorHandler
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.domain.repository.character.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val dataSource: CharacterRemoteDataSource,
    private val errorRemoteHandler: ErrorHandler
) : CharacterRepository {
    override suspend fun getCharactersByPage(page: Int): Result<Pair<Boolean, List<CharacterShow>>> {
        val response = dataSource.getCharactersByPage(page)

        return response.fold(
            onSuccess = { characterResponse ->
                Result.success(
                    Pair(
                        characterResponse.info.next != null,
                        characterResponse.results.map { it.toDomain() })
                )
            },
            onFailure = { exception ->
                val error = if (exception is Exception) errorRemoteHandler.handle(exception) else AppError.Unknown
                Result.failure(error)
            }
        )
    }

    private fun CharacterDto.toDomain(): CharacterShow = CharacterShow(
        id = id,
        name = name,
        status = CharacterStatus.entries.firstOrNull { it.value == status } ?: CharacterStatus.UNKNOWN,
        species = species,
        type = type,
        gender = CharacterGender.entries.firstOrNull { it.value == gender } ?: CharacterGender.UNKNOWN,
        origin = origin.name,
        location = location.name,
        image = image
    )
}