package com.financeattendance.data.repository

import com.financeattendance.data.dao.FinanceDao
import com.financeattendance.data.entity.FinanceRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FinanceRepository @Inject constructor(
    private val financeDao: FinanceDao
) {
    suspend fun addRecord(record: FinanceRecord): Long = withContext(Dispatchers.IO) {
        financeDao.insert(record)
    }
    suspend fun updateRecord(record: FinanceRecord): Int = withContext(Dispatchers.IO) {
        financeDao.update(record)
    }
    suspend fun deleteRecord(record: FinanceRecord): Int = withContext(Dispatchers.IO) {
        financeDao.delete(record)
    }
    
    fun queryRecords(startDate: String, endDate: String, type: String): Flow<List<FinanceRecord>> =
        financeDao.queryRecords(startDate, endDate, type)
    
    fun getRecordById(id: String): Flow<FinanceRecord?> = financeDao.getRecordById(id)
}