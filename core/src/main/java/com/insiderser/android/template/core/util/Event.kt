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

package com.insiderser.android.template.core.util

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
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
    fun getContentIfNotHandled(): T? = if (!hasBeenHandled) {
        hasBeenHandled = true
        content
    } else null

    /**
     * Return the content, even if it's already been handled.
     */
    @VisibleForTesting
    fun peekContent(): T = content
}

@Suppress("FunctionName")
fun Event(): Event<Unit> = Event(Unit)

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

/**
 * Adds the given [onChanged] lambda as an observer for [Event] within
 * the lifespan of the given [owner].
 * The events are dispatched on the main thread. If [LiveData] already has unconsumed events
 * set, it will be delivered to the [onChanged].
 *
 * The observer will only receive events if the owner is in
 * [started][androidx.lifecycle.Lifecycle.State.STARTED] or
 * [resumed][androidx.lifecycle.Lifecycle.State.RESUMED] state (active).
 *
 * If the owner moves to the [destroyed][androidx.lifecycle.Lifecycle.State.DESTROYED] state,
 * the observer will automatically be removed.
 *
 * When data changes while the [owner] is not active, it will not receive any updates.
 * If it becomes active again, it will receive the last available data automatically.
 *
 * LiveData keeps a strong reference to the observer and the owner as long as the
 * given [LifecycleOwner] is not destroyed. When it is destroyed, LiveData removes references to
 * the observer and the owner.
 *
 * If the given owner is already in [destroyed][androidx.lifecycle.Lifecycle.State.DESTROYED] state,
 * LiveData ignores the call.
 */
inline fun <T : Any> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    crossinline onChanged: (T) -> Unit
) {
    observe(owner, EventObserver { t ->
        onChanged(t)
    })
}
