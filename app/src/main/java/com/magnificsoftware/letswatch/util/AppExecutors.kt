package com.magnificsoftware.letswatch.util

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Global executor pools for the whole application.
 *
 * Grouping tasks like this avoids the effects of task starvation (e.g. disk reads don't wait behind
 * webservice requests).
 */
@Singleton
open class AppExecutors(
    private val diskIO: ExecutorService,
    private val networkIO: ExecutorService,
    private val mainThread: Executor,
) {

    @Inject
    constructor() : this(
        Executors.newSingleThreadExecutor(),
        Executors.newFixedThreadPool(3),
        MainThreadExecutor()
    )

    fun diskIO(): Executor {
        return diskIO
    }

    private fun <RETURN> futureExecution(
        executor: ExecutorService,
        task: Callable<RETURN>
    ): Future<RETURN> {
        return executor.submit(task)
    }

    fun <RETURN> futureDiskIO(task: Callable<RETURN>): Future<RETURN> {
        return futureExecution(diskIO, task)
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun <RETURN> futureNetworkIO(task: Callable<RETURN>): Future<RETURN> {
        return futureExecution(networkIO, task)
    }

    fun mainThread(): Executor {
        return mainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
