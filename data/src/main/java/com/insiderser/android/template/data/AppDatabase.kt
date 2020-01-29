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
package com.insiderser.android.template.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asExecutor

/**
 * A main Room database. Here you can retrieve all app DAOs.
 *
 * You don't need to use this class directly. Instead, get one of DAOs using Dagger.
 *
 * In big projects, it's preferable to create separate database classes for each
 * feature/module. That's because if you have one DB for the whole app,
 * it won't scale very well and has no isolation. On the other hand,
 * with feature-based DBs you create separate DBs for each feature,
 * which scales very well and has perfect isolation, but it costs more because
 * maintaining DB connections is expensive, and your DBs can't share data between
 * each other. You can go hybrid, merging dependent databases together and
 * keeping separate DBs in isolation. Also, Google says that they are working
 * on making Room merge dependent DBs at runtime, but it's not available yet.
 *
 * @see com.insiderser.android.template.data.dagger.DataModule
 * @see AppDatabase.create
 */
@Database(
    entities = [
        MyEntity::class
    ],
    version = AppDatabase.DB_VERSION
)
abstract class AppDatabase : RoomDatabase() {

    abstract val myDao: MyDao

    companion object {

        internal const val DB_VERSION = 1
        private const val DB_NAME = "app.db"

        /**
         * Create an [AppDatabase] instance that is connected to the persistent SQLite
         * database. If the database doesn't exist, it will be created.
         *
         * **Note**: you don't need to use this method directly — use dagger for that.
         *
         * @param context an application context
         * @see com.insiderser.android.template.data.dagger.DataModule
         */
        @JvmStatic
        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .setTransactionExecutor(Dispatchers.IO.asExecutor())
                .build()
    }
}
