package com.generic.api.caller

import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import java.net.UnknownHostException

class PerformRemoteApisUseCase constructor(
    private val repository: RemoteRepository,
    private val localRepository: LocalRepository,
) {
    init {
        Header.token = localRepository.getStringFromPreferences("Token")
    }

    operator fun invoke(
        method: ApiMethod,
        url: String,
        vararg parameters: Any?,
        isCache: Boolean,
        tokenKey: String?,
        onEach: (ModelState<String>) -> Unit
    ): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading())
            val data = when (method) {
                ApiMethod.POST -> {
                    repository.callApiPOST(url, parameters = parameters)
                }
                ApiMethod.GET -> {
                    repository.callApiGET(url, parameters = parameters)
                }
                ApiMethod.POST_BODY -> {
                    val a = parameters.toJson().toHashMap().toJson().toPojo<JsonObject>()
                    repository.callApiPostWithBody(
                        url,
                        parameter = a
                    )
                }
            }
            tokenKey?.let {
                Header.token = Header.headers[it]
                localRepository.saveStringInPreferences("Token", Header.token.toString())
                Header.isHeader = false
            }
            if (isCache) {
                localRepository.saveStringInPreferences(url, data)
            }
            emit(Resource.Success(data = data))
        }catch (e: UnknownHostException){
            if (!isCache)
                emit(Resource.Error(message = "No access to the internet"))
            else
                emit(Resource.Success(data = localRepository.getStringFromPreferences(url)))
        } catch (e: Exception) {
            e.printStackTrace()
            if (!isCache)
                emit(Resource.Error(message = e.localizedMessage ?: "An Unknown error occurred"))
            else
                emit(Resource.Success(data = localRepository.getStringFromPreferences(url)))
        }
    }.onEach {
        when (it) {
            is Resource.Loading -> {
                onEach(ModelState(isLoading = true))
            }
            is Resource.Error -> {
                onEach(ModelState(error = it.message + ""))
            }
            is Resource.Success -> {
                onEach(ModelState(data = it.data))
            }
        }
    }
}