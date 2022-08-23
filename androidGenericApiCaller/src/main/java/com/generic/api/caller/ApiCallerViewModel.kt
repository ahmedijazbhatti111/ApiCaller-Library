package com.generic.api.caller

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn

class ApiCallerViewModel(context: Context) : ViewModel() {

    private val performRemoteApisUseCase = PerformRemoteApisUseCase(
        ApiCaller.provideRemoteRepository(context),
        ApiCaller.provideLocalRepository(context)
    )

    fun callApiForResponse(
        method: ApiMethod,
        apiPath: String = "",
        vararg parameters: Any?,
        isCache: Boolean = false,
        tokenKey: String? = null,
    ) = MutableStateFlow<ModelState<String>>(ModelState()).also{stateFlow->
        performRemoteApisUseCase(
            method,
            apiPath,
            parameters = parameters,
            isCache,
            tokenKey,
        ) {
            stateFlow.value = it
        }.launchIn(viewModelScope)
    }
}