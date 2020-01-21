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
@file:Suppress("DEPRECATION")

package com.insiderser.android.template.core.util

import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class NetworkUtilsTest {

    @Test
    fun hasNetworkConnection_withInternet() {
        val ani: NetworkInfo = mockk()
        every { ani.isConnectedOrConnecting } returns true

        val cm: ConnectivityManager = mockk()
        every { cm.activeNetworkInfo } returns ani

        val victim = NetworkUtils(cm)
        assertThat(victim.hasNetworkConnection()).isTrue()
    }

    @Test
    fun hasNetworkConnection_noInternet() {
        val ani: NetworkInfo = mockk()
        every { ani.isConnectedOrConnecting } returns false

        val cm: ConnectivityManager = mockk()
        every { cm.activeNetworkInfo } returns ani

        val victim = NetworkUtils(cm)
        assertThat(victim.hasNetworkConnection()).isFalse()
    }

    @Test
    fun hasNetworkConnection_nullConnectivityManager() {
        val victim = NetworkUtils(cm = null)
        assertThat(victim.hasNetworkConnection()).isFalse()
    }

    @Test
    fun hasNetworkConnection_nullActiveNetworkInfo() {
        val cm: ConnectivityManager = mockk()
        every { cm.activeNetworkInfo } returns null

        val victim = NetworkUtils(cm)
        assertThat(victim.hasNetworkConnection()).isFalse()
    }
}