package com.gmail.vlaskorobogatov.network.calladapters

import com.gmail.vlaskorobogatov.network.exceptions.BadRequestException
import com.gmail.vlaskorobogatov.network.exceptions.InternalServerException
import com.gmail.vlaskorobogatov.network.exceptions.UnauthorizedException
import com.gmail.vlaskorobogatov.network.exceptions.UnexpectedHttpException
import com.google.gson.Gson
import retrofit2.HttpException
import retrofit2.Response
import java.net.HttpURLConnection

internal fun <T : Any> mapResponseToResult(response: Response<T>): Result<T> {
    val body = response.body()
    return if (response.isSuccessful && body != null) {
        Result.success(body)
    } else {
        val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(), ErrorResponse::class.java)
        Result.failure(mapErrorToNetworkException(errorResponse.message, response.code()))
    }
}

internal fun mapExceptionToNetworkException(throwable: Throwable): RuntimeException {
    return when (throwable) {
        is HttpException -> mapHttpExceptionToNetworkException(throwable)
        else -> UnexpectedHttpException()
    }
}

internal fun mapHttpExceptionToNetworkException(exception: HttpException): RuntimeException {
    return mapErrorToNetworkException(exception.message, exception.code())
}

internal fun mapErrorToNetworkException(errorMessage: String?, code: Int): RuntimeException {
    return when (code) {
        HttpURLConnection.HTTP_BAD_REQUEST -> {
            BadRequestException(errorMessage)
        }
        HttpURLConnection.HTTP_UNAUTHORIZED -> {
            UnauthorizedException()
        }
        HttpURLConnection.HTTP_INTERNAL_ERROR -> {
            InternalServerException()
        }
        else -> {
            UnexpectedHttpException()
        }
    }
}

