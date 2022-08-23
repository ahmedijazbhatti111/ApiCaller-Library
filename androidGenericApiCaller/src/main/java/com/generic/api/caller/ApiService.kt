package com.generic.api.caller

import com.generic.api.caller.Header.token
import com.generic.api.caller.Header.COOKIE
import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url
import retrofit2.http.Body
import retrofit2.http.Header


interface ApiService {
    @GET
    suspend fun getData(
        @Url url: String,
        @Header(COOKIE) loginCookies: String = token.toString()
    ): String

    @POST("{url}")
    suspend fun postData(
        @Path("url",encoded = true) url: String,
        @Header(COOKIE) loginCookies: String = token.toString()
    ): String

    @POST
    suspend fun postDataWithBody(
        @Url url: String,
        @Body transactionRequest: JsonObject,
        @Header(COOKIE) loginCookies: String = token.toString()
    ): String
}

