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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

abstract class ResultUseCase<in Params, Type> {

    protected abstract val workDispatcher: CoroutineDispatcher

    suspend operator fun invoke(param: Params): Result<Type> = withContext(workDispatcher) {
        execute(param)
    }

    protected abstract suspend fun execute(param: Params): Result<Type>
}

abstract class NoResultUseCase<in Params> {

    protected abstract val workDispatcher: CoroutineDispatcher

    suspend operator fun invoke(param: Params) {
        withContext(workDispatcher) {
            execute(param)
        }
    }

    protected abstract suspend fun execute(param: Params)
}

abstract class ObservableUseCase<in Param, Type> {

    private val eventChannel = ConflatedBroadcastChannel<Param>()

    protected abstract val workDispatcher: CoroutineDispatcher

    operator fun invoke(param: Param) {
        eventChannel.offer(param)
    }

    fun observe(): Flow<Type> = eventChannel.asFlow()
        .distinctUntilChanged()
        .flatMapLatest { execute(it) }
        .flowOn(workDispatcher)

    protected abstract suspend fun execute(param: Param): Flow<Type>
}

suspend operator fun <Type> ResultUseCase<Unit, Type>.invoke(): Result<Type> = invoke(Unit)
suspend operator fun NoResultUseCase<Unit>.invoke() = invoke(Unit)
operator fun <Type> ObservableUseCase<Unit, Type>.invoke() = invoke(Unit)
