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
package com.insiderser.android.core.util

/**
 * Convenience method for callbacks/listeners whose return value indicates
 * whether the event was consumed or not.
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
 * This extension allows to force Kotlin to check that all variants are matched.
 *
 * Example:
 * ```
 * when(sealedObject) {
 *     is OneType -> ...
 *     is AnotherType -> ...
 * }.checkAllMatched
 * ```
 */
val <T> T.checkAllMatched: T
    get() = this
