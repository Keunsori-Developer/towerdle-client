package com.keunsori.data.retrofit

data class ServiceException(val code: Int, override val message: String) : Exception(message)
