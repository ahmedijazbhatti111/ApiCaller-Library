package com.generic.api.caller

import android.content.Context

object ApiCaller {
    var baseUrl : String? = null
    var headerHashMap : HashMap<String,String> = HashMap()

    fun provideRemoteRepository(context: Context): RemoteRepository {
        return RemoteRepositoryImp(ServiceBuilder.provideApi(context, baseUrl, headerHashMap))
    }
    fun provideLocalRepository(context: Context): LocalRepository {
        return LocalRepositoryImp(context.getSharedPreferences("HEADER_PREF", Context.MODE_PRIVATE))
    }

    fun config(baseUrl: String, vararg headers: Pair<String, String>){
        ApiCaller.baseUrl = baseUrl
        headers.forEach {
            headerHashMap[it.first] = it.second
        }
    }

    fun addHeaderConfig(header: Pair<String, String>) {
        headerHashMap[header.first] = header.second
    }
}


