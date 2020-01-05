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
    id("com.diffplug.gradle.spotless") version "3.27.0"
    id("com.github.ben-manes.versions") version "0.27.0"
    id("com.vanniktech.android.junit.jacoco") version "0.15.0"
    id("org.sonarqube") version "2.8"
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
    setIgnoreProjects(":test-shared")
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
        "**/*\$Lambda$*.*", // Jacoco can not handle several "$" in class name.
        "**/*Dagger*.*", // Dagger auto-generated code.
        "**/*MembersInjector*.*", // Dagger auto-generated code.
        "**/*_Provide*Factory*.*", // Dagger auto-generated code.
        "**/*_Factory*.*", // Dagger auto-generated code.
        "**/*\$JsonObjectMapper.*", // LoganSquare auto-generated code.
        "**/*\$inlined$*.*", // Kotlin specific, Jacoco can not handle several "$" in class name.
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
            ktlint(Versions.ktlint)
            @Suppress("INACCESSIBLE_TYPE")
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }
    }

    apply(plugin = "org.sonarqube")
    sonarqube {
        properties {
            // TODO: configure for new project
            property("sonar.projectName", "Android Template")
            property("sonar.projectKey", "Insiderser_AndroidTemplate")
            property("sonar.organization", "insiderser")
            property("sonar.host.url", "https://sonarcloud.io")
            property("sonar.sourceEncoding", "UTF-8")
            property("sonar.profile", "Android Lint")
            property("sonar.projectVersion", Versions.versionName)

            val sonarToken = System.getenv("SONAR_TOKEN")
                ?: findProperty("sonar.token")
            if (sonarToken != null) {
                property("sonar.login", sonarToken)
            }
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
        candidate.version.contains("alpha")
    }
}
