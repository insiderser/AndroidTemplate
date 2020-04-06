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

/**
 * Executes a single unit of business logic.
 *
 * @param P Type of parameter that will be passed to [execute] function.
 * @param R Type that will be returned, wrapped in `Flow<Result<R>>`.
 */
abstract class UseCase<in P, R> {

    /**
     * Execute this use case, suspending calling coroutine until finished.
     */
    suspend operator fun invoke(param: P): Result<R> = runCatching { execute(param) }

    /**
     * Executes core logic.
     *
     * **Note**: this method can be called on any thread. You must take care that no
     * heavy logic are run on the main thread.
     *
     * Execution is considered successful only if it returns without any [Exception]s thrown.
     */
    protected abstract suspend fun execute(param: P): R
}

/**
 * Execute this use case, suspending calling coroutine until finished.
 */
suspend operator fun <R> UseCase<Unit, R>.invoke(): Result<R> = invoke(Unit)
