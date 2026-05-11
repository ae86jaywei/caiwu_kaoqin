package com.financeattendance.data.repository

import com.financeattendance.data.dao.WorkDao
import com.financeattendance.data.entity.WorkRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WorkRepository @Inject constructor(
    private val workDao: WorkDao
) {
    suspend fun addRecord(record: WorkRecord): Long = withContext(Dispatchers.IO) {
        workDao.insert(record)
    }
    suspend fun updateRecord(record: WorkRecord): Int = withContext(Dispatchers.IO) {
        workDao.update(record)
    }
    suspend fun deleteRecord(record: WorkRecord): Int = withContext(Dispatchers.IO) {
        workDao.delete(record)
    }
    
    fun queryRecords(personId: String?): Flow<List<WorkRecord>> =
        workDao.queryRecords(personId ?: "")
    
    fun getRecordById(id: String): Flow<WorkRecord?> = workDao.getRecordById(id)
}