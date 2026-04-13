package com.financeattendance.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.financeattendance.data.entity.FinanceRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(record: FinanceRecord): Long

    @Update
    suspend fun update(record: FinanceRecord): Int

    @Delete
    suspend fun delete(record: FinanceRecord): Int

    @Query("SELECT * FROM finance_record WHERE date BETWEEN :startDate AND :endDate AND (:recordType = '' OR record_type = :recordType) ORDER BY date DESC")
    fun queryRecords(startDate: String, endDate: String, recordType: String): Flow<List<FinanceRecord>>

    @Query("SELECT * FROM finance_record WHERE id = :id")
    fun getRecordById(id: String): Flow<FinanceRecord?>
}
