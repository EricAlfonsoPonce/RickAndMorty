package es.ericalfonsoponce.data.handler.local

import es.ericalfonsoponce.data.dataSource.handler.remote.dto.NetworkError
import es.ericalfonsoponce.data.handler.ErrorHandler
import es.ericalfonsoponce.domain.entity.error.AppError

class ErrorLocalHandlerImpl: ErrorHandler {
    override fun handle(exception: Exception): Exception {
        return when (exception) {
            is NetworkError.EmptyBody -> AppError.EmptyBody
            is NetworkError.NoInternet, is NetworkError.Timeout -> AppError.NoInternet
            is NetworkError.Failure -> AppError.Failure(exception.msg)
            else -> AppError.Unknown
        }
    }
}