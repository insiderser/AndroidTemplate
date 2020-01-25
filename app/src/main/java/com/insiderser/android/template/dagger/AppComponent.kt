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
package com.insiderser.android.template.dagger

import com.insiderser.android.template.MyApplication
import com.insiderser.android.template.core.dagger.CoreComponent
import com.insiderser.android.template.core.dagger.CoreModule
import com.insiderser.android.template.core.dagger.ViewModelFactoryModule
import com.insiderser.android.template.prefs.data.dagger.PreferencesStorageComponent
import com.insiderser.android.template.prefs.data.dagger.PreferencesStorageModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

/**
 * Main application-level dagger component that holds everything together.
 *
 * Use [DaggerAppComponent.factory] to create [AppComponent].
 *
 * Feature modules may create separate module components
 * that depend on one of [AppComponent]'s parent components.
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        CoreModule::class,
        ContextModule::class,
        ViewModelFactoryModule::class,
        ActivityBindingModule::class,
        PreferencesStorageModule::class
    ]
)
internal interface AppComponent : AndroidInjector<MyApplication>, CoreComponent,
    PreferencesStorageComponent {

    /**
     * Dagger factory for building [AppComponent], binding instances into a dagger graph.
     */
    @Component.Factory
    interface Factory {

        /**
         * Create [AppComponent] & bind [MyApplication] into a dagger graph.
         */
        fun create(@BindsInstance application: MyApplication): AppComponent
    }
}
