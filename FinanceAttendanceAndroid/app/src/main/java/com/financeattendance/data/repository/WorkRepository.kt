package com.financeattendance.data.repository

import com.financeattendance.data.dao.WorkDao
import com.financeattendance.data.entity.WorkRecord
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkRepository @Inject constructor(
    private val workDao: WorkDao
) {
    suspend fun addRecord(record: WorkRecord): Long = workDao.insert(record)
    suspend fun updateRecord(record: WorkRecord): Int = workDao.update(record)
    suspend fun deleteRecord(record: WorkRecord): Int = workDao.delete(record)
    
    fun queryRecords(personId: String?): Flow<List<WorkRecord>> =
        workDao.queryRecords(personId ?: "")
    
    fun getRecordById(id: String): Flow<WorkRecord?> = workDao.getRecordById(id)
}
