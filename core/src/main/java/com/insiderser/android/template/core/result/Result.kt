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
package com.insiderser.android.template.core.result

import com.insiderser.android.template.core.result.Result.Error
import com.insiderser.android.template.core.result.Result.Loading
import com.insiderser.android.template.core.result.Result.Success

/**
 * A generic class that holds the result of an operation with its loading status.
 *
 * [Result] is either [Success], [Error] or [Loading].
 *
 * @param R result type, that will be returned if the [Result] is [Success].
 */
sealed class Result<out R> {

    /**
     * The operation was successful & (maybe) returned some [data].
     */
    data class Success<out R>(val data: R) : Result<R>()

    /**
     * An [error][cause] occurred during the operation.
     */
    data class Error(val cause: Throwable) : Result<Nothing>()

    /**
     * The operation is in progress.
     */
    object Loading : Result<Nothing>() {
        override fun toString(): String = javaClass.canonicalName!!
    }
}

/**
 * Whether the operation was successful and returned some data.
 *
 * @return `true` if the [Result] is [Success] & holds non-null [data][Success.data].
 */
val Result<*>.succeeded
    get() = this is Success && data != null

/**
 * If the [Result] is [Success], return its data; otherwise, return [fallback].
 */
fun <R> Result<R>.successOr(fallback: R): R =
    (this as? Success<R>)?.data ?: fallback
