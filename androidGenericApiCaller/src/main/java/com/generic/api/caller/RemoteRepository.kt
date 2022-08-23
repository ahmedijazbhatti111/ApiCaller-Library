package com.generic.api.caller

import com.google.gson.JsonObject

interface RemoteRepository {
    suspend fun callApiGET(url:String,vararg parameters: Any?): String
    suspend fun callApiPOST(url:String,vararg parameters: Any?): String
    suspend fun callApiPostWithBody(url:String,parameter:JsonObject): String
}