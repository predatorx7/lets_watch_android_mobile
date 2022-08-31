package com.magnificsoftware.letswatch.util.async_future

import android.os.Handler
import android.os.Looper
import java9.util.concurrent.CompletableFuture
import java.util.concurrent.CancellationException
import java.util.concurrent.Executor
import java.util.concurrent.Future

class AsyncFuture<T>(
    task: AsyncFutureTask<T>,
    executor: Executor? = null,
) {
    private val completableFuture = CompletableFuture<T?>()

    init {
        completableFuture.whenComplete { r, t ->
            Handler(Looper.getMainLooper()).post {
                if (t is CancellationException) {
                    task.onCancelled()
                } else {
                    task.onCompleted(r, t)
                }
            }
        }

        if (executor != null) {
            completableFuture.completeAsync({
                task.execute()
            }, executor)
        } else {
            completableFuture.completeAsync {
                task.execute()
            }
        }
    }

    val isLoading get(): Boolean = !completableFuture.isDone

    fun cancel() {
        completableFuture.cancel(true)
    }

    fun asFuture(): Future<T?> = completableFuture
}