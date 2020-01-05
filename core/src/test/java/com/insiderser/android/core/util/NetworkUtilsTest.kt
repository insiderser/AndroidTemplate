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
@file:Suppress("DEPRECATION")

package com.insiderser.android.core.util

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
