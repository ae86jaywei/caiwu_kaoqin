package com.financeattendance.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financeattendance.data.entity.WorkRecord

@Dao
interface WorkDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: WorkRecord)

    @Update
    suspend fun update(record: WorkRecord)

    @Delete
    suspend fun delete(record: WorkRecord)

    @Query("SELECT * FROM work_record WHERE (:personId = '' OR person_id = :personId) ORDER BY work_date DESC")
    suspend fun queryRecords(personId: String): List<WorkRecord>

    @Query("SELECT * FROM work_record WHERE id = :id")
    suspend fun getRecordById(id: String): WorkRecord?
}
