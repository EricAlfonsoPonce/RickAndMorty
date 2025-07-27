package es.ericalfonsoponce.data.handler.local

import es.ericalfonsoponce.data.dataSource.handler.local.dbo.DataBaseError
import es.ericalfonsoponce.data.dataSource.handler.remote.dto.NetworkError
import es.ericalfonsoponce.data.handler.ErrorHandler
import es.ericalfonsoponce.domain.entity.error.AppError

class ErrorLocalHandlerImpl: ErrorHandler {
    override fun handle(exception: Exception): Exception {
        return when (exception) {
            is DataBaseError.Constraint, is DataBaseError.Failure -> AppError.SqlError
            else -> AppError.Unknown
        }
    }
}