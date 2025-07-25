package es.ericalfonsoponce.data.repository.character

import es.ericalfonsoponce.data.dataSource.character.remote.CharacterRemoteDataSource
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val dataSource: CharacterRemoteDataSource
) {

}