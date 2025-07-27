package es.ericalfonsoponce.domain.entity.error

sealed class AppError: Exception() {
    data class Failure(val msg: String) : AppError()
    object NoInternet : AppError()
    object Unknown : AppError()
    object EmptyBody : AppError()
    object SqlError : AppError()
}