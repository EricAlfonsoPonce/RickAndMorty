package es.ericalfonsoponce.data.dataSource.character.local

import es.ericalfonsoponce.data.dataSource.character.local.dbo.CharacterDbo

interface CharacterLocalDataSource {
    suspend fun getAllCharacters(id: Int): Result<List<CharacterDbo>>
    suspend fun insertCharacter(character: CharacterDbo): Result<Long>
    suspend fun updateCharacter(character: CharacterDbo): Result<Int>
    suspend fun removeCharacter(character: CharacterDbo): Result<Int>
}