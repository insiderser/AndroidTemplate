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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDelegate
import com.insiderser.android.template.model.Theme

/**
 * Convenience method for callbacks/listeners whose return value indicates
 * whether the event was consumed or not.
 * @param f Function to execute (will be called only once).
 * @return Always `true`.
 */
inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

/**
 * Force Kotlin to check that all variants in `when` statement are matched.
 *
 * By default, Kotlin doesn't care whether we consumed all possible variants
 * in `when` statements as long as the statement doesn't return any value.
 *
 * This extension allows us to force Kotlin to check that all variants are matched.
 *
 * Example:
 * ```
 * when(sealedObject) {
 *     is OneType -> ...
 *     is AnotherType -> ...
 * }.checkAllMatched()
 * ```
 */
fun <T> T.checkAllMatched(): T = this

/**
 * Inflate layout from the given [layout resource][resource].
 * @see LayoutInflater.inflate
 */
fun ViewGroup.inflate(@LayoutRes resource: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(resource, this, attachToRoot)

/**
 * Get [AppCompatDelegate] night mode for the given [Theme].
 */
fun Theme.toAppCompatNightMode(): Int = when (this) {
    Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
    Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
    Theme.FOLLOW_SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    Theme.AUTO_BATTERY -> AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
}
