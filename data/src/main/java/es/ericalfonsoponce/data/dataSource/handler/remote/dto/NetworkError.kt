package es.ericalfonsoponce.data.dataSource.handler.remote.dto

sealed class NetworkError : Exception() {
    data class Failure(val msg: String): NetworkError()
    object Unknown: NetworkError()
    object EmptyBody: NetworkError()
    object NoInternet : NetworkError()
    object Timeout: NetworkError()
}