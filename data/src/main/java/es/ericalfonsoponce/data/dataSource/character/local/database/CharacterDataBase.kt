package es.ericalfonsoponce.data.dataSource.character.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import es.ericalfonsoponce.data.dataSource.character.local.dao.CharacterDao
import es.ericalfonsoponce.data.dataSource.character.local.dbo.CharacterDbo

@Database(
    entities = [CharacterDbo::class],
    version = 1,
    exportSchema = false
)

abstract class CharacterDataBase : RoomDatabase() {
    abstract val characterDao: CharacterDao
}