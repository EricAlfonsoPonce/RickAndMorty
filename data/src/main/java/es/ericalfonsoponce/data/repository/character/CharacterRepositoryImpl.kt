package es.ericalfonsoponce.data.repository.character

import es.ericalfonsoponce.data.dataSource.character.remote.CharacterRemoteDataSource
import es.ericalfonsoponce.data.handler.ErrorHandler
import es.ericalfonsoponce.domain.repository.character.CharacterRepository
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val dataSource: CharacterRemoteDataSource,
    private val errorRemoteHandler: ErrorHandler
): CharacterRepository {
    override suspend fun getCharactersByPage(page: Int) {
        TODO("Not yet implemented")
    }
}