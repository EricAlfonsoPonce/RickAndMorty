package es.ericalfonsoponce.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.ericalfonsoponce.domain.repository.character.CharacterRepository
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCase
import es.ericalfonsoponce.domain.useCase.character.CharacterUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun providesCharacterUseCase(
        characterRepository: CharacterRepository
    ): CharacterUseCase = CharacterUseCaseImpl(characterRepository)

}