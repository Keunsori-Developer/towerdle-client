package com.keunsori.data.data.response

abstract class BaseResponse(
    open var message: String? = null, // TODO: 아직 정확한 데이터 폼을 모름
    open var error: String? = null,
    open var errorCode: Int? = null,
    open var statusCode: Int = 0,
)

class Response : BaseResponse()
