package es.ericalfonsoponce.data.dataSource.handler.local.dbo

import es.ericalfonsoponce.data.dataSource.handler.remote.dto.NetworkError

sealed class DataBaseError: Exception() {
    object Failure: DataBaseError()
    object Constraint: DataBaseError()
    object Unknown: DataBaseError()
}