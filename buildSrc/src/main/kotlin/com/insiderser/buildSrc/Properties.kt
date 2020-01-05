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

package com.insiderser.buildSrc

import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import java.util.Properties

/**
 * Load all properties from `local.properties` into [project][Project]'s [extra].
 *
 * Loaded properties can be retrieved from [extra] or using [Project.findProperty].
 */
fun Project.loadLocalProperties() {
    val localPropertiesFile = file("local.properties")
    if (localPropertiesFile.exists()) {
        val localProperties = Properties()
        localPropertiesFile.inputStream().use { inputStream ->
            localProperties.load(inputStream)
        }

        localProperties.forEach { key, value ->
            check(key is String)
            extra[key] = value
        }
    }
}
