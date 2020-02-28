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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.insiderser.android.template.test.shared.util.await
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
    fun givenEmptyTable_find_returnsNothing() {
        val allEntities = dao.findAll().await()
        assertThat(allEntities).isEmpty()

        val singleEntity = dao.findOneById(1).await()
        assertThat(singleEntity).isNull()
    }

    @Test
    fun givenEntityWithId_insertOne_insertsIntoTableWithThisId() {
        val id = 6
        val name = "Alex"
        val entity = MyEntity(id = id, name = name)

        val newId = dao.insertOne(entity)
        assertThat(newId).isEqualTo(id)

        val insertedEntity = dao.findOneById(id).await()
        assertThat(insertedEntity!!.id).isEqualTo(id)
        assertThat(insertedEntity.name).isEqualTo(name)

        val allEntities = dao.findAll().await()
        assertThat(allEntities).containsExactly(insertedEntity)
    }

    @Test
    fun givenEntityWithId0_insertOne_insertsIntoTableWithGeneratedId() {
        val name = "Alex"
        val entity = MyEntity(id = 0, name = name)

        val id = dao.insertOne(entity).toInt()
        assertThat(id).isAtLeast(1)

        val expected = MyEntity(id, name)
        assertThat(dao.findOneById(id).await()).isEqualTo(expected)
        assertThat(dao.findAll().await()).containsExactly(expected)
    }

    @Test
    fun whenInsertingEntityWithExistingId_insertOne_replacesEntry() {
        val id = 100

        dao.insertOne(MyEntity(id = id, name = "Alex"))

        val newEntity = MyEntity(id = id, name = "Sam")
        val newId = dao.insertOne(newEntity)

        assertThat(newId).isEqualTo(id)
        assertThat(dao.findOneById(id).await()).isEqualTo(newEntity)
        assertThat(dao.findAll().await()).containsExactly(newEntity)
    }

    @Test
    fun givenInsertedEntryId_deleteOneById_deletesEntry() {
        val id = 100

        dao.insertOne(MyEntity(id = id, name = "Alex"))
        val affectedEntries = dao.deleteOneById(id)

        assertThat(affectedEntries).isEqualTo(1)
        assertThat(dao.findAll().await()).isEmpty()
        assertThat(dao.findOneById(id).await()).isNull()
    }

    @Test
    fun givenNonEmptyTable_deleteAll_deletesAllEntries() {
        dao.insertOne(MyEntity(id = 1, name = "Alex"))
        dao.insertOne(MyEntity(id = 2, name = "Alex"))
        dao.insertOne(MyEntity(id = 3, name = "Alex"))
        dao.insertOne(MyEntity(id = 4, name = "Alex"))

        val affectedCount = dao.deleteAll()
        assertThat(affectedCount).isEqualTo(4)
        assertThat(dao.findAll().await()).isEmpty()
        assertThat(dao.findOneById(1).await()).isNull()
    }

    @Test
    fun givenEmptyTable_delete_doesNothing() {
        assertThat(dao.deleteAll()).isEqualTo(0)
        assertThat(dao.deleteOneById(1)).isEqualTo(0)
    }
}
