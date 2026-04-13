package com.financeattendance.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financeattendance.data.entity.WorkRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: WorkRecord): Long

    @Update
    fun update(record: WorkRecord): Int

    @Delete
    fun delete(record: WorkRecord): Int

    @Query("SELECT * FROM work_record WHERE (:personId = '' OR person_id = :personId) ORDER BY work_date DESC")
    fun queryRecords(personId: String): Flow<List<WorkRecord>>

    @Query("SELECT * FROM work_record WHERE id = :id")
    fun getRecordById(id: String): Flow<WorkRecord?>
}
