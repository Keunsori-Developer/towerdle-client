package com.keunsori.domain.entity


sealed class ApiResult<T> {
    class Success<T>(val data: T) : ApiResult<T>()
    class Fail<T>(val exception: Throwable) : ApiResult<T>()
}

