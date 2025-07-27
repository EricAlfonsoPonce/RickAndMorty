package es.ericalfonsoponce.data.dataSource.character.local

import es.ericalfonsoponce.data.dataSource.character.local.dao.CharacterDao
import es.ericalfonsoponce.data.dataSource.character.local.database.CharacterDataBase
import es.ericalfonsoponce.data.dataSource.character.local.dbo.CharacterDbo
import es.ericalfonsoponce.data.dataSource.handler.local.DataBaseHandler
import javax.inject.Inject

class CharacterLocalDataSourceImpl @Inject constructor(
    characterDataBase: CharacterDataBase,
    private val dataBaseHandler: DataBaseHandler
) : CharacterLocalDataSource {
    private val characterDao: CharacterDao = characterDataBase.characterDao
    override suspend fun getAllCharacters(id: Int): Result<List<CharacterDbo>> {
        return dataBaseHandler.load { characterDao.getAllCharacters(id) }
    }

    override suspend fun insertCharacter(character: CharacterDbo): Result<Long> {
        return dataBaseHandler.load { characterDao.insertCharacter(character) }
    }

    override suspend fun updateCharacter(character: CharacterDbo): Result<Int> {
        return dataBaseHandler.load { characterDao.updateCharacter(character) }
    }

    override suspend fun removeCharacter(character: CharacterDbo): Result<Int> {
        return dataBaseHandler.load { characterDao.removeCharacter(character) }
    }
}