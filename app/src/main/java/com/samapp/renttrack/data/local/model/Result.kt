package com.samapp.renttrack.data.local.model

sealed class Result<T> (
    val data : T? = null,
    val message : String? = null
){
    class Loading<T>: Result<T>()
    class Success<T>(data: T?) : Result<T>(data = data)
    class Error<T>(message: String?,data:T? = null): Result<T>(message = message,data = data)
}