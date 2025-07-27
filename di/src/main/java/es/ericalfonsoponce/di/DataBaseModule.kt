package es.ericalfonsoponce.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import es.ericalfonsoponce.data.dataSource.character.local.dao.CharacterDao
import es.ericalfonsoponce.data.dataSource.character.local.database.CharacterDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun providesCharacterDatabase(
        @ApplicationContext context: Context
    ): CharacterDataBase =
        Room.databaseBuilder(
            context,
            CharacterDataBase::class.java,
            "RickAndMortyDatabase.db"
        ).build()

    @Provides
    @Singleton
    fun providesCharacterDao(
        characterDataBase: CharacterDataBase
    ): CharacterDao =
        characterDataBase.characterDao
}