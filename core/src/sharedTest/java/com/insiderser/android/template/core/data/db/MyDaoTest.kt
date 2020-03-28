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

package com.insiderser.android.template.core.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
class MyDaoTest {

    @Rule
    @JvmField
    val executorRule = InstantTaskExecutorRule()

    private lateinit var db: AppDatabase
    private lateinit var dao: MyDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.myDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun givenEmptyTable_find_returnsNothing() = runBlockingTest {
        val allEntities = dao.findAll().first()
        assertThat(allEntities).isEmpty()

        val singleEntity = dao.findOneById(1).first()
        assertThat(singleEntity).isNull()
    }

    @Test
    fun givenEntityWithId_insertOne_insertsIntoTableWithThisId() = runBlockingTest {
        val entity = MyEntity(id = 6, name = "Alex")

        val newId = dao.insertOne(entity)
        assertThat(newId).isEqualTo(entity.id)

        val loaded = dao.findOneById(entity.id).first()
        assertThat(loaded.id).isEqualTo(entity.id)
        assertThat(loaded.name).isEqualTo(entity.name)

        val allEntities = dao.findAll().first()
        assertThat(allEntities).containsExactly(loaded)
    }

    @Test
    fun givenEntityWithId0_insertOne_insertsIntoTableWithGeneratedId() = runBlockingTest {
        val entity = MyEntity(id = 0, name = "Alex")

        val id = dao.insertOne(entity).toInt()
        assertThat(id).isAtLeast(1)

        val expected = MyEntity(id, entity.name)
        assertThat(dao.findOneById(id).first()).isEqualTo(expected)
        assertThat(dao.findAll().first()).containsExactly(expected)
    }

    @Test
    fun whenInsertingEntityWithExistingId_insertOne_replacesEntry() = runBlockingTest {
        val id = 100

        dao.insertOne(MyEntity(id = id, name = "Alex"))

        val newEntity = MyEntity(id = id, name = "Sam")
        val newId = dao.insertOne(newEntity)

        assertThat(newId).isEqualTo(id)
        assertThat(dao.findOneById(id).first()).isEqualTo(newEntity)
        assertThat(dao.findAll().first()).containsExactly(newEntity)
    }

    @Test
    fun givenInsertedEntryId_deleteOneById_deletesEntry() = runBlockingTest {
        val id = 100

        dao.insertOne(MyEntity(id = id, name = "Alex"))
        val deleted = dao.deleteOneById(id)

        assertThat(deleted).isEqualTo(1)
        assertThat(dao.findAll().first()).isEmpty()
        assertThat(dao.findOneById(id).first()).isNull()
    }

    @Test
    fun givenNonEmptyTable_deleteAll_deletesAllEntries() = runBlockingTest {
        val n = 5
        for (id in 1..n) {
            dao.insertOne(MyEntity(id = id, name = "Alex"))
        }

        val deleted = dao.deleteAll()
        assertThat(deleted).isEqualTo(n)
        assertThat(dao.findAll().first()).isEmpty()
        assertThat(dao.findOneById(1).first()).isNull()
    }

    @Test
    fun givenEmptyTable_delete_doesNothing() = runBlockingTest {
        assertThat(dao.deleteAll()).isEqualTo(0)
        assertThat(dao.deleteOneById(1)).isEqualTo(0)
    }
}
