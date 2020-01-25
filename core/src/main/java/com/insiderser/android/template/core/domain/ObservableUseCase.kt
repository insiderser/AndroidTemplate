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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

/**
 * Executes its business logic in [execute] method. Implementations can post the result to
 * [`result MediatorLiveData`][result].
 *
 * @param P Type of parameter that will be passed to [execute] function.
 * @param R Type that will be returned, wrapped in [LiveData].
 */
abstract class ObservableUseCase<in P, R> {

    protected val result = MediatorLiveData<R>()

    /**
     * Returns a [LiveData] observable where all results will be posted.
     *
     * **Note**: this method only returns the observable. To execute this use case,
     * call [execute] method.
     */
    fun observe(): LiveData<R> = result

    /**
     * Execute the logic.
     */
    abstract suspend fun execute(param: P)
}

/**
 * Execute the logic.
 *
 * **Note**: make sure to call it on a suitable thread.
 */
suspend fun <R> ObservableUseCase<Unit, R>.execute() = execute(Unit)
