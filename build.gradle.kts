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

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import com.insiderser.android.template.buildSrc.Versions
import com.insiderser.android.template.buildSrc.loadProperties
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// FIXME: cannot use imports in buildscript: https://github.com/gradle/gradle/issues/9270
@Suppress("RemoveRedundantQualifierName")
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://jitpack.io")
    }
    dependencies {
        classpath(com.insiderser.android.template.buildSrc.Libs.androidGradlePlugin)
        classpath(com.insiderser.android.template.buildSrc.Libs.Kotlin.gradlePlugin)
        classpath(com.insiderser.android.template.buildSrc.Libs.AndroidX.Navigation.safeArgs)
    }
}

plugins {
    id("com.diffplug.gradle.spotless") version "3.27.1"
    id("com.github.ben-manes.versions") version "0.27.0"
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

loadProperties(file("local.properties"))

subprojects {
    apply(plugin = "com.diffplug.gradle.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            ktlint(Versions.ktlint).userData(
                mapOf(
                    // TODO: KtLint import ordering conflicts with IntelliJ's.
                    //   See https://github.com/pinterest/ktlint/issues/527
                    "disabled_rules" to "import-ordering"
                )
            )
            @Suppress("INACCESSIBLE_TYPE")
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            allWarningsAsErrors = true
            jvmTarget = Versions.jvmTarget

            val compilerArgs = mutableListOf(
                "-Xjsr305=strict",
                "-Xuse-experimental=kotlin.Experimental"
            )

            // We don't use coroutines or flow in those projects. Because of this,
            // Kotlin compiler will generate a warning that will fail our build
            // because we have `allWarningsAsErrors = true`
            val projectNamesWithoutCoroutinesFlow = arrayOf("model", "test-shared")
            if (this@subprojects.name !in projectNamesWithoutCoroutinesFlow) {
                compilerArgs += listOf(
                    "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
                )
            }
            freeCompilerArgs = compilerArgs
        }
    }
}

tasks.withType<DependencyUpdatesTask> {
    checkForGradleUpdate = false
    rejectVersionIf {
        candidate.version.contains("alpha") ||
            // Reject Kotlin early access preview (EAP) releases
            candidate.version.contains("eap")
    }
}
