package es.ericalfonsoponce.data.dataSource.handler.remote

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import es.ericalfonsoponce.data.dataSource.handler.remote.dto.NetworkError
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type
import java.net.SocketTimeoutException

class ApiHandler {
    suspend fun <T> load(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Result.success(body)
                } ?: throw NetworkError.EmptyBody
            } else {
                throw parseError(response.errorBody())
            }
        } catch (exception: Exception) {
            Result.failure(handler(exception))
        }
    }

    private fun handler(error: Exception): Exception {
        return when (error) {
            is HttpException -> {
                parseError(error.response()?.errorBody())
            }

            is SocketTimeoutException -> NetworkError.Timeout
            is IOException -> NetworkError.NoInternet
            is NetworkError -> error
            else -> NetworkError.Unknown
        }
    }

    private fun parseError(body: ResponseBody?): NetworkError.Failure {
        val errorType: Type = object : TypeToken<NetworkError.Failure?>() {}.type
        val errorParsed = Gson().fromJson<NetworkError.Failure?>(body?.string(), errorType)
        return NetworkError.Failure(errorParsed.msg)
    }
}
