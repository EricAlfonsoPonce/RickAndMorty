package es.ericalfonsoponce.data.repository.character

import es.ericalfonsoponce.data.dataSource.character.local.CharacterLocalDataSource
import es.ericalfonsoponce.data.dataSource.character.local.dbo.CharacterDbo
import es.ericalfonsoponce.data.dataSource.character.remote.CharacterRemoteDataSource
import es.ericalfonsoponce.data.dataSource.character.remote.dto.CharacterDto
import es.ericalfonsoponce.data.handler.ErrorHandler
import es.ericalfonsoponce.domain.entity.character.CharacterGender
import es.ericalfonsoponce.domain.entity.character.CharacterShow
import es.ericalfonsoponce.domain.entity.character.CharacterStatus
import es.ericalfonsoponce.domain.entity.error.AppError
import es.ericalfonsoponce.domain.repository.character.CharacterRepository
import javax.inject.Inject
import javax.inject.Named

class CharacterRepositoryImpl @Inject constructor(
    private val remoteDataSource: CharacterRemoteDataSource,
    private val localDataSource: CharacterLocalDataSource,
    private val errorRemoteHandler: ErrorHandler,
    private val errorLocalHandler: ErrorHandler
) : CharacterRepository {
    override suspend fun getCharactersByPage(page: Int): Result<Pair<Boolean, List<CharacterShow>>> {

        val characterResponse = remoteDataSource.getCharactersByPage(page)
            .getOrElse { throwable ->
                val error = if (throwable is Exception) errorRemoteHandler.handle(throwable) else AppError.Unknown
                return Result.failure(error)
            }

        characterResponse.results.forEach {
            localDataSource.insertCharacter(it.toDbo())
                .onFailure { throwable ->
                    val error = if (throwable is Exception) errorLocalHandler.handle(throwable) else AppError.Unknown
                    return Result.failure(error)
                }
        }

        val localCharacters = localDataSource.getAllCharacters(characterResponse.results.firstOrNull()?.id ?: 0)
            .getOrElse { throwable ->
                val error = if (throwable is Exception) errorLocalHandler.handle(throwable) else AppError.Unknown
                return Result.failure(error)
            }

        return Result.success(
            Pair(
                characterResponse.info.next != null,
                localCharacters.map { it.toDomain() }
            )
        )
    }

    override suspend fun updateCharacter(character: CharacterShow): Result<Unit> {
        return localDataSource.updateCharacter(character.toDbo()).fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = { throwable ->
                val error =
                    if (throwable is Exception) errorLocalHandler.handle(throwable) else AppError.Unknown
                Result.failure(error)
            }
        )
    }

    override suspend fun removeCharacter(character: CharacterShow): Result<Unit> {
        return localDataSource.removeCharacter(character.toDbo()).fold(
            onSuccess = {
                Result.success(Unit)
            },
            onFailure = { throwable ->
                val error =
                    if (throwable is Exception) errorLocalHandler.handle(throwable) else AppError.Unknown
                Result.failure(error)
            }
        )
    }

    private fun CharacterDto.toDbo(): CharacterDbo = CharacterDbo(
        id = id,
        name = name,
        status = status,
        species = species,
        gender = gender,
        origin = origin.name,
        location = location.name,
        image = image
    )

    private fun CharacterDbo.toDomain(): CharacterShow = CharacterShow(
        id = id,
        name = name,
        status = CharacterStatus.entries.firstOrNull { it.value == status }
            ?: CharacterStatus.UNKNOWN,
        species = species,
        gender = CharacterGender.entries.firstOrNull { it.value == gender }
            ?: CharacterGender.UNKNOWN,
        origin = origin,
        location = location,
        image = image
    )

    private fun CharacterShow.toDbo(): CharacterDbo = CharacterDbo(
        id = id,
        name = name,
        status = status.value,
        species = species,
        gender = gender.value,
        origin = origin,
        location = location,
        image = image
    )
}