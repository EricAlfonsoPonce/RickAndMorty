package es.ericalfonsoponce.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.ericalfonsoponce.data.dataSource.character.remote.CharacterRemoteDataSource
import es.ericalfonsoponce.data.dataSource.character.remote.CharacterRemoteDataSourceImpl
import es.ericalfonsoponce.data.dataSource.character.remote.api.CharacterApi
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
}