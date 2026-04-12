package com.financeattendance.data.repository

import com.financeattendance.data.dao.SalaryDao
import com.financeattendance.data.entity.SalaryRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SalaryRepository @Inject constructor(
    private val salaryDao: SalaryDao
) {
    suspend fun addRecord(record: SalaryRecord): Long = salaryDao.insert(record)
    suspend fun updateRecord(record: SalaryRecord): Int = salaryDao.update(record)
    suspend fun deleteRecord(record: SalaryRecord): Int = salaryDao.delete(record)
    
    fun queryRecords(personId: String?): Flow<List<SalaryRecord>> =
        salaryDao.queryRecords(personId ?: "")
    
    fun getRecordById(id: String): Flow<SalaryRecord?> = salaryDao.getRecordById(id)
}
