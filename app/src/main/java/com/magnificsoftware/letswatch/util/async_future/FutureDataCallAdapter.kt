package com.magnificsoftware.letswatch.util.async_future

import com.magnificsoftware.letswatch.util.ApiResponse
import java9.util.concurrent.CompletableFuture
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.Future

/**
 * A Retrofit adapter that converts the Call into a ApiResponse.
 * @param <R>
</R> */
class FutureDataCallAdapter<R>(
    private val responseType: Type,
) :
    CallAdapter<R, Future<ApiResponse<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<R>): Future<ApiResponse<R>> {
        val completableFuture = CompletableFuture<ApiResponse<R>>()
        call.enqueue(object : Callback<R> {
            override fun onResponse(call: Call<R>, response: Response<R>) {
                completableFuture.complete(ApiResponse.create(response))
            }

            override fun onFailure(call: Call<R>, throwable: Throwable) {
                completableFuture.complete(ApiResponse.create(throwable))
            }
        })
        return completableFuture
    }
}