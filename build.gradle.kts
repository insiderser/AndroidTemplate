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
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// FIXME: cannot use imports in buildscript: https://github.com/gradle/gradle/issues/9270
@Suppress("RemoveRedundantQualifierName")
buildscript {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
        mavenCentral()
    }
    dependencies {
        classpath(com.insiderser.android.template.buildSrc.Libs.androidGradlePlugin)
        classpath(com.insiderser.android.template.buildSrc.Libs.Kotlin.gradlePlugin)
        classpath(com.insiderser.android.template.buildSrc.Libs.AndroidX.Navigation.safeArgs)
    }
}

plugins {
    id("com.diffplug.gradle.spotless") version "4.0.1"
    id("com.github.ben-manes.versions") version "0.28.0"
}

allprojects {
    repositories {
        google()
        jcenter()
        maven("https://jitpack.io")
        mavenCentral()
    }
}

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
            licenseHeaderFile(rootProject.file("copyright.kt"))
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            allWarningsAsErrors = true
            jvmTarget = Versions.jvmTarget.toString()
            freeCompilerArgs = mutableListOf(
                "-Xjsr305=strict",
                "-Xallow-result-return-type",
                "-Xopt-in=kotlin.RequiresOptIn",
                "-progressive",
                "-Xuse-experimental=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xuse-experimental=kotlinx.coroutines.FlowPreview"
            )
        }
    }
}

tasks.withType<DependencyUpdatesTask> {
    checkForGradleUpdate = false
    rejectVersionIf {
        candidate.version.contains("alpha") ||
            candidate.version.contains("beta") ||
            // Kotlin early access preview (EAP).
            candidate.version.contains("eap") ||
            candidate.version.contains(Regex("""-M\d"""))
    }
}
