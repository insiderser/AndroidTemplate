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
package com.insiderser.android.template.prefs.data.delegate

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.annotation.WorkerThread
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal sealed class ObservablePreference<T>(
    private val sharedPreferences: Lazy<SharedPreferences>,
    private val preferenceKey: String
) : ReadOnlyProperty<Any, Flow<T>> {

    // Not null only if registered
    private var onPreferenceChangeListener: OnSharedPreferenceChangeListener? = null

    private val channel = ConflatedBroadcastChannel<T>()

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Flow<T> {
        maybeInit()
        return channel.asFlow()
    }

    private fun maybeInit() {
        if (onPreferenceChangeListener == null) {
            synchronized(this) {
                if (onPreferenceChangeListener == null) {
                    init()
                }
            }
        }
    }

    private fun init() {
        updateValue()
        onPreferenceChangeListener = OnSharedPreferenceChangeListener { _, key ->
            if (key == preferenceKey) {
                updateValue()
            }
        }
        sharedPreferences.value.registerOnSharedPreferenceChangeListener(onPreferenceChangeListener)
    }

    private fun updateValue() {
        val newValue = getCurrentPreferenceValue(sharedPreferences.value, preferenceKey)
        if (newValue != channel.valueOrNull) {
            channel.offer(newValue)
        }
    }

    protected abstract fun getCurrentPreferenceValue(
        sharedPreferences: SharedPreferences,
        key: String
    ): T
}

internal class ObservableStringPreference(
    sharedPreferences: Lazy<SharedPreferences>,
    preferenceKey: String,
    private val defaultValue: String? = null
) : ObservablePreference<String?>(sharedPreferences, preferenceKey) {

    override fun getCurrentPreferenceValue(
        sharedPreferences: SharedPreferences,
        key: String
    ): String? = sharedPreferences.getString(key, defaultValue)
}
