package com.financeattendance.data.repository

import com.financeattendance.data.dao.SalaryDao
import com.financeattendance.data.entity.SalaryRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SalaryRepository @Inject constructor(
    private val salaryDao: SalaryDao
) {
    suspend fun addRecord(record: SalaryRecord): Long = withContext(Dispatchers.IO) {
        salaryDao.insert(record)
    }
    suspend fun updateRecord(record: SalaryRecord): Int = withContext(Dispatchers.IO) {
        salaryDao.update(record)
    }
    suspend fun deleteRecord(record: SalaryRecord): Int = withContext(Dispatchers.IO) {
        salaryDao.delete(record)
    }
    
    fun queryRecords(personId: String?): Flow<List<SalaryRecord>> =
        salaryDao.queryRecords(personId ?: "")
    
    fun getRecordById(id: String): Flow<SalaryRecord?> = salaryDao.getRecordById(id)
}