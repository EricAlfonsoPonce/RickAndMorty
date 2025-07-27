package es.ericalfonsoponce.data.dataSource.character.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import es.ericalfonsoponce.data.dataSource.character.local.dbo.CharacterDbo

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character WHERE id >= :id ORDER BY id ASC")
    suspend fun getAllCharacters(id: Int): List<CharacterDbo>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCharacter(character: CharacterDbo): Long

    @Update
    suspend fun updateCharacter(character: CharacterDbo): Int

    @Delete
    suspend fun removeCharacter(character: CharacterDbo): Int
}