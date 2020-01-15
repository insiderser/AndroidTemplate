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
import com.insiderser.buildSrc.Libs
import com.insiderser.buildSrc.Versions
import com.insiderser.buildSrc.loadLocalProperties
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
        classpath(com.insiderser.buildSrc.Libs.androidGradlePlugin)
        classpath(com.insiderser.buildSrc.Libs.Kotlin.gradlePlugin)
        classpath(com.insiderser.buildSrc.Libs.AndroidX.Navigation.safeArgs)
    }
}

plugins {
    id("com.diffplug.gradle.spotless") version "3.27.1"
    id("com.github.ben-manes.versions") version "0.27.0"
    id("com.vanniktech.android.junit.jacoco") version "0.15.0"
}

allprojects {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

loadLocalProperties()

junitJacoco {
    jacocoVersion = Versions.jacoco
    includeNoLocationClasses = false
    excludes = listOf(
        // From https://github.com/vanniktech/gradle-android-junit-jacoco-plugin/blob/master/src/main/groovy/com/vanniktech/android/junit/jacoco/GenerationPlugin.groovy
        "**/R.*",
        "**/R$*.*",
        "**/R2.*", // ButterKnife Gradle Plugin.
        "**/R2$*.*", // ButterKnife Gradle Plugin.
        "**/*$$*",
        "**/*\$ViewInjector*.*", // Older ButterKnife Versions.
        "**/*\$ViewBinder*.*", // Older ButterKnife Versions.
        "**/*_ViewBinding*.*", // Newer ButterKnife Versions.
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*\$Lambda$*.*", // Jacoco cannot handle several "$" in class name.
        "**/*Dagger*.*", // Dagger auto-generated code.
        "**/*MembersInjector*.*", // Dagger auto-generated code.
        "**/*_Provide*Factory*.*", // Dagger auto-generated code.
        "**/*_Factory*.*", // Dagger auto-generated code.
        "**/*\$JsonObjectMapper.*", // LoganSquare auto-generated code.
        "**/*\$inlined$*.*", // Kotlin specific, Jacoco cannot handle several "$" in class name.
        "**/*\$Icepick.*", // Icepick auto-generated code.
        "**/*\$StateSaver.*", // android-state auto-generated code.
        "**/*AutoValue_*.*", // AutoValue auto-generated code.

        "**/*Binding.*", // Data/View Binding
        "**/*Module.*", // Dagger modules
        "**/*Component.*", // Dagger components
        "**/*Impl.*", // Room generated code
        "**/*Application.*" // Testing App classes is a pain in the ass. Is it even possible?
    )
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
            @Suppress("INACCESSIBLE_TYPE")
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = Versions.jvmTarget
            allWarningsAsErrors = true
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

    configurations.all {
        resolutionStrategy {
            // FIXME: JUnit classes are not resolved without this:
            force(Libs.Test.junit4)
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
