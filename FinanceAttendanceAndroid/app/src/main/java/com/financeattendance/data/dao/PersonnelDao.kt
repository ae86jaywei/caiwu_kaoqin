package com.financeattendance.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financeattendance.data.entity.Personnel
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonnelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(person: Personnel): Long

    @Update
    suspend fun update(person: Personnel): Int

    @Delete
    suspend fun delete(person: Personnel): Int

    @Query("SELECT * FROM personnel ORDER BY create_time DESC")
    fun queryAllPersons(): Flow<List<Personnel>>

    @Query("SELECT * FROM personnel WHERE id = :id")
    fun getPersonById(id: String): Flow<Personnel?>
}
