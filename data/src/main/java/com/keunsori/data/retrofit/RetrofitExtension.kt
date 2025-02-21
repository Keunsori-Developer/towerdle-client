package com.keunsori.data.retrofit

import android.util.Log
import com.keunsori.data.data.response.BaseResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend inline fun <Res : BaseResponse> Call<Res>.getResponse() = suspendCoroutine {
    enqueue(object : Callback<Res> {
        override fun onResponse(call: Call<Res>, response: Response<Res>) {
            val res = response.body()
            if (response.isSuccessful && res != null) {
                Log.d("HttpResponse", "success: ${response.code()} ${response.message()}")
                Log.d("HttpResponse", "response: $res")
                it.resumeWith(Result.success(res))
            } else {
                val errorBody = response.errorBody()?.string()
                val stringToJson = if (!errorBody.isNullOrEmpty()) JSONObject(errorBody) else JSONObject()

                Log.d("HttpResponse", "fail: $stringToJson")

                it.resumeWith(
                    Result.failure(
                        ServiceException(response.code(), stringToJson.optString("message", "UnknownError"))
                    )
                )
            }
        }

        override fun onFailure(call: Call<Res>, t: Throwable) {
            Log.d("HttpResponse", "error: $t")
            it.resumeWithException(t)
        }
    })
}
