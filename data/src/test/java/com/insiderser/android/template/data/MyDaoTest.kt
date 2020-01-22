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
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.insiderser.android.template.test.shared.util.await
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyDaoTest {

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: MyDao

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.myDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun assert_emptyTable_returnsNothing() {
        val allEntities = dao.findAll().await()
        assertThat(allEntities).isEmpty()

        val singleEntity = dao.findOneById(1).await()
        assertThat(singleEntity).isNull()
    }

    @Test
    fun insertOne_withId() {
        val id = 6
        val name = "Alex"
        val entity = MyEntity(id = id, name = name)

        val newId = dao.insert(entity)
        assertThat(newId).isEqualTo(id)

        val insertedEntity = dao.findOneById(id).await()
        assertThat(insertedEntity).isNotNull()
        assertThat(insertedEntity!!.id).isEqualTo(id)
        assertThat(insertedEntity.name).isEqualTo(name)

        val allEntities = dao.findAll().await()
        assertThat(allEntities).containsExactly(insertedEntity)
    }

    @Test
    fun insertOne_id0() {
        val name = "Alex"
        val entity = MyEntity(id = 0, name = name)

        val id = dao.insert(entity).toInt()
        assertThat(id).isAtLeast(1)

        val insertedEntity = dao.findOneById(id).await()
        assertThat(insertedEntity).isEqualTo(MyEntity(id, name))

        val allEntities = dao.findAll().await()
        assertThat(allEntities).containsExactly(insertedEntity)
    }

    @Test
    fun assert_insertWithExistingId_replacesEntry() {
        val id = 100
        val newName = "Sam"

        dao.insert(MyEntity(id = id, name = "Alex"))

        val newEntity = MyEntity(id = id, name = newName)
        val newId = dao.insert(newEntity)

        assertThat(newId).isEqualTo(id)
        assertThat(dao.findOneById(id).await()).isEqualTo(newEntity)
    }

    @Test
    fun assert_delete_deletesEntry() {
        val id = 100

        dao.insert(MyEntity(id = id, name = "Alex"))
        val affectedEntries = dao.deleteOneById(id)

        assertThat(affectedEntries).isEqualTo(1)
        assertThat(dao.findAll().await()).isEmpty()
        assertThat(dao.findOneById(id).await()).isNull()
    }

    @Test
    fun assert_deleteNotInserted_returns0() {
        val id = 100
        val affectedEntries = dao.deleteOneById(id)
        assertThat(affectedEntries).isEqualTo(0)
    }
}
