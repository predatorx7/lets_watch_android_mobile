package com.magnificsoftware.letswatch.util.async_future

interface AsyncFutureTask<T> {
    fun execute(): T?

    fun onCompleted(value: T?, error: Throwable?) {}

    fun onCancelled() {}
}