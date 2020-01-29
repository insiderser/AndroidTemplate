/*
 * Copyright 2020 Oleksandr Bezushko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.insiderser.android.template.core.domain

import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.PROTECTED
import com.insiderser.android.template.core.result.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 * Executes business logic asynchronously.
 *
 * @param coroutineDispatcher Dispatcher that [execute] will be called on.
 * @param P Type of parameter that will be passed to [execute] function.
 * @param R Type that will be returned, wrapped in `Flow<Result<R>>`.
 * @see kotlinx.coroutines.Dispatchers
 */
abstract class UseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher
) {

    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(coroutineDispatcher + job)

    /**
     * Execute this use case with the given params. Can be called on any thread.
     * @return [Flow] where the result will be posted.
     */
    operator fun invoke(param: P): Flow<Result<R>> {
        val channel = ConflatedBroadcastChannel<Result<R>>()
        this(param, channel)
        return channel.asFlow()
    }

    /**
     * Execute this use case with the given params. Can be called on any thread.
     * @param channel Where the result should be posted?
     */
    @VisibleForTesting
    operator fun invoke(param: P, channel: SendChannel<Result<R>>) {
        coroutineScope.launch {
            channel.offer(Result.Loading)
            try {
                val result = execute(param)
                channel.offer(Result.Success(result))
            } catch (e: CancellationException) {
                Timber.d("Job was cancelled")
                throw e
            } catch (e: Exception) {
                Timber.i(e, "Use case execution failed")
                channel.offer(Result.Error(e))
            }
        }
    }

    /**
     * Execute this use case, suspending calling coroutine until finished.
     */
    suspend fun executeNow(param: P): R = withContext(coroutineDispatcher) {
        execute(param)
    }

    /**
     * Executes core logic.
     *
     * Will be called on a [coroutineDispatcher] passed as a parameter
     * to [UseCase] constructor. Can be canceled at any moment.
     *
     * Execution is considered successful if it returns without any [Exception].
     */
    @Throws(Exception::class)
    @VisibleForTesting(otherwise = PROTECTED)
    abstract suspend fun execute(param: P): R

    /**
     * Cancel all ongoing jobs of this use case. This use case can't be used anymore.
     */
    fun cancel() {
        job.cancel()
    }
}

/**
 * Execute this use case. Can be called on any thread.
 * @return A [Flow] where the result will be posted.
 */
operator fun <R> UseCase<Unit, R>.invoke(): Flow<Result<R>> = invoke(Unit)

/**
 * Execute this use case, suspending calling coroutine until finished.
 * @return Either [Result.Success] or [Result.Error].
 */
suspend fun <R> UseCase<Unit, R>.executeNow(): R = executeNow(Unit)
