package com.financeattendance.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financeattendance.data.entity.FinanceRecord

@Dao
interface FinanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: FinanceRecord)

    @Update
    suspend fun update(record: FinanceRecord)

    @Delete
    suspend fun delete(record: FinanceRecord)

    @Query("SELECT * FROM finance_record WHERE date BETWEEN :startDate AND :endDate AND (:recordType = '' OR record_type = :recordType) ORDER BY date DESC")
    suspend fun queryRecords(startDate: String, endDate: String, recordType: String): List<FinanceRecord>

    @Query("SELECT * FROM finance_record WHERE id = :id")
    suspend fun getRecordById(id: String): FinanceRecord?
}
