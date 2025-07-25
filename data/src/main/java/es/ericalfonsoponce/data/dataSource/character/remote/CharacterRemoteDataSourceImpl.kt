package es.ericalfonsoponce.data.dataSource.character.remote

import es.ericalfonsoponce.data.dataSource.character.remote.api.CharacterApi
import javax.inject.Inject

class CharacterRemoteDataSourceImpl @Inject constructor(
    private val characterApi: CharacterApi
): CharacterRemoteDataSource {

}