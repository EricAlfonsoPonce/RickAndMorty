package es.ericalfonsoponce.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.ericalfonsoponce.data.dataSource.character.remote.CharacterRemoteDataSource
import es.ericalfonsoponce.data.repository.character.CharacterRepositoryImpl
import es.ericalfonsoponce.domain.repository.character.CharacterRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providesCharacterRepository(
        characterRemoteDataSource: CharacterRemoteDataSource
    ): CharacterRepository = CharacterRepositoryImpl(characterRemoteDataSource)

}