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

import com.insiderser.android.template.core.dagger.ActivityScope
import com.insiderser.android.template.ui.MainActivity
import com.insiderser.android.template.ui.MainActivityModule
import com.insiderser.android.template.ui.launcher.LauncherActivity
import com.insiderser.android.template.ui.launcher.LauncherActivityModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * We want Dagger.Android to create a SubComponent which has a parent Component of whichever module
 * ActivityBindingModule is on, in our case that will be [AppComponent]. You never
 * need to tell [AppComponent] that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that [AppComponent] exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the
 * specified modules and be aware of a scope annotation [ActivityScope].
 */
@Module
internal abstract class ActivityBindingModule {

    /**
     * Tell dagger to create a SubComponent for [MainActivity].
     */
    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [LauncherActivityModule::class])
    abstract fun launcherActivity(): LauncherActivity
}
