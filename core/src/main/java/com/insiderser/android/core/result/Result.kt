/*
 * Copyright 2020 Oleksandr Bezushko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.insiderser.android.core.result

import com.insiderser.android.core.result.Result.Error
import com.insiderser.android.core.result.Result.Loading
import com.insiderser.android.core.result.Result.Success

/**
 * A generic class that holds the result of an operation with its loading status.
 *
 * [Result] is either [Success], [Error] or [Loading].
 *
 * @param R result type, that will be returned if the [Result] is [Success].
 */
sealed class Result<out R> {

    data class Success<out R>(val data: R) : Result<R>()
    data class Error(val cause: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

/**
 * Whether the operation was successful and returned some data.
 *
 * @return `true` if the [Result] is [Success] & holds non-null [data][Success.data].
 */
val Result<*>.succeeded
    get() = this is Success && data != null

/**
 * If the [Result] is [Success], return it's data; otherwise, return [fallback].
 */
fun <R> Result<R>.successOr(fallback: R): R =
        (this as? Success<R>)?.data ?: fallback
