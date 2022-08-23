package com.generic.api.caller

import com.google.gson.JsonObject

class RemoteRepositoryImp(private val api: ApiService) : RemoteRepository {
    override suspend fun callApiGET(url:String, vararg parameters: Any?): String {
        return api.getData(RemoteDataSource.getData(url, parameters))
    }

    override suspend fun callApiPOST(url:String,vararg parameters: Any?): String {
        return api.postData(RemoteDataSource.getData(url, parameters))
    }

    override suspend fun callApiPostWithBody(url:String, parameter:JsonObject): String {
        return api.postDataWithBody(RemoteDataSource.getData(url),parameter)
    }
}