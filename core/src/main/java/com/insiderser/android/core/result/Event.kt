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
