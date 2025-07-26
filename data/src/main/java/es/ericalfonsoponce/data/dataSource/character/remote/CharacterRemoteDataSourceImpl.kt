package es.ericalfonsoponce.data.dataSource.character.remote

import es.ericalfonsoponce.data.dataSource.character.remote.api.CharacterApi
import es.ericalfonsoponce.data.dataSource.handler.remote.ApiHandler
import javax.inject.Inject

class CharacterRemoteDataSourceImpl @Inject constructor(
    private val characterApi: CharacterApi,
    private val apiHandler: ApiHandler
) : CharacterRemoteDataSource {

}