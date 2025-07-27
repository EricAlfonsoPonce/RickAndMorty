package es.ericalfonsoponce.data.dataSource.handler.local

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteException
import es.ericalfonsoponce.data.dataSource.handler.local.dbo.DataBaseError
import java.sql.SQLException
import javax.inject.Inject

class DataBaseHandler @Inject constructor() {
    suspend fun <T> load(call: suspend () -> T): Result<T> {
        return try {
            Result.success(call())
        } catch (exception: Exception) {
            Result.failure(handler(exception))
        }
    }

    private fun handler(error: Exception): Exception {
        return when (error) {
            is SQLiteConstraintException -> DataBaseError.Constraint
            is SQLiteException -> DataBaseError.Failure
            else -> DataBaseError.Unknown
        }
    }
}