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
package com.insiderser.android.template.core.dagger

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.RUNTIME

/**
 * By default, if no scope annotation is present, the injector creates an instance, uses the
 * instance for one injection, and then forgets it. If a scope annotation is present, the
 * injector may retain the instance for possible reuse in a later injection.
 *
 * Scopes TL;DR:
 *  - No scope = new instance created every time
 *  - `@Singleton` = only one instance
 *  - `@CustomScope` = instance reused depending on the component’s lifecycle
 *
 * In Dagger, an unscoped component cannot depend on a scoped component. As
 * CoreComponent is a scoped component (`@Singleton`, we create a custom
 * scope to be used by all fragment components. Additionally, a component with a specific scope
 * cannot have a sub component with the same scope.
 *
 * The [ActivityScope] scoping annotation specifies that the lifespan of a dependency be the same
 * as that of an [activity][android.app.Activity]. This is used to annotate dependencies that behave
 * like a singleton within the lifespan of an [android.app.Activity].
 *
 * `@Singleton` is used to specify that the lifespan
 * of a dependency be the same as that of the [android.app.Application].
 *
 * @see FragmentScope
 * @see FeatureScope
 */
@MustBeDocumented
@Retention(RUNTIME)
@Scope
annotation class ActivityScope

/**
 * Just like [ActivityScope], this annotation tells dagger that the lifespan of
 * a dependency should be the same as that of a [android.app.Fragment].
 *
 * @see ActivityScope
 * @see FeatureScope
 */
@MustBeDocumented
@Retention(RUNTIME)
@Scope
annotation class FragmentScope

/**
 * Tells Dagger that the lifespan of a dependency
 * should be the same as that of a feature components.
 *
 * Use this only when the dependency is bound to that feature and
 * you have a *multi-module project*, where your feature has its own module.
 *
 * @see ActivityScope
 * @see FragmentScope
 */
@MustBeDocumented
@Retention(RUNTIME)
@Scope
annotation class FeatureScope
