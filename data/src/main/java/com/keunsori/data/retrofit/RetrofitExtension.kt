package com.keunsori.data.retrofit

import android.util.Log
import com.keunsori.data.data.response.BaseResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend inline fun <Res : BaseResponse> Call<Res>.getResponse() = suspendCoroutine {
    enqueue(object : Callback<Res> {
        override fun onResponse(call: Call<Res>, response: Response<Res>) {
            val res = response.body()

            if (response.isSuccessful) {
                Log.d("HttpResponse", "success: ${response.code()} ${response.message()}")
                Log.d("HttpResponse", "response: $res")
                it.resumeWith(Result.success(res!!)) // respnose가 success일 때 body가 null이면 exception 발생하게 유도
            } else {
                val stringToJson = JSONObject(response.errorBody()?.string()!!)
                Log.d("HttpResponse", "fail: $stringToJson")

                it.resumeWith(Result.failure(Exception(stringToJson.getString("message"))))
            }
        }

        override fun onFailure(call: Call<Res>, t: Throwable) {
            Log.d("HttpResponse", "error: $t")
            it.resumeWithException(t)
        }
    })
}