package com.financeattendance.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financeattendance.data.entity.SalaryRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface SalaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: SalaryRecord): Long

    @Update
    suspend fun update(record: SalaryRecord): Int

    @Delete
    suspend fun delete(record: SalaryRecord): Int

    @Query("SELECT * FROM salary_record WHERE (:personId = '' OR person_id = :personId) ORDER BY pay_date DESC")
    fun queryRecords(personId: String): Flow<List<SalaryRecord>>

    @Query("SELECT * FROM salary_record WHERE id = :id")
    fun getRecordById(id: String): Flow<SalaryRecord?>
}
