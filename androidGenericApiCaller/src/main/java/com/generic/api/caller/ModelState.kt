package com.generic.api.caller

data class  ModelState<T>(
    val isLoading: Boolean = false,
    val data: T? = null,
    val error: String = ""
)