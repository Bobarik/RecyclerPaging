package com.gmail.vlaskorobogatov.network.services

import com.gmail.vlaskorobogatov.network.requests.CardRequest
import com.gmail.vlaskorobogatov.network.response.CardResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoadDataService {
    @POST("mobileapp/getAllCompanies")
    @Headers("TOKEN: 123")
    suspend fun getAllCards(@Body offset: CardRequest): Result<List<CardResponse>>
}