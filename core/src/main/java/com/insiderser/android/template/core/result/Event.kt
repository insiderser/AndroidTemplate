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

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer

/**
 * Used as a wrapper for data that is exposed via [androidx.lifecycle.LiveData]
 * that represents an event.
 */
class Event<out T : Any>(private val content: T) {

    /**
     * `false` if [getContentIfNotHandled] was never called, `true` otherwise.
     */
    var hasBeenHandled: Boolean = false
        private set

    /**
     * Returns the content and prevents its use again.
     *
     * If the content has been handled, this returns `null`.
     */
    fun getContentIfNotHandled(): T? {
        return if (!hasBeenHandled) {
            hasBeenHandled = true
            content
        } else null
    }

    /**
     * Return the content, even if it's already been handled.
     */
    @VisibleForTesting
    fun peekContent() = content
}

/**
 * An [Observer] for [Event]s, simplifying the pattern of checking if the [Event]'s
 * content has been already handled.
 *
 * @param onEventUnhandledCallback invoked only if the [Event]'s content
 * hasn't been handled.
 */
class EventObserver<T : Any>(
    private val onEventUnhandledCallback: (T) -> Unit
) : Observer<Event<T>> {

    override fun onChanged(event: Event<T>?) {
        val content = event?.getContentIfNotHandled()
        if (content != null) {
            onEventUnhandledCallback(content)
        }
    }
}
