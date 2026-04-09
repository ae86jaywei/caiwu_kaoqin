package com.financeattendance.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financeattendance.data.entity.AttendanceRecord

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: AttendanceRecord)

    @Update
    suspend fun update(record: AttendanceRecord)

    @Delete
    suspend fun delete(record: AttendanceRecord)

    @Query("SELECT * FROM attendance_record WHERE (:startDate = '' OR clock_date BETWEEN :startDate AND :endDate) AND (:personId = '' OR person_id = :personId) AND (:projectId = '' OR project_id = :projectId) ORDER BY clock_date DESC")
    suspend fun queryRecords(startDate: String, endDate: String, personId: String, projectId: String): List<AttendanceRecord>

    @Query("SELECT * FROM attendance_record WHERE id = :id")
    suspend fun getRecordById(id: String): AttendanceRecord?
}
