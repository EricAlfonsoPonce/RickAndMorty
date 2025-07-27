package es.ericalfonsoponce.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.ericalfonsoponce.data.dataSource.character.local.CharacterLocalDataSource
import es.ericalfonsoponce.data.dataSource.character.local.CharacterLocalDataSourceImpl
import es.ericalfonsoponce.data.dataSource.character.local.database.CharacterDataBase
import es.ericalfonsoponce.data.dataSource.character.remote.CharacterRemoteDataSource
import es.ericalfonsoponce.data.dataSource.character.remote.CharacterRemoteDataSourceImpl
import es.ericalfonsoponce.data.dataSource.character.remote.api.CharacterApi
import es.ericalfonsoponce.data.dataSource.handler.local.DataBaseHandler
import es.ericalfonsoponce.data.dataSource.handler.remote.ApiHandler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun providesCharacterRemoteDataSource(
        characterApi: CharacterApi,
        apiHandler: ApiHandler
    ): CharacterRemoteDataSource =
        CharacterRemoteDataSourceImpl(
            characterApi = characterApi,
            apiHandler = apiHandler
        )
    @Provides
    @Singleton
    fun providesCharacterLocalDataSource(
        characterDataBase: CharacterDataBase,
        dataBaseHandler: DataBaseHandler
    ): CharacterLocalDataSource =
        CharacterLocalDataSourceImpl(
            characterDataBase = characterDataBase,
            dataBaseHandler = dataBaseHandler
        )
}